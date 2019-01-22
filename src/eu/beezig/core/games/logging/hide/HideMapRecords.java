package eu.beezig.core.games.logging.hide;

import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.hiveapi.wrapper.APIUtils;

import java.io.*;

public class HideMapRecords {

    private static File recordsFile;

    public static void endGame(String map, long kills) {
        if(map == null) return;
        String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
        JSONObject current = read();
        if(current == null) current = new JSONObject();
        JSONObject forAccount = current.containsKey(uuid) ? (JSONObject) current.get(uuid) : new JSONObject();

        if(forAccount.containsKey(map)) {
            long currentKills = (long) forAccount.get(map);
            if(kills > currentKills) forAccount.put(map, kills);
            else return;
        }
        else forAccount.put(map, kills);

        current.put(uuid, forAccount);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(recordsFile))) {
            writer.write(current.toJSONString());
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static long getForMap(String map) {
        if(map == null) return 0;
        String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
        JSONObject current = read();
        if(current == null) return 0;
        if(current.containsKey(uuid)) {
            JSONObject forAccount = (JSONObject) current.get(uuid);
            if(forAccount.containsKey(map))
                return (long) forAccount.get(map);
            else return 0;
        }
        else return 0;
    }

    private static JSONObject read() {
        try(BufferedReader reader = new BufferedReader(new FileReader(recordsFile))) {
            return APIUtils.getObject(reader);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void init() {
        recordsFile = new File(BeezigMain.mcFile + "/hide/records.json");
        if(!recordsFile.exists()) {
            try {
                recordsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
