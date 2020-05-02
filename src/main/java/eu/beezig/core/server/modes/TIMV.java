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
import eu.beezig.core.data.DataPath;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.util.Message;
import eu.beezig.core.util.StringUtils;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.TimvStats;

import java.util.List;
import java.util.Optional;

public class TIMV extends HiveMode {

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

    private static class MapData {
        public String map;
        public int enderchests;
    }
}
