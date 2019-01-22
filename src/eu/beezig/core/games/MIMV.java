package eu.beezig.core.games;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.logging.GameLogger;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.hiveapi.stuff.mimv.MIMVRank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MIMV extends GameMode {

    private static GameLogger logger;

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static String role;
    public static boolean hasVoted = false;
    public static String map;
    public static int dailyPoints;
    public static int gamePts;
    public static String rank;
    public static MIMVRank rankObject;
    public static List<String> votesToParse = new ArrayList<>();
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public static void initDailyPointsWriter() throws IOException {
        File f = new File(BeezigMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName);
        if (!f.exists()) {
            f.createNewFile();
            initPointsWriterWithZero();
            return;
        }

        logger = new GameLogger(BeezigMain.mcFile + "/mimv/games.csv");
        logger.setHeaders(new String[] {
                "Karma",
                "Role",
                "Map"
        });

        FileInputStream stream = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        if (line == null) {
            initPointsWriterWithZero();
            stream.close();
            return;
        } else {
            MIMV.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();

    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(MIMV gameMode) {

        gameMode.setState(GameState.FINISHED);

        if(role != null && !role.isEmpty() && logger != null)
            logger.logGame(gamePts + "", ChatColor.stripColor(role), map);

        role = "";
        map = "";
        gamePts = 0;
        votesToParse.clear();
        hasVoted = false;
        ActiveGame.reset("mimv");
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
        // TODO Auto-generated method stub
        return "Murder in Mineville";
    }

}
