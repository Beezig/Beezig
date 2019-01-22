package eu.beezig.core.games;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.logging.GameLogger;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.hiveapi.stuff.cai.CAIRank;
import eu.beezig.core.utils.StreakUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CAI extends GameMode {

    public static String activeMap;

    private static GameLogger logger;
    public static String gameId;

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static boolean hasVoted = false;
    public static List<String> votesToParse = new ArrayList<>();

    public static boolean inGame;
    public static boolean hasWon;
    public static int winstreak;
    public static int bestStreak;

    public static long speedCooldown;
    public static long invisCooldown;
    public static long leaderItem0;
    public static long leaderItem1;
    public static long leaderItem2;

    public static long gamePoints;
    public static int dailyPoints;
    public static String rank;
    public static String team;
    public static CAIRank rankObject;
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public static void initDailyPointsWriter() throws IOException {
        logger = new GameLogger(BeezigMain.mcFile + "/cai/games.csv");
        logger.setHeaders(new String[] {
                "Points",
                "Map",
                "Victory?",
                "GameID",
                "Timestamp"
        });

        File f = new File(BeezigMain.mcFile + "/cai/dailyPoints/" + dailyPointsName);
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
            CAI.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();


    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/cai/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/cai/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(CAI gameMode) {
        System.out.println("reset");
        gameMode.setState(GameState.FINISHED);
        if (inGame && !hasWon) {
            boolean wasBest = winstreak >= bestStreak;
            System.out.println("Lost!");
            CAI.winstreak = 0;
            StreakUtils.resetWinstreak("cai", wasBest);
        }
        System.out.println(inGame + " " + logger);
        if(inGame && logger != null)
        logger.logGame(gamePoints + "", activeMap, hasWon ? "Yes" : "No", gameId, System.currentTimeMillis() + "");

        gameId = null;
        inGame = false;
        hasWon = false;
        invisCooldown = 0;
        speedCooldown = 0;
        leaderItem0 = 0;
        leaderItem1 = 0;
        leaderItem2 = 0;
        CAI.messagesToSend.clear();
        CAI.footerToSend.clear();
        CAI.votesToParse.clear();

        CAI.hasVoted = false;
        CAI.activeMap = null;
        gamePoints = 0;

        team = "";
        ActiveGame.reset("cai");
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
        return "Cowboys and Indians";
    }

}
