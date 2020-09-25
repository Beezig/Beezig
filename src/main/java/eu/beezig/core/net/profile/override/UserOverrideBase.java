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

import java.util.Map;

public abstract class UserOverrideBase {
    private String id;
    private String comment;

    public UserOverrideBase loadFromObject(JsonObject overrideObj) {
        if (overrideObj != null) {
            this.id = overrideObj.has("id") ? overrideObj.get("id").getAsString() : null;
            this.comment = overrideObj.has("comment") ? overrideObj.get("comment").getAsString() : null;
        }
        return this;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public abstract Map<String, Object> getAsMap();

    protected final void addBaseToMap(Map<String, Object> map) {
        map.put(String.format("%s.id", id), id);
        map.put(String.format("%s.comment", id), comment);
    }
}
