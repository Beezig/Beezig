package eu.beezig.core.utils.mps;

import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.anywhere.AdvancedRecordsAnywhere;
import eu.beezig.core.advancedrecords.anywhere.GamemodeStatistics;
import eu.beezig.core.advancedrecords.anywhere.statistic.RecordsStatistic;
import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MultiPsStats {


    private static final String PTS = "points";
    private static final String GM = "games";
    private static final String V = "victories";
    private static final String K = "kills";
    private static final String D = "deaths";
    private static final String KD = "kd";
    private static final String WL = "wl";

    private static HashMap<String, String> stats = new HashMap<>();

    public static void init() {
        stats.put("Points", PTS);
        stats.put("Games Played", GM);
        stats.put("Victories", V);
        stats.put("Kills", K);
        stats.put("Deaths", D);
        stats.put("K/D", KD);
        stats.put("Win Rate", WL);
    }

    public static RecordsStatistic getRecordsStatistic(String mode, String value) {
        return AdvancedRecordsAnywhere.getGamemode(mode).getStatistics().stream()
                .filter(s -> value.equals(getValue(s.getKey()))).findAny().orElseGet(() -> {
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Statistic not found.");
                    return null;
                });
    }

    private static String getValue(String s) {
        if (stats.containsKey(s)) return stats.get(s);
        else return s.toLowerCase().replace(" ", "");
    }

    public static List<String> getAllPossibleValues(String mode) {
        List<String> result = new ArrayList<>();
        GamemodeStatistics gm = AdvancedRecordsAnywhere.getGamemode(mode);
        if (gm == null) return result;

        return gm.getStatistics().stream().map(stat -> {
            String value = getValue(stat.getKey());
            if (value == null) return stat.getKey().toLowerCase().replace(" ", "");
            else return value;
        }).collect(Collectors.toList());
    }

}
