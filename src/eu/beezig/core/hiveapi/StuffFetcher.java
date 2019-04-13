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

package eu.beezig.core.hiveapi;

import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.beezig.core.hiveapi.stuff.timv.TIMVMap;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class StuffFetcher {

    private static final String BASE_URL = "https://rocco.dev/beezighosting/files/";

    public static HashMap<String, DRMap> getDeathRunMaps() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(APIUtils.readURL(new URL(BASE_URL + "dr.json")));
            HashMap<String, DRMap> tr = new HashMap<>();
            obj.forEach((k, v) -> {

                String key = (String) k;
                JSONObject value = (JSONObject) v;

                DRMap map = new DRMap((long) value.get("checkpoints"), (String) value.get("speedrun"), key, (String) value.get("api"));

                tr.put(key.toLowerCase(), map);
            });

            return tr;
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, TIMVMap> getTroubleInMinevilleMaps() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(APIUtils.readURL(new URL(BASE_URL + "timv.json")));
            HashMap<String, TIMVMap> tr = new HashMap<>();
            obj.forEach((k, v) -> {

                String key = (String) k;
                JSONObject value = (JSONObject) v;

                TIMVMap map = new TIMVMap(key, (long) value.get("enderchests"));

                tr.put(key.toLowerCase(), map);
            });

            return tr;
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> getGravityMaps() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(APIUtils.readURL(new URL(BASE_URL + "grav.json")));
            HashMap<String, String> tr = new HashMap<>();
            obj.forEach((k, v) -> tr.put((String) k, (String) v));

            return tr;
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
