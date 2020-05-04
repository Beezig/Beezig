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

import eu.beezig.core.util.UUIDUtils;
import eu.beezig.hiveapi.wrapper.player.GameStats;
import eu.beezig.hiveapi.wrapper.utils.json.JObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class PlayerStatsMode {
    private HashMap<String, String> apiTable;
    private Function<String, CompletableFuture<? extends GameStats>> producer;

    PlayerStatsMode(HashMap<String, String> apiTable) {
        this.apiTable = apiTable;
    }

    public PlayerStatsMode(Function<String, CompletableFuture<? extends GameStats>> producer,
                           PlayerStatsMode from, HashMap<String, String> extra) {
        // The reason we first assign our table to a clone of the template one and not the extra one is
        // because it allows the manager to overwrite values, as they get put last.
        this.apiTable = (HashMap<String, String>) from.apiTable.clone();
        this.apiTable.putAll(extra);
        this.producer = producer;
    }

    String getApiKey(String commonName) {
        return apiTable.get(commonName.toLowerCase(Locale.ROOT));
    }

    public CompletableFuture<? extends GameStats> get(UUID uuid) {
        return producer.apply(UUIDUtils.strip(uuid));
    }

    PlayerStatsProfile getProfile(HashMap<String, String> displayNames, GameStats source, String key, String stat) {
        Number value = null;
        JObject src = source.getSource();
        if("kd".equalsIgnoreCase(stat)) {
            value = src.getLong(getApiKey("kills")) / (double) src.getLong(getApiKey("deaths"));
        }
        else if("wl".equalsIgnoreCase(stat)) {
            value = src.getLong(getApiKey("victories")) * 100D / (double) src.getLong(getApiKey("played"));
        }
        if(value == null) value = source.getSource().getInt(key);
        return new PlayerStatsProfile(displayNames.get(source.getUUID()), value);
    }
}
