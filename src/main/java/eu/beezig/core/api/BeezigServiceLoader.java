/*
 * Copyright (C) 2017-2020 Beezig Team
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.api;

import com.google.common.collect.ImmutableSet;
import eu.beezig.core.Beezig;
import eu.beezig.core.autovote.AutovoteConfig;
import eu.beezig.core.command.CommandManager;
import eu.beezig.core.config.Settings;
import eu.beezig.core.data.HiveTitle;
import eu.beezig.core.net.BeezigNetManager;
import eu.beezig.core.net.profile.OwnProfile;
import eu.beezig.core.net.profile.UserProfile;
import eu.beezig.core.net.profile.override.UserOverride;
import eu.beezig.core.news.ForgeNewsEntry;
import eu.beezig.core.news.NewsEntry;
import eu.beezig.core.news.NewsType;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.TitleService;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.server.GameMode;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BeezigServiceLoader {
    private ServiceLoader<IBeezigService> services = ServiceLoader.load(IBeezigService.class,
            Beezig.get().isLaby() ? getClass().getClassLoader() : getClass().getClassLoader().getParent());
    private IBeezigService mainService;

    public IBeezigService getMainService() {
        return mainService;
    }

    public void attemptLoad() {
        Iterator<IBeezigService> iter = services.iterator();
        if(iter.hasNext()) {
            mainService = iter.next();
            registerCallbacks();
        }
    }

    private void registerCallbacks() {
        mainService.registerUserIndicator(uuid -> {
            BeezigNetManager mgr = Beezig.get().getNetworkManager();
            if(mgr == null || mgr.getProfilesCache() == null) return -1;
            Optional<UserProfile> profile = Beezig.get().getNetworkManager().getProfilesCache().getIfPresent(uuid);
            if(profile == null) return -1;
            return profile.map(user -> user.getRoleContainer().getRole().getIndex()).orElse(-1);
        });
        mainService.registerTitle(raw -> {
            if(!ServerHive.isCurrent()) return null;
            GameMode mode = Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
            if(mode instanceof HiveMode) {
                TitleService titles = ((HiveMode) mode).getTitleService();
                if(titles == null) return null;
                Pair<Integer, HiveTitle> pair = titles.getTitle(raw.getKey(), raw.getValue());
                if(pair == null) return null;
                return pair.getRight().getColoredName();
            }
            return null;
        });
        mainService.registerNormalizeMapName(StringUtils::normalizeMapName);
        mainService.registerFormatNumber(Message::formatNumber);
        mainService.registerTranslate(Message::translate);
        mainService.registerTranslateFormat(pair -> Beezig.api().translate(pair.getLeft(), pair.getRight()));
        mainService.registerBeezigDir(() -> Beezig.get().getBeezigDir());
        mainService.registerGetSetting(name -> Settings.valueOf(name).get().getValue());
        mainService.registerSetSetting(pair -> Beezig.cfg().set(Settings.valueOf(pair.getKey()), pair.getValue().toString()));
        mainService.registerSetSettingAsIs(pair -> Beezig.cfg().setAsIs(Settings.valueOf(pair.getKey()), pair.getValue()));
        mainService.registerGetLoadedNews(category -> {
            if(Beezig.get().getNewsManager() == null) return ImmutableSet.of();
            NewsType type;
            try {
                type = NewsType.valueOf(category);
            } catch (IllegalArgumentException noEnum) {
                return ImmutableSet.of();
            }
            Set<ForgeNewsEntry> result = new TreeSet<>(NewsEntry.compareForge().reversed());
            result.addAll(Beezig.get().getNewsManager().getLoadedNews().
                getOrDefault(type, ImmutableSet.of()).stream().map(NewsEntry::toForge).collect(Collectors.toList()));
            return result;
        });
        mainService.registerGetForumsNews(() -> {
            if(Beezig.get().getNewsManager() == null) return new HashSet<>();
            Set<ForgeNewsEntry> result = new TreeSet<>(NewsEntry.compareForge().reversed());
            result.addAll(Beezig.get().getNewsManager().downloadForums().stream().limit(10).map(NewsEntry::toForge).collect(Collectors.toList()));
            return result;
        });
        mainService.registerGetRegion(() -> {
            BeezigNetManager net = Beezig.net();
            if(net == null) return null;
            OwnProfile profile = net.getProfile();
            if(profile == null) return null;
            if(profile.getRegion() == null) return null;
            return profile.getRegion().getId();
        });
        mainService.registerSetAutovoteMaps(pair -> new AutovoteConfig().setMaps(pair.getKey(), pair.getValue()));
        mainService.registerSaveConfig(() -> {
            try {
                Beezig.cfg().save();
            } catch (IOException e) {
                ExceptionHandler.catchException(e, "Couldn't save config");
                Message.error(Message.translate("error.data_read"));
            }
        });
        mainService.registerGetOverrides(uuid -> {
            Optional<UserProfile> profile = Beezig.get().getNetworkManager().getProfilesCache().getNowOrSubmit(uuid, Collections.singletonList(uuid));
            return profile.map(p -> {
                UserOverride overrides = p.getRoleContainer().getOverride();
                if (overrides == null) return null;
                HashMap<String, Object> ret = new HashMap<>();
                overrides.getOverrides().forEach(override -> ret.putAll(override.getAsMap()));
                return ret;
            });
        });
        mainService.loadConfig(Beezig.get().getBeezigDir());
        try {
            mainService.addCommands(new ArrayList<>(CommandManager.commandExecutors));
        } catch(Throwable ex) {
            ExceptionHandler.catchException(ex, "Couldn't load commands");
        }
    }
}
