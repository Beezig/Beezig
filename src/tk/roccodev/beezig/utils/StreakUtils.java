package tk.roccodev.beezig.utils;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.games.CAI;
import tk.roccodev.beezig.games.Giant;
import tk.roccodev.beezig.games.HIDE;
import tk.roccodev.beezig.games.SKY;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StreakUtils {

    private static File winstreakFile;

    public static void init(File rootDir){
        winstreakFile = new File(rootDir + "/winstreaks.json");
        initValues(CAI.class, "cai");
        initValues(BED.class, "bed");
        initValues(SKY.class, "sky");
        initValues(Giant.class, "gnt");
        initValues(HIDE.class, "hide");
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

  public static String getMessageForCommand(String mode) {
      String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
      try(BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
          JSONObject obj = APIUtils.getObject(reader);
          if(obj == null) return Log.error + "No winstreak data found for the specified mode.";
          if(!obj.containsKey(uuid)) return Log.error + "No winstreak data found for the specified mode for the current player.";
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
              String spaces = "           ";

              StringBuilder msg = new StringBuilder();
              msg.append(bar).append('\n').append('\n');
              msg.append(Log.info +"§3Current Streak: §b" + streak).append('\n');
              msg.append(Log.info + "§3Best Streak: §b" + best).append('\n');
              msg.append('\n');
              msg.append(Log.info + "§3Last Reset: §b" + lrDate).append('\n');
              msg.append(Log.info + "§3Best Reset: §b" + brDate).append('\n');
              msg.append('\n').append(bar);

              return msg.toString();


          }
          else return Log.error + "No winstreak data found for the specified mode.";

      } catch (IOException e) {
          e.printStackTrace();
      }
      return Log.error + "Unexpected error.";
  }


  public static void resetWinstreak(String mode, boolean wasBest) {
      new Thread(() -> {
          String uuid = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
          try(BufferedReader reader = new BufferedReader(new FileReader(winstreakFile))) {
              JSONObject obj = APIUtils.getObject(reader);
              if(obj == null) obj = new JSONObject();
              JSONObject acc = obj.containsKey(uuid) ? (JSONObject) obj.get(uuid) : new JSONObject();
              if (acc.containsKey(mode)) {
                  JSONObject j = (JSONObject) acc.get(mode);
                  j.put("streak", 0);
                  j.put("lastReset", System.currentTimeMillis());
                  if(wasBest) j.put("bestReset", System.currentTimeMillis());
                  acc.put(mode, j);
              }
              else {
                  JSONObject toAdd = new JSONObject();
                  toAdd.put("streak", 0);
                  toAdd.put("lastReset", System.currentTimeMillis());
                  toAdd.put("bestReset", System.currentTimeMillis());
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
