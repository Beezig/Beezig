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

package eu.beezig.core.net.profile.override;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserOverrideDeserializer implements JsonDeserializer<UserOverride> {
    @Override
    public UserOverride deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsObj = json.getAsJsonObject();

        JsonArray overrides = jsObj.getAsJsonArray("overrides");
        JsonObject overrideObj;
        List<UserOverrideBase> overrideList = new ArrayList<>(5);
        for (JsonElement overrideElem : overrides) {
            overrideObj = overrideElem.getAsJsonObject();
            String id = overrideObj.get("id").getAsString();
            if (id.equals("custom-badge")) {
                overrideList.add(new CustomBadgeOverride().loadFromObject (overrideObj));
            } else if (id.equals("steve-mode")) {
                overrideList.add(new SteveModeOverride().loadFromObject(overrideObj));
            }
        }
        return new UserOverride(overrideList);
    }
}
