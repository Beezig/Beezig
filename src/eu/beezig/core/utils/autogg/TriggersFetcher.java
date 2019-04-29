/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.utils.autogg;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;

public class TriggersFetcher {

    private static final String FETCH_URL = "https://rocco.dev/beezighosting/autogg/triggers.json";

    public static void fetch() throws IOException {
        URL url = new URL(FETCH_URL);
        JSONArray arr = APIUtils.getArray(APIUtils.readURL(url));
        JSONArray disabled;
        try (BufferedReader reader = new BufferedReader(new FileReader(BeezigMain.mcFile + "/autogg.json"))) {
            JSONObject config = APIUtils.getObject(reader);
            disabled = (JSONArray) config.get("disabled");
            Triggers.delay = Math.toIntExact((long) config.get("delay"));
            Triggers.ggText = (String) config.get("text");
        }
        for (Object o : arr) {
            JSONObject j = (JSONObject) o;
            Trigger t = new Trigger((String) j.get("desc"), (String) j.get("shortcode"), (String) j.get("trigger"), Math.toIntExact((long) j.get("type")));
            if (disabled.contains(t.getShortcode())) t.setEnabled(false);
            else t.setEnabled(true);
            Triggers.triggers.add(t);
        }
        Triggers.disabledModesCache = disabled;

    }

    public static void loadDefaults() throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("text", "gg");
        obj.put("delay", 0);
        obj.put("disabled", new JSONArray());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BeezigMain.mcFile + "/autogg.json"))) {
            writer.write(obj.toJSONString());
        }
    }

    public static boolean shouldLoad() {
        try {
            Class c = Class.forName("tk.roccodev.autogg.GGMod");
            Field f = c.getField("enabled");
            boolean b = f.getBoolean(null);
            return !b;

        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException ignored) {
            return true;
        }
    }


}
