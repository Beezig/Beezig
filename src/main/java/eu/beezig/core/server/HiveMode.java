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

import eu.the5zig.mod.server.GameMode;

public abstract class HiveMode extends GameMode {
    private int points;
    private String map;
    private GlobalStats global;

    public HiveMode() {
        global = new GlobalStats();
    }

    public abstract void end();

    public int getPoints() {
        return points;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void addPoints(int points) {
        this.points += points;
        if(global.points != null) global.points += points;
    }

    public void addKills(int kills) {
        this.kills += kills;
        if(global.kills != null) global.kills += kills;
    }

    public void addDeaths(int deaths) {
        this.deaths += deaths;
        if(global.deaths != null) global.deaths += deaths;
    }

    public GlobalStats getGlobal() {
        return global;
    }

    public static class GlobalStats {
        private Integer points;
        private Integer kills;
        private Integer deaths;
        private Integer victories;
        private Integer played;

        public Integer getPlayed() {
            return played;
        }

        public Integer getPoints() {
            return points;
        }

        public Integer getKills() {
            return kills;
        }

        public Integer getDeaths() {
            return deaths;
        }

        public Integer getVictories() {
            return victories;
        }
    }
}
