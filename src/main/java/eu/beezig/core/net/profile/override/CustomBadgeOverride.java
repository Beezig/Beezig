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

package eu.beezig.core.net.profile.override;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CustomBadgeOverride extends UserOverrideBase {
    private int priority;
    private String url;

    @Override
    public UserOverrideBase loadFromObject(JsonObject overrideObj) {
        if (overrideObj != null) {
            // If no badge priority is provided, just fall back to priority 25 (above 50% of the standard badges)
            this.priority = overrideObj.has("priority") ? overrideObj.get("priority").getAsInt() : 25;
            // If no url is provided, fall back to "Badge 1" (currently, the standard badge)
            this.url = overrideObj.has("url") ? overrideObj.get("url").getAsString() : "https://go.beezig.eu/badges/1.png";
        }
        return super.loadFromObject(overrideObj);
    }

    @Override
    public Map<String, Object> getAsMap() {
        HashMap<String, Object> ret = new HashMap<>();
        ret.put(String.format("%s.priority", getId()), priority);
        ret.put(String.format("%s.url", getId()), url);
        addBaseToMap(ret);
        return ret;
    }
}
