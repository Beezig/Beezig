package tk.roccodev.beezig.utils.autogg;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;

public class TriggersFetcher {

    private static final String FETCH_URL = "https://roccodev.pw/beezighosting/autogg/triggers.json";

    public static void fetch() throws IOException {
        URL url = new URL(FETCH_URL);
        JSONArray arr = APIUtils.getArray(APIUtils.readURL(url));
        JSONArray disabled = new JSONArray();
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
