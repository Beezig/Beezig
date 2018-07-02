package tk.roccodev.beezig.utils;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.games.CAI;
import tk.roccodev.beezig.games.SKY;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;

import java.io.*;
import java.util.Calendar;

public class StreakUtils {

    private static File winstreakFile;

    public static void init(File rootDir){
        winstreakFile = new File(rootDir + "/winstreaks.json");
        initValues(CAI.class, "cai");
        initValues(BED.class, "bed");
        initValues(SKY.class, "sky");
    }


  public static void incrementWinstreakByOne(String mode){
        new Thread(() -> {
            String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
            try(BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
                JSONObject obj = APIUtils.getObject(reader);
                if(obj == null) obj = new JSONObject();
                JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
                if (acc.containsKey(mode)) {
                    JSONObject j = (JSONObject) acc.get(mode);
                    long streak = (long) j.get("streak") + 1;
                    long best = j.containsKey("bestStreak") ? (long) j.get("bestStreak") : 0;
                    if(streak >= best) j.put("bestStreak", streak);
                    j.put("streak", streak);
                    acc.put(mode, j);

                }
                else {
                    JSONObject toAdd = new JSONObject();
                    toAdd.put("streak", 1);
                    toAdd.put("bestStreak", 1);
                    acc.put(mode, toAdd);
                }
                obj.put(uuid, acc);

                try(BufferedWriter writer = new BufferedWriter(new FileWriter(winstreakFile))) {
                    writer.write(obj.toJSONString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

  }

  public static void initValues(Class c, String mode) {
      try(BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
          String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
          JSONObject obj = APIUtils.getObject(reader);
          if(obj == null) obj = new JSONObject();
          JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
          if (acc.containsKey(mode)) {
              JSONObject j = (JSONObject) acc.get(mode);
              try {
                  c.getField("winstreak").set(null, j.containsKey("streak") ? Math.toIntExact((long) j.get("streak")) : 0);
                  c.getField("bestStreak").set(null, j.containsKey("bestStreak") ? Math.toIntExact((long) j.get("bestStreak")) : c.getField("winstreak").get(null));
              } catch (IllegalAccessException e) {
                  e.printStackTrace();
              } catch (NoSuchFieldException e) {
                  e.printStackTrace();
              }


          }
          else {
              try {
                  c.getField("winstreak").set(null, 0);
                  c.getField("bestStreak").set(null, 0);
              } catch (IllegalAccessException e) {
                  e.printStackTrace();
              } catch (NoSuchFieldException e) {
                  e.printStackTrace();
              }
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
  }


  public static void resetWinstreak(String mode) {
      new Thread(() -> {
          String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
          try(BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
              JSONObject obj = APIUtils.getObject(reader);
              if(obj == null) obj = new JSONObject();
              JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
              if (acc.containsKey(mode)) {
                  JSONObject j = (JSONObject) acc.get(mode);
                  j.put("streak", 0);
                  acc.put(mode, j);
              }
              else {
                  JSONObject toAdd = new JSONObject();
                  toAdd.put("streak", 0);
                  acc.put(mode, toAdd);
              }
              obj.put(uuid, acc);
              try(BufferedWriter writer = new BufferedWriter(new FileWriter(winstreakFile))) {
                  writer.write(obj.toJSONString());
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }).start();
  }


}
