package tk.roccodev.beezig.advancedrecords.anywhere;

import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.advancedrecords.anywhere.statistic.PercentRatioStatistic;
import tk.roccodev.beezig.advancedrecords.anywhere.statistic.RatioRecordsStatistic;
import tk.roccodev.beezig.advancedrecords.anywhere.statistic.RecordsStatistic;
import tk.roccodev.beezig.advancedrecords.anywhere.util.GamemodeBuilder;
import tk.roccodev.beezig.hiveapi.wrapper.APIGameMode;

import java.util.ArrayList;
import java.util.List;

public class AdvancedRecordsAnywhere {

    private static List<GamemodeStatistics> gamemodes = new ArrayList<>();

    // A bunch of string constants
    private static final String PTS = "Points";
    private static final String GM = "Games Played";
    private static final String V = "Victories";
    private static final String K = "Kills";
    private static final String D = "Deaths";
    private static final String KD = "K/D";
    private static final String WR = "Win Rate";

    public static GamemodeStatistics getGamemode(String query) {
        for(GamemodeStatistics gamemode : gamemodes) {
            if(query.matches("(?i:" + gamemode.getGamemode() + ")")) return gamemode;
        }
        return null;
    }

    public static void register() {

        gamemodes.add(new GamemodeBuilder("BED") // BED, BEDS, BEDT etc.
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RecordsStatistic("Beds Destroyed", "beds_destroyed"))
                .addStatistic(new RecordsStatistic("Teams Eliminated", "teams_eliminated"))
                .addStatistic(new RatioRecordsStatistic(KD, "kills", "deaths"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played"))
                .build());

    }

    public static void run(String player, String mode) {
        GamemodeStatistics gm = getGamemode(mode);
        if(gm == null) {
           The5zigAPI.getAPI().messagePlayer(Log.error + "Game mode not found or not supported.");
            return;
        }
        The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
        new Thread(() -> {

            APIGameMode apiObj = new APIGameMode(player) {
                @Override
                public String getShortcode() {
                    return mode.toUpperCase();
                }
            };

            JSONObject jObj = apiObj.jsonObject();

            The5zigAPI.getAPI().messagePlayer("          §6§m                  §f " + apiObj.getParentMode().getCorrectName() + "'s Stats §6§m                  ");

            for(RecordsStatistic stat : gm.getStatistics()) {
                The5zigAPI.getAPI().messagePlayer(" §3" + stat.getKey() + ":§b " + stat.getValue(jObj));
            }

            The5zigAPI.getAPI().messagePlayer("                      §6§m                  §6§m                  ");


        }).start();

    }



}
