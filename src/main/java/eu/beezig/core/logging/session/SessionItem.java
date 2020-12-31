/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.logging.session;

import java.util.HashMap;
import java.util.Map;

public class SessionItem {
    private String mode, map;
    private int points, kills, deaths;
    private boolean victory;
    private Map<String, String> custom;
    private long gameStart, gameEnd;

    private SessionItem() {}

    public String getMode() {
        return mode;
    }

    public String getMap() {
        return map;
    }

    public int getPoints() {
        return points;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public boolean isVictory() {
        return victory;
    }

    public long getGameStart() {
        return gameStart;
    }

    public long getGameEnd() {
        return gameEnd;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public static class Builder {
        private String mode, map;
        private int points = -1, kills = -1, deaths = -1;
        private boolean victory;
        private Map<String, String> custom;
        private long gameStart;

        public Builder(String mode) {
            this.mode = mode;
        }

        public Builder map(String map) {
            this.map = map;
            return this;
        }

        public Builder points(int points) {
            this.points = points;
            return this;
        }

        public Builder kills(int kills) {
            this.kills = kills;
            return this;
        }

        public Builder deaths(int deaths) {
            this.deaths = deaths;
            return this;
        }

        public Builder victory(boolean victory) {
            this.victory = victory;
            return this;
        }

        public Builder custom(String key, String value) {
            if(this.custom == null) this.custom = new HashMap<>();
            this.custom.put(key, value);
            return this;
        }

        public Builder gameStart(long start) {
            this.gameStart = start;
            return this;
        }

        public SessionItem build() {
            SessionItem item = new SessionItem();
            item.points = points;
            item.kills = kills;
            item.deaths = deaths;
            item.map = map;
            item.mode = mode;
            item.victory = victory;
            item.custom = custom;
            item.gameStart = gameStart;
            item.gameEnd = System.currentTimeMillis();
            return item;
        }
    }
}
