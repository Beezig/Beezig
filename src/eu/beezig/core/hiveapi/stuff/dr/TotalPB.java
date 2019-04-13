/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.hiveapi.stuff.dr;

import eu.beezig.core.games.DR;

import java.util.Map;

public class TotalPB {

    public static String getTotalPB(Map<String, Long> mapRecords) {
        try {

            if (mapRecords.size() != DR.mapsPool.size()) return null;
            //Doesn't have a time on every map

            long time = 0;

            for (Object x : mapRecords.values()) {
                time += (long) x;
            }

            int seconds = Math.toIntExact(time) % 60;
            int minutes = Math.floorDiv(Math.toIntExact(time), 60);
            if (seconds < 10) {
                return (minutes + ":0" + seconds);
            }
            return (minutes + ":" + seconds);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


}
