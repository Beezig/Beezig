package eu.beezig.core.games;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.IHive;
import eu.beezig.core.games.logging.GameLogger;
import eu.beezig.core.hiveapi.stuff.gnt.GiantRank;
import eu.beezig.core.utils.StreakUtils;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Giant extends GameMode {

    public static int points;
    public static int teamsEliminated;
    public static int gold;
    public static Giant instance;
    public static String team = "";
    public static String activeMap;
    public static int giantKills;
    public static boolean hasVoted;
    public static String rank;
    public static GiantRank rankObject;
    public static boolean inGame;
    public static boolean hasWon;
    public static boolean isEnding;
    public static int winstreak;
    public static int bestStreak;
    public static int totalKills;

    //KDR
    public static int totalDeaths;
    public static int gameDeaths;
    public static int gameKills;
    public static double gameKdr;
    public static double totalKdr;
    public static int dailyPoints;
    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static List<String> votesToParse = new ArrayList<>();
    public static String gameId;
    private static GameLogger logger;
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public Giant() {
        instance = this;
    }

    public static void initDailyPointsWriter() throws IOException {
        logger = new GameLogger(BeezigMain.mcFile + "/gnt/games.csv");
        logger.setHeaders(new String[]{
                "Mode",
                "Map",
                "Kills",
                "Deaths",
                "Giants",
                "Victory?",
                "Timestamp",
                "GameID"
        });


        File f = new File(BeezigMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName);
        if (!f.exists()) {
            f.createNewFile();
            initPointsWriterWithZero();
            return;
        }
        FileInputStream stream = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        if (line == null) {
            initPointsWriterWithZero();
            stream.close();
            return;
        } else {
            Giant.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();

    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(Giant gameMode) {

        if (inGame && !hasWon) {
            boolean wasBest = winstreak >= bestStreak;
            System.out.println("Lost!");
            winstreak = 0;
            StreakUtils.resetWinstreak("gnt", wasBest);
        }
        System.out.println(inGame + " " + logger);
        if (inGame && logger != null)
            logger.logGame(ActiveGame.is("GNTM") ? "Mini" : "Normal",
                    activeMap, gameKills + "", gameDeaths + "", giantKills + "", hasWon ? "Yes" : "No", System.currentTimeMillis() + "", gameId);

        isEnding = false;
        gameId = null;
        hasWon = false;
        inGame = false;
        teamsEliminated = 0;
        gold = 0;
        team = "";
        activeMap = null;
        gameKills = 0;
        gameDeaths = 0;
        gameKdr = 0D;
        giantKills = 0;
        hasVoted = false;
        votesToParse.clear();

        gameMode.setState(GameState.FINISHED);
        ActiveGame.set("");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");


    }

    public boolean isMini() {
        return false;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "SkyGiants (Unspecified)";
    }


}
