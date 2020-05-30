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

package eu.beezig.core.calc.ps;

import com.google.common.collect.ImmutableMap;
import eu.beezig.hiveapi.wrapper.player.Profiles;

import java.util.HashMap;

class PlayerStats {
    HashMap<String, PlayerStatsMode> modes = new HashMap<>();

    PlayerStats() {
        PlayerStatsMode basicPvP = new PlayerStatsMode(new HashMap<>(ImmutableMap.<String, String>builder()
                .put("points", "total_points")
                .put("kills", "kills")
                .put("deaths", "deaths")
                .put("victories", "victories")
                .put("played", "games_played")
                .put("kd", "placeholder")
                .put("wl", "placeholder")
                .build()));
        modes.put("bed", new PlayerStatsMode(Profiles::bed, basicPvP, new HashMap<>(ImmutableMap.of(
                "winstreak", "win_streak",
                "beds", "beds_destroyed",
                "teams", "teams_eliminated"
        ))));
        modes.put("sky", new PlayerStatsMode(Profiles::sky, basicPvP, new HashMap<>()));
        PlayerStatsMode sgn;
        modes.put("sgn", sgn = new PlayerStatsMode(Profiles::sgn, basicPvP, new HashMap<>()));
        modes.put("timv", new PlayerStatsMode(Profiles::timv, new HashMap<>(ImmutableMap.<String, String>builder()
                .put("karma", "total_points")
                .put("points", "total_points")
                .put("ipoints", "i_points")
                .put("dpoints", "d_points")
                .put("tpoints", "t_points")
                .put("rolepoints", "role_points")
                .put("record", "most_points")
                .build())));
        modes.put("dr", new PlayerStatsMode(Profiles::dr, basicPvP, new HashMap<>(ImmutableMap.of(
            "checkpoints", "totalcheckpoints",
            "rplayed", "runnergamesplayed",
            "dplayed", "deathgamesplayed",
            "rwins", "runnerwins",
            "dwins", "deathwins"
        ))));
        modes.put("hide", new PlayerStatsMode(Profiles::hide, basicPvP, new HashMap<>(ImmutableMap.of(
                "kills", "seekerkills",
                "played", "gamesplayed"
        ))));
        modes.put("bp", new PlayerStatsMode(Profiles::bp, new HashMap<>(ImmutableMap.<String, String>builder()
                .put("points", "total_points")
                .put("victories", "victories")
                .put("placings", "total_placing")
                .put("eliminations", "total_eliminations")
                .put("played", "games_played")
                .put("wl", "placeholder")
                .build())));
        modes.put("grav", new PlayerStatsMode(Profiles::grav, new HashMap<>(ImmutableMap.of(
            "points", "points",
            "victories", "victories",
            "played", "gamesplayed",
            "wl", "placeholder"
        ))));
        modes.put("lab", new PlayerStatsMode(Profiles::lab, new HashMap<>(ImmutableMap.of(
            "points", "total_points",
            "victories", "victories",
            "played", "gamesplayed",
            "wl", "placeholder"
        ))));
        modes.put("sg", new PlayerStatsMode(Profiles::sg, sgn, new HashMap<>(ImmutableMap.of(
            "played", "gamesplayed"
        ))));
    }
}
