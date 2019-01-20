package tk.roccodev.beezig.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.games.logging.GameLogger;
import tk.roccodev.beezig.hiveapi.stuff.bp.BPRank;
import tk.roccodev.beezig.utils.ws.Connector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BP extends GameMode {

    private static GameLogger logger;

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static String song;
    public static String artist;
    public static int gamePts;
    public static int dailyPoints;
    public static String rank;
    public static BPRank rankObject;
    public static List<String> votesToParse = new ArrayList<>();
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public static void initDailyPointsWriter() throws IOException {

        logger = new GameLogger(BeezigMain.mcFile + "/bp/games.csv");
        logger.setHeaders(new String[] {
                "Points",
                "Song",
                "Timestamp"
        });

        File f = new File(BeezigMain.mcFile + "/bp/dailyPoints/" + dailyPointsName);
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
            BP.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();


    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(BP gameMode) {

        if(gameMode.getState() == GameState.GAME && logger != null)
            logger.logGame(gamePts + "", song, System.currentTimeMillis() + "");
        gameMode.setState(GameState.FINISHED);
        song = null;
        artist = null;

        gamePts = 0;
        votesToParse.clear();
        ActiveGame.reset("bp");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
        saveDailyPoints();
        Connector.leaveLobbyBP();
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
        return "BlockParty";
    }

}
