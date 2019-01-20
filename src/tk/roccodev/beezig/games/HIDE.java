package tk.roccodev.beezig.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.games.logging.GameLogger;
import tk.roccodev.beezig.games.logging.hide.HideMapRecords;
import tk.roccodev.beezig.hiveapi.stuff.hide.HIDERank;
import tk.roccodev.beezig.utils.StreakUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HIDE extends GameMode {

    private static GameLogger logger;

    public static String activeMap;

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static boolean hasVoted = false;
    public static List<String> votesToParse = new ArrayList<>();


    public static boolean inGame;
    public static boolean hasWon;
    public static int winstreak;
    public static int bestStreak;

    public static long mostKills;

    public static int dailyPoints;
    public static int kills;
    public static boolean seeking;
    public static int lastPts;
    public static String rank;
    public static HIDERank rankObject;
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;
    public static String gameId;

    public static void initDailyPointsWriter() throws IOException {

        logger = new GameLogger(BeezigMain.mcFile + "/hide/games.csv");
        logger.setHeaders(new String[] {
                "Map",
                "Victory?",
                "Kills",
                "Timestamp",
                "GameID"
        });


        File f = new File(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName);
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
            HIDE.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();

    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();

    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(HIDE gameMode) {

        gameMode.setState(GameState.FINISHED);
        if (inGame && !hasWon) {
            boolean wasBest = winstreak >= bestStreak;
            System.out.println("Lost!");
            winstreak = 0;
            StreakUtils.resetWinstreak("hide", wasBest);
        }
        if(inGame)
            HideMapRecords.endGame(activeMap, kills);
        if(inGame && logger != null)
        logger.logGame(activeMap, hasWon ? "Yes" : "No", kills + "", System.currentTimeMillis() + "", gameId);
        gameId = null;
        inGame = false;
        hasWon = false;
        HIDE.messagesToSend.clear();
        HIDE.footerToSend.clear();
        HIDE.votesToParse.clear();

        HIDE.hasVoted = false;
        HIDE.activeMap = null;
        kills = 0;
        lastPts = 0;
        mostKills = 0;
        seeking = false;
        ActiveGame.reset("hide");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
        saveDailyPoints();
    }

    public static boolean shouldRender(GameState state) {

        if (state == GameState.GAME)
            return true;
        if (state == GameState.PREGAME)
            return true;
        return state == GameState.STARTING;
    }

    @Override
    public String getName() {
        return "Hide & Seek";
    }

}
