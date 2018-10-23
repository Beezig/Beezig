package tk.roccodev.beezig.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.games.logging.GameLogger;
import tk.roccodev.beezig.hiveapi.stuff.sky.SKYRank;
import tk.roccodev.beezig.utils.StreakUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SKY extends GameMode {

    private static GameLogger logger;

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static boolean hasVoted = false;
    public static List<String> votesToParse = new ArrayList<>();

    public static String mode;

    public static long gamePoints;
    public static int totalKills;

    public static int kills;
    public static int deaths;

    public static boolean inGame;
    public static boolean hasWon;
    public static int winstreak;
    public static int bestStreak;

    public static int apiKills, apiDeaths;

    public static double apiKdr;
    public static double gameKdr;
    public static int dailyPoints;
    public static String rank;
    public static String team;
    public static SKYRank rankObject;
    public static String map;
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public static void updateKdr() {
        apiKdr = (double) apiKills / (apiDeaths == 0 ? 1 : apiDeaths);
        gameKdr = (double) (kills + apiKills) / (deaths + apiDeaths == 0 ? 1 : apiDeaths + deaths);
    }

    public static void initDailyPointsWriter() throws IOException {
        File f = new File(BeezigMain.mcFile + "/sky/dailyPoints/" + dailyPointsName);
        if (!f.exists()) {
            f.createNewFile();
            initPointsWriterWithZero();
            return;
        }

        logger = new GameLogger(BeezigMain.mcFile + "/sky/games.csv");
        logger.setHeaders(new String[] {
                "Points",
                "Map",
                "Kills",
                "Mode",
                "Victory?"
        });

        FileInputStream stream = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        if (line == null) {
            initPointsWriterWithZero();
            stream.close();
            return;
        } else {
            SKY.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();

    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/sky/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/sky/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(SKY gameMode) {

        gameMode.setState(GameState.FINISHED);
        if (inGame && !hasWon) {
            boolean wasBest = winstreak >= bestStreak;
            System.out.println("Lost!");
            winstreak = 0;
            StreakUtils.resetWinstreak("sky", wasBest);
        }

        if(inGame && logger != null)
            logger.logGame(gamePoints + "", map, kills + "", mode,  hasWon ? "Yes" : "No");

        hasWon = false;
        inGame = false;
        SKY.messagesToSend.clear();
        SKY.footerToSend.clear();
        SKY.votesToParse.clear();

        SKY.hasVoted = false;
        gamePoints = 0;
        team = "";
        mode = "";
        kills = 0;
        deaths = 0;
        ActiveGame.reset("sky");
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
        return "SkyWars";
    }

}
