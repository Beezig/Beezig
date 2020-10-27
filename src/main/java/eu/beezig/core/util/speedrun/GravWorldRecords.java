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

package eu.beezig.core.util.speedrun;

import com.google.gson.reflect.TypeToken;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GravWorldRecords {
    public static CompletableFuture<WorldRecord> getRecord(String mapName) {
        String map = mapName.toUpperCase(Locale.ROOT).replace(" ", "_");
        return getRecords().thenApplyAsync(m -> m.get(map));
    }

    public static CompletableFuture<Map<String, WorldRecord>> getRecords() {
        String url = (Beezig.DEBUG ? "http://localhost:8726" : "https://web.beezig.eu") + "/v1/proxy/gravity";
        try {
            return Downloader.getJsonObject(new URL(url))
                .thenApplyAsync(o -> Beezig.gson.fromJson(o.getInput().toJSONString(), new TypeToken<HashMap<String, WorldRecord>>(){}.getType()));
        } catch (MalformedURLException e) {
            ExceptionHandler.catchException(e);
            return null;
        }
    }

    public static class WorldRecord {
        private String name;
        private String time;
        private String map;
        private transient String display;

        public String getDisplay() {
            if(display != null) return display;
            return display = String.format("%s (%s)", getTimeDisplay(), name);
        }

        public String getTimeDisplay() {
            return DurationFormatUtils.formatDuration(getMillis(), "m':'ss.SSS");
        }

        public long getMillis() {
            return Long.parseLong(time, 10);
        }

        public String getName() {
            return name;
        }

        public String getMap() {
            return map;
        }
    }
}
