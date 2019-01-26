package eu.beezig.core.utils.tutorial;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import org.json.simple.JSONObject;
import pw.roccodev.beezig.hiveapi.wrapper.utils.json.LazyObject;

import java.io.*;
import java.net.URL;

public class TutorialManager {

    public static JSONObject progress;
    public static JSONObject remote;
    private static File progressFile;
    private static boolean completed = false;

    public static void init() {
        progressFile = new File(BeezigMain.mcFile + "/tutorial.json");
        try (BufferedReader reader = new BufferedReader(new FileReader(progressFile))) {
            boolean hasChanged = false;
            JSONObject json = APIUtils.getObject(reader);
            if (json == null) {
                json = new JSONObject();
                hasChanged = true;
            }

            if (!json.containsKey("completed")) {
                json.put("completed", false);
                hasChanged = true;
            }

            completed = (boolean) json.get("completed");

            if (hasChanged) {
                save(json);
            }

            if (!completed) {
                LazyObject obj = new LazyObject(null, new URL("https://roccodev.pw/beezighosting/tutorial.json"));
                obj.fetch();
                remote = obj.getInput();
            }

            progress = json;


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static boolean shouldOperate() {
        return !completed && remote != null;
    }

    public static void save(JSONObject obj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(progressFile))) {
            writer.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
