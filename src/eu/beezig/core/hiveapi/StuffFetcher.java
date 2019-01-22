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

    private static final String BASE_URL = "https://roccodev.pw/beezighosting/files/";

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
