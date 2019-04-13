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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Triggers {

    public static final int TYPE_CHAT = 0;
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_SUBTITLE = 2;

    public static final HashSet<Trigger> triggers = new HashSet<>();
    public static String ggText;
    public static int delay;
    public static boolean enabled;
    public static boolean inParty;
    public static long lastPartyJoined;
    static JSONArray disabledModesCache;


    public static boolean shouldGG(String msg, int type) {
        for (Trigger t : triggers) {
            if (!t.isEnabled()) continue;
            if (type != t.getType()) continue;
            if (msg.contains(t.getTrigger())) return true;
        }
        return false;
    }

    public static void changeMode(String mode, boolean enabled) {
        if (enabled) disabledModesCache.remove(mode);
        else disabledModesCache.add(mode);

        for (Trigger t : triggers) {
            if (t.getShortcode().equals(mode)) t.setEnabled(enabled);
        }

    }

    public static void flush() throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("text", ggText);
        obj.put("delay", delay);
        obj.put("disabled", disabledModesCache);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BeezigMain.mcFile + "/autogg.json"))) {
            writer.write(obj.toJSONString());
        }
    }


}
