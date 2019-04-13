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

package eu.beezig.core.games.logging.hide;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

import java.io.*;

public class HideMapRecords {

    private static File recordsFile;

    public static void endGame(String map, long kills) {
        if (map == null) return;
        String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
        JSONObject current = read();
        if (current == null) current = new JSONObject();
        JSONObject forAccount = current.containsKey(uuid) ? (JSONObject) current.get(uuid) : new JSONObject();

        if (forAccount.containsKey(map)) {
            long currentKills = (long) forAccount.get(map);
            if (kills > currentKills) forAccount.put(map, kills);
            else return;
        } else forAccount.put(map, kills);

        current.put(uuid, forAccount);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(recordsFile))) {
            writer.write(current.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static long getForMap(String map) {
        if (map == null) return 0;
        String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
        JSONObject current = read();
        if (current == null) return 0;
        if (current.containsKey(uuid)) {
            JSONObject forAccount = (JSONObject) current.get(uuid);
            if (forAccount.containsKey(map))
                return (long) forAccount.get(map);
            else return 0;
        } else return 0;
    }

    private static JSONObject read() {
        try (BufferedReader reader = new BufferedReader(new FileReader(recordsFile))) {
            return APIUtils.getObject(reader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void init() {
        recordsFile = new File(BeezigMain.mcFile + "/hide/records.json");
        if (!recordsFile.exists()) {
            try {
                recordsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
