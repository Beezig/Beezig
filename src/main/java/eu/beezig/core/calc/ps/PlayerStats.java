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

package eu.beezig.core.calc.ps;

import eu.beezig.hiveapi.wrapper.player.Profiles;

import java.util.HashMap;

class PlayerStats {
    static HashMap<String, PlayerStatsMode> modes = new HashMap<>();

    static {
        PlayerStatsMode basicPvP = new PlayerStatsMode(new HashMap<String, String>() {{
            put("points", "total_points");
            put("kills", "kills");
            put("deaths", "deaths");
            put("victories", "victories");
            put("played", "games_played");
            put("kd", "placeholder");
            put("wl", "placeholder");
        }});
        modes.put("bed", new PlayerStatsMode(Profiles::bed, basicPvP, new HashMap<String, String>() {{
            put("winstreak", "win_streak");
            put("beds", "beds_destroyed");
            put("teams", "teams_eliminated");
        }}));
    }
}
