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
import eu.the5zig.util.minecraft.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class SteveModeOverride extends UserOverrideBase {
    private String text;
    private boolean enabled;

    @Override
    public UserOverrideBase loadFromObject(JsonObject overrideObj) {
        if (overrideObj != null) {
            this.text = overrideObj.has("text") ? overrideObj.get("text").getAsString() : ChatColor.BOLD + "=D ?";
            this.enabled = !overrideObj.has("enabled") || overrideObj.get("enabled").getAsBoolean();
        }
        return super.loadFromObject(overrideObj);
    }

    @Override
    public Map<String, Object> getAsMap() {
        Map<String, Object> ret = new HashMap<>();
        ret.put(String.format("%s.text", getId()), text);
        ret.put(String.format("%s.enabled", getId()), enabled);
        super.addBaseToMap(ret);
        return ret;
    }

    public String getText() {
        return text;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
