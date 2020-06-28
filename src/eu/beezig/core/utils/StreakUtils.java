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

package eu.beezig.core.utils;

import eu.beezig.core.Log;
import eu.beezig.core.games.*;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StreakUtils {

    private static File winstreakFile;

    public static void init(File rootDir) {
        winstreakFile = new File(rootDir + "/winstreaks.json");
        initValues(BED.class, "bed");
        initValues(SKY.class, "sky");
        initValues(HIDE.class, "hide");
    }


    public static void incrementWinstreakByOne(String mode) {
        new Thread(() -> {
            String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
            try (BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
                JSONObject obj = APIUtils.getObject(reader);
                if (obj == null) obj = new JSONObject();
                JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
                if (acc.containsKey(mode)) {
                    JSONObject j = (JSONObject) acc.get(mode);
                    long streak = (long) j.get("streak") + 1;
                    long best = j.containsKey("bestStreak") ? (long) j.get("bestStreak") : 0;
                    if (streak >= best) j.put("bestStreak", streak);
                    j.put("streak", streak);
                    acc.put(mode, j);

                } else {
                    JSONObject toAdd = new JSONObject();
                    toAdd.put("streak", 1);
                    toAdd.put("bestStreak", 1);
                    acc.put(mode, toAdd);
                }
                obj.put(uuid, acc);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(winstreakFile))) {
                    writer.write(obj.toJSONString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void initValues(Class c, String mode) {
        try (BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
            String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
            JSONObject obj = APIUtils.getObject(reader);
            if (obj == null) obj = new JSONObject();
            JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
            if (acc.containsKey(mode)) {
                JSONObject j = (JSONObject) acc.get(mode);
                try {
                    c.getField("winstreak").set(null, j.containsKey("streak") ? Math.toIntExact((long) j.get("streak")) : 0);
                    c.getField("bestStreak").set(null, j.containsKey("bestStreak") ? Math.toIntExact((long) j.get("bestStreak")) : c.getField("winstreak").get(null));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }


            } else {
                try {
                    c.getField("winstreak").set(null, 0);
                    c.getField("bestStreak").set(null, 0);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMessageForCommand(String mode) {
        String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
        try (BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
            JSONObject obj = APIUtils.getObject(reader);
            if (obj == null) return Log.error + "No winstreak data found for the specified mode.";
            if (!obj.containsKey(uuid))
                return Log.error + "No winstreak data found for the specified mode for the current player.";
            JSONObject acc = (JSONObject) obj.get(uuid);
            if (acc.containsKey(mode)) {
                JSONObject j = (JSONObject) acc.get(mode);

                long streak = j.containsKey("streak") ? (long) j.get("streak") : 0L;
                long best = j.containsKey("bestStreak") ? (long) j.get("bestStreak") : 0L;

                long lastReset = j.containsKey("lastReset") ? (long) j.get("lastReset") : 0L;
                long bestReset = j.containsKey("bestReset") ? (long) j.get("bestReset") : 0L;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                String lrDate = lastReset == 0 ? "Never" : sdf.format(new Date(lastReset));
                String brDate = bestReset == 0 ? "Never" : sdf.format(new Date(bestReset));

                String bar = "    §7§m                                                                                    ";

                return bar + '\n' + '\n' +
                        Log.info + "§3Current Streak: §b" + streak + '\n' +
                        Log.info + "§3Best Streak: §b" + best + '\n' +
                        '\n' +
                        Log.info + "§3Last Reset: §b" + lrDate + '\n' +
                        Log.info + "§3Best Reset: §b" + brDate + '\n' +
                        '\n' + bar;


            } else return Log.error + "No winstreak data found for the specified mode.";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Log.error + "Unexpected error.";
    }


    public static void resetWinstreak(String mode, boolean wasBest) {
        new Thread(() -> {
            String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
            try (BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
                JSONObject obj = APIUtils.getObject(reader);
                if (obj == null) obj = new JSONObject();
                JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
                if (acc.containsKey(mode)) {
                    JSONObject j = (JSONObject) acc.get(mode);
                    j.put("streak", 0);
                    j.put("lastReset", System.currentTimeMillis());
                    if (wasBest) j.put("bestReset", System.currentTimeMillis());
                    acc.put(mode, j);
                } else {
                    JSONObject toAdd = new JSONObject();
                    toAdd.put("streak", 0);
                    toAdd.put("lastReset", System.currentTimeMillis());
                    toAdd.put("bestReset", System.currentTimeMillis());
                    acc.put(mode, toAdd);
                }
                obj.put(uuid, acc);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(winstreakFile))) {
                    writer.write(obj.toJSONString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


}
