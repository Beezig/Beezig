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

package eu.beezig.core.hiveapi.wrapper;

import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

public class APIUtils {

    public static String getUUID(String ign) {
        if (ign.length() == 32) return ign;
        // ^ input is already a uuid
        JSONParser parser = new JSONParser();
        JSONObject o;
        try {
            o = (JSONObject) parser.parse(Parser.read(Parser.mojang(ign)));
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed getUUID (Mojang) - " + ign);
            e.printStackTrace();
            return "\\invalid";
        }
        return (String) o.get("id");
    }

    public static JSONObject getObject(Reader toParse) {
        JSONParser parser = new JSONParser();
        JSONObject o = null;
        try {
            o = (JSONObject) parser.parse(toParse);
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed");
            //e.printStackTrace();
        }
        return o;
    }

    public static JSONArray getArray(Reader toParse) {
        JSONParser parser = new JSONParser();
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(toParse);
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed");
            //e.printStackTrace();
        }
        return a;
    }

    public static <T extends Comparable<T>> void concurrentSort(final List<T> key, List<?>... lists) {

        //
        //
        //	Source: https://ideone.com/cXdw6T
        //
        //

        // Do validation
        if (key == null || lists == null)
            throw new NullPointerException("key cannot be null.");

        for (List<?> list : lists)
            if (list.size() != key.size())
                throw new IllegalArgumentException("all lists must be the same size");

        // Lists are size 0 or 1, nothing to sort
        if (key.size() < 2)
            return;

        // Create a List of indices
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < key.size(); i++)
            indices.add(i);

        // Sort the indices list based on the key
        indices.sort(Comparator.comparing(key::get));

        Map<Integer, Integer> swapMap = new HashMap<>(indices.size());
        List<Integer> swapFrom = new ArrayList<>(indices.size()),
                swapTo = new ArrayList<>(indices.size());

        // create a mapping that allows sorting of the List by N swaps.
        for (int i = 0; i < key.size(); i++) {
            int k = indices.get(i);
            while (i != k && swapMap.containsKey(k))
                k = swapMap.get(k);

            swapFrom.add(i);
            swapTo.add(k);
            swapMap.put(i, k);
        }

        // use the swap order to sort each list by swapping elements
        for (List<?> list : lists)
            for (int i = 0; i < list.size(); i++)
                Collections.swap(list, swapFrom.get(i), swapTo.get(i));
    }

    public static String getTimeAgo(long time) {

        /*
         * Copyright 2012 Google Inc.
         *
         * Licensed under the Apache License, Version 2.0 (the "License");
         * you may not use this file except in compliance with the License.
         * You may obtain a copy of the License at
         *
         *      http://www.apache.org/licenses/LICENSE-2.0
         *
         * Unless required by applicable law or agreed to in writing, software
         * distributed under the License is distributed on an "AS IS" BASIS,
         * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         * See the License for the specific language governing permissions and
         * limitations under the License.
         */

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;
        long MONTH_MILLIS = 30L * DAY_MILLIS;
        long YEAR_MILLIS = 12L * MONTH_MILLIS;
        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "A minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "An hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48L * HOUR_MILLIS) {
            return "Yesterday";
        } else if (diff < 29L * DAY_MILLIS) {
            return diff / DAY_MILLIS + " days ago";
        } else if (diff < 2L * MONTH_MILLIS) {
            return "1 month ago";
        } else if (diff < 11L * MONTH_MILLIS) {
            return diff / MONTH_MILLIS + " months ago";
        } else if (diff < 2L * YEAR_MILLIS) {
            return "1 year ago";
        } else {
            return diff / YEAR_MILLIS + " years ago";
        }
    }

    public static String getTimePassed(long time) {


        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        if (time < 0) {
            // HiveAPI error
            return "Error";
        }


        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;
        long MONTH_MILLIS = 30L * DAY_MILLIS;

        if (time < MINUTE_MILLIS) {
            return "Less a minute";
        } else if (time < 2 * MINUTE_MILLIS) {
            return "A minute";
        } else if (time < 50 * MINUTE_MILLIS) {
            return time / MINUTE_MILLIS + " minutes";
        } else if (time < 90 * MINUTE_MILLIS) {
            return "An hour";
        } else if (time < 24 * HOUR_MILLIS) {
            return time / HOUR_MILLIS + " hours";
        } else if (time < 48L * HOUR_MILLIS) {
            return "A day";
        } else if (time < 29L * DAY_MILLIS) {
            return time / DAY_MILLIS + " days";
        } else if (time < 2L * MONTH_MILLIS) {
            return "1 month";
        } else {
            return time / MONTH_MILLIS + " months";
        }
    }

    public static String capitalize(String sentence) {

        //Thanks to "Samet öztoprak" on stackoverflow :^)

        String words[] = sentence.replaceAll("\\s+", " ").trim().split(" ");
        StringBuilder newSentence = new StringBuilder();
        for (String word : words) {
            for (int i = 0; i < word.length(); i++)
                newSentence.append((i == 0) ? word.substring(i, i + 1).toUpperCase() :
                        (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase() : word.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
        }

        return newSentence.toString().trim();
    }


    public static double average(Object[] arr) {

        double sum = 0L;

        for (Object anArr : arr) {
            sum += Double.valueOf(String.valueOf(anArr));
        }

        return sum / arr.length;
    }

    public static ChatColor getLevelColorHIDE(int level) {
        if (level < 5) return ChatColor.GRAY;
        if (level < 10) return ChatColor.AQUA;
        if (level < 15) return ChatColor.GREEN;
        if (level < 20) return ChatColor.YELLOW;
        if (level < 25) return ChatColor.GOLD;
        if (level < 30) return ChatColor.RED;
        if (level < 35) return ChatColor.LIGHT_PURPLE;
        if (level < 40) return ChatColor.AQUA;
        if (level < 45) return ChatColor.DARK_GREEN;
        if (level < 50) return ChatColor.DARK_PURPLE;

        if (level == 50) return ChatColor.DARK_BLUE;

        return ChatColor.WHITE;
    }

    public static String getNextPecentHIDE(int exp, int level) {
        if (level == 30) return "";
        //exp = 600
        //level = 4
        exp = exp - 50;

        int prevXP = 0;
        int i = 1;
        while (i < level) {
            prevXP = 50 * i + prevXP;
            i++;
        }
        int nextXP = 50 * i + prevXP;
        int diff = nextXP - prevXP;
        exp = exp - prevXP;

        double percent = Math.floor(((double) exp / (double) diff) * 100d);

        return " +" + ((int) percent) + "%";
    }

    public static Reader readURL(URL url) {
        return Parser.read(url);
    }

    public static URL speedrunPublic(String id, int mode) {
        return Parser.speedrun(id, mode);
    }


    static class Parser {


        public static URL mojang(String ign) {
            String url = "https://api.mojang.com/users/profiles/minecraft/@ign@";
            try {
                return new URL(url.replaceAll("@ign@", ign));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        public static URL game(String uuid, String game) {

            String url = "http://api.hivemc.com/v1/player/@player@/" + game;
            if (game.isEmpty()) url = "http://api.hivemc.com/v1/player/@player@";
            try {
                return new URL(url.replaceAll("@player@", uuid));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return null;
            }
        }

        /**
         * Parses url from speedrun.com
         *
         * @param param mapId for mode 0, or userId for mode 1
         * @param mode  0 for map parsing, 1 for user parsing
         * @return the parsed URL
         */
        public static URL speedrun(String param, int mode) {
            String url = "";
            switch (mode) {
                case 0:
                    url = "https://www.speedrun.com/api/v1/leaderboards/369ep8dl/level/@id@/824xzvmd?top=1";
                    break;
                case 1:
                    url = "https://www.speedrun.com/api/v1/users/@id@";
            }
            try {
                return new URL(url.replaceAll("@id@", param));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        public static URL hiveLB(int index, String game) {
            String url = "http://api.hivemc.com/v1/game/@game@/leaderboard/" + index + "/" + (index + 1);
            try {
                return new URL(url.replaceAll("@game@", game));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        public static URL monthlyLB(String game) {
            String url = "https://thtmx.rocks/@game@/api/ighGH789fdf5kfHUo";
            try {
                return new URL(url.replaceAll("@game@", game));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        public static Reader read(URL url) {
            BufferedReader reader = null;
            try {
                URLConnection conn = url.openConnection();
                conn.addRequestProperty("User-Agent", Log.getUserAgent());
                conn.setRequestProperty("Accept", "application/json");

                return new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));

            } catch (Exception e) {

                //e.printStackTrace();
                return null;
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }

		/* LEGACY
		 			
	
	
	//TIMV
	public static void TIMVupdateKarma() throws ParseException, Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(TIMVparsePlayerURL(playername)));
		
		TIMVkarma =  (long) o.get("total_points");
		
		
	}
		 
		 
		 
		 */
    }

}
