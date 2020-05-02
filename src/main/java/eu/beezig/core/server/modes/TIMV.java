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
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.TimvStats;

import java.util.List;

public class TIMV extends HiveMode {

    private List<MapData> maps;

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

    public void initMapData() {
        try {
            maps = Beezig.get().getData().getData(DataPath.TIMV_MAPS);
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
