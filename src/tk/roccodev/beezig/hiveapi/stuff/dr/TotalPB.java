package tk.roccodev.beezig.hiveapi.stuff.dr;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.games.DR;

import java.util.Map;

public class TotalPB {

    public static String getTotalPB(Map<String, Long> mapRecords) {
        try {

            if (mapRecords.size() != DR.mapsPool.size()) return null;
            //Doesn't have a time on every map

            long time = 0;

            for (Object x : mapRecords.values()) {
                time += (long) x;
            }

            int seconds = Math.toIntExact(time) % 60;
            int minutes = Math.floorDiv(Math.toIntExact(time), 60);
            if (seconds < 10) {
                return (minutes + ":0" + seconds);
            }
            return (minutes + ":" + seconds);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


}
