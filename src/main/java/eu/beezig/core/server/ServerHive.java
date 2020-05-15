/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.server;

import eu.beezig.core.Beezig;
import eu.beezig.core.server.listeners.BEDListener;
import eu.beezig.core.server.listeners.TIMVListener;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.player.HivePlayer;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.the5zig.mod.server.*;

import java.util.Locale;

public class ServerHive extends ServerInstance {

    private long tokens;
    private HivePlayer profile;

    public long getTokens() {
        return tokens;
    }

    public HivePlayer getProfile() {
        return profile;
    }

    @Override
    public void registerListeners() {
        GameListenerRegistry registry = getGameListener();
        registry.registerListener(new ListenerHive());
        registry.registerListener(new TIMVListener());
        registry.registerListener(new BEDListener());
    }

    @Override
    public String getName() {
        return "The Hive";
    }

    @Override
    public String getConfigName() {
        return "hive";
    }

    private void onServerJoin() {
        Profiles.global(UUIDUtils.strip(Beezig.user().getId())).thenAcceptAsync(profile -> {
            this.profile = profile;
            this.tokens = profile.getTokens();
            Beezig.logger.info(String.format("Loaded profile for current user. Data as of %s", Message.date(profile.getCachedAt())));
        }).exceptionally(e -> {
            Message.error(Message.translate("error.token_fetch"));
            Beezig.logger.error(e);
            return null;
        });
    }

    @Override
    public boolean handleServer(String host, int port) {
        if(host.toLowerCase(Locale.ROOT).contains("hivemc.") || host.toLowerCase(Locale.ROOT).endsWith("hive.sexy")
                || host.toLowerCase(Locale.ROOT).contains(".j2o")) {
            onServerJoin();
            return true;
        }
        return false;
    }

    public static boolean isCurrent() {
        return Beezig.api().getActiveServer() instanceof ServerHive;
    }

    private class ListenerHive extends AbstractGameListener<GameMode> {
        @Override
        public Class<GameMode> getGameMode() {
            return null;
        }

        @Override
        public boolean matchLobby(String s) {
            return false;
        }

        @Override
        public void onMatch(GameMode gameMode, String key, IPatternResult match) {
            if(key == null) return;
            Beezig.logger.debug(String.format("[ServerHive] Matched key %s, %d groups", key, match.size()));

            if(key.startsWith("join.")) getGameListener().switchLobby(key.replace("join.", ""));
            else if("tokens".equals(key)) ServerHive.this.tokens = Integer.parseInt(match.get(1), 10);
            else if("tokens.boost".equals(key)) ServerHive.this.tokens += Integer.parseInt(match.get(0), 10);
            else if("map".equals(key) && gameMode instanceof HiveMode) ((HiveMode)gameMode).setMap(match.get(0));
            else if("autovote.map".equals(key) && gameMode instanceof HiveMode) ((HiveMode)gameMode).getAutovoteManager().parse(match);
            // Advanced Records
            if(gameMode instanceof HiveMode) match.ignoreMessage(((HiveMode) gameMode).getAdvancedRecords().parseMessage(key, match));
        }

        @Override
        public void onServerConnect(GameMode gameMode) {
            if(gameMode instanceof HiveMode) {
                ((HiveMode)gameMode).end();
            }
            getGameListener().switchLobby(null);
        }

        @Override
        public void onTick(GameMode gameMode) {
            if(gameMode instanceof HiveMode) {
                HiveMode mode = (HiveMode) gameMode;
                mode.getStatsFetcher().attemptCompute(mode, Beezig.api().getSideScoreboard());
            }
        }
    }
}
