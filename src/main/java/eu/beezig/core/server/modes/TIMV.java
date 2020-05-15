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

package eu.beezig.core.server.modes;

import eu.beezig.core.Beezig;
import eu.beezig.core.advrec.AdvRecUtils;
import eu.beezig.core.config.Settings;
import eu.beezig.core.data.DataPath;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.IAutovote;
import eu.beezig.core.util.CollectionUtils;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.Message;
import eu.beezig.core.util.StringUtils;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.TimvStats;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TIMV extends HiveMode implements IAutovote {

    private List<MapData> maps;
    private MapData currentMapData;

    public TIMV() {
        statsFetcher.setScoreboardTitle("Your TIMV Stats");
        statsFetcher.setApiComputer(name -> {
            TimvStats api = Profiles.timv(name).join();
            GlobalStats stats = new GlobalStats();
            stats.setPoints((int) api.getPoints());
            return stats;
        });
        statsFetcher.setScoreboardComputer(lines -> {
            GlobalStats stats = new GlobalStats();
            stats.setPoints(lines.get("Karma"));
            return stats;
        });
        getAdvancedRecords().setExecutor(this::recordsExecutor);
    }

    private void recordsExecutor() {
        List<Pair<String, String>> messages = getAdvancedRecords().getMessages();
        // Move "Karma" to the top
        Collections.swap(messages, 0, CollectionUtils.indexOf(messages, p -> "Karma".equals(p.getLeft())));
        int rolePts = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Role points")).intValue();
        int karma = Message.getNumberFromFormat(messages.get(0).getRight()).intValue();
        if(Settings.TIMV_ADVREC_VICTORIES.get().getBoolean()) {
           int estimatedVictories = (rolePts - karma / 10) / 20;
           messages.add(new ImmutablePair<>("Victories (est.)", getAdvancedRecords().modifyValue(estimatedVictories)));
           if(Settings.TIMV_ADVREC_KPV.get().getBoolean()) {
               double kpv = karma / (double) estimatedVictories;
               messages.add(new ImmutablePair<>("Karma per Victory (est.)", Message.ratio(kpv)));
           }
        }
        if(Settings.TIMV_ADVREC_KRR.get().getBoolean()) {
            double krr = karma / (double) rolePts;
            messages.add(new ImmutablePair<>("Karma/Role points", Message.ratio(krr)));
        }
        if(Settings.TIMV_ADVREC_TRATIO.get().getBoolean()) {
            int tIndex = CollectionUtils.indexOf(messages, p -> "Traitor Points".equals(p.getLeft()));
            String rawPts = messages.get(tIndex).getRight();
            int tPts = Message.getNumberFromFormat(rawPts).intValue();
            double ratio = tPts * 100D / (double) rolePts;
            messages.set(tIndex, new ImmutablePair<>("Traitor Points",
                    String.format("%s (%s%%%s)", rawPts, (ratio > 30 ? "§c" : "") + Message.ratio(ratio), Color.accent())));
        }
        boolean record = Settings.TIMV_ADVREC_RECORD.get().getBoolean();
        if(AdvRecUtils.needsAPI() || record) {
            AdvRecUtils.announceAPI();
            try {
                TimvStats api = Profiles.timv(getAdvancedRecords().getTarget()).get();
                messages.set(0, new ImmutablePair<>("Karma",
                        messages.get(0).getRight() + AdvRecUtils.getTitle(getTitleService(), api.getTitle(), karma)));
                if(record) messages.add(new ImmutablePair<>("Karma Record", getAdvancedRecords().modifyValue((int) api.getMostPoints())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMap(String map) {
        super.setMap(map);
        String normalized = StringUtils.normalizeMapName(map);
        Optional<MapData> data = maps.stream().filter(m -> normalized.equals(m.map)).findAny();
        if(!data.isPresent()) {
            Message.error("error.map_not_found");
            return;
        }
        currentMapData = data.get();
    }

    public void initMapData() {
        try {
            maps = Beezig.get().getData().getDataList(DataPath.TIMV_MAPS, MapData[].class);
            if(maps == null) {
                Message.error("error.data_read");
                Beezig.logger.error("Tried to fetch maps but file wasn't found.");
            }
        } catch (Exception e) {
            Message.error("error.data_read");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Trouble in Mineville";
    }

    @Override
    public void end() {

    }

    @Override
    public String getIdentifier() {
        return "timv";
    }

    @Override
    public int getMaxVoteOptions() {
        return 6;
    }

    @Override
    public boolean isLastRandom() {
        return true;
    }

    private static class MapData {
        public String map;
        public int enderchests;
    }
}
