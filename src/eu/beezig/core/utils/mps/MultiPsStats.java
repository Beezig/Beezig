package eu.beezig.core.utils.mps;

import eu.beezig.core.advancedrecords.anywhere.AdvancedRecordsAnywhere;
import eu.beezig.core.advancedrecords.anywhere.statistic.RecordsStatistic;

import java.util.HashMap;

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
                .filter(s -> value.equals(getValue(s.getKey()))).findAny().orElse(null);
    }

    private static String getValue(String s) {
        if(stats.containsKey(s)) return stats.get(s);
        else return s.toLowerCase().replace(" ", "");
    }

}
