package tk.roccodev.beezig.games;

import com.csvreader.CsvWriter;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.APIValues;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVMap;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.beezig.notes.NotesManager;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TIMV extends GameMode {

    public static final double TRATIO_LIMIT = 35.7d;

    public static String joinMessage = ChatColor
            .stripColor(The5zigAPI.getAPI().getGameProfile().getName() + " wants to investigate!");

    public static int karmaCounter;
    public static TIMVMap activeMap;
    public static int traitorsBefore = 0;
    public static int traitorsDiscovered = 0;
    public static int detectivesBefore = 0;
    public static int detectivesDiscovered = 0;

    public static ArrayList<String> traitorTeam = new ArrayList<>();

    public static String gameID;
    public static int dailyKarma;
    public static String rank;
    public static TIMVRank rankObject;
    // Advanced Records
    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    // Autovoting
    public static List<String> votesToParse = new ArrayList<>();
    // Anti HAS 'test'
    public static List<String> testRequests = new ArrayList<>();
    public static HashMap<String, TIMVMap> mapsPool;
    public static int lastTestMsg = -1;
    public static String mapStr;
    public static List<String[]> csvEntries;
    public static String role;

    // CSV Stuff
    public static int tPoints;
    public static int dPoints;
    public static int iPoints;
    public static boolean dead;
    public static boolean hasVoted = false;
    public static long lastRecordKarma;
    public static boolean actionBarChecked = false;
    private static PrintWriter dailyKarmaWriter;
    private static String dailyKarmaName;

    public static void setDailyKarmaFileName(String newName) {
        dailyKarmaName = newName;
    }

    public static void initDailyKarmaWriter() throws IOException {
        File f = new File(BeezigMain.mcFile + "/timv/dailykarma/" + dailyKarmaName);
        if (!f.exists()) {
            f.createNewFile();
            initKarmaWriterWithZero();
            return;
        }
        FileInputStream stream = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        if (line == null) {
            initKarmaWriterWithZero();
            stream.close();
            return;
        } else {
            TIMV.dailyKarma = Integer.parseInt(line);
        }
        stream.close();
        reader.close();


    }

    private static void initKarmaWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyKarmaWriter = new PrintWriter(BeezigMain.mcFile + "/timv/dailykarma/" + dailyKarmaName, "UTF-8");
        dailyKarmaWriter.println(0);

        dailyKarmaWriter.close();


    }

    private static void saveDailyKarma() {
        try {
            dailyKarmaWriter = new PrintWriter(BeezigMain.mcFile + "/timv/dailykarma/" + dailyKarmaName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyKarmaWriter.println(dailyKarma);
        dailyKarmaWriter.flush();
        dailyKarmaWriter.close();
    }

    /**
     * Writes the data into the CSV logger.
     *
     * @return whether the writer has written the file.
     */
    public static boolean writeCsv() {
        The5zigAPI.getLogger().info("writing");
        // Prevent from writing a line twice
        if (role == null)
            return false;
        if (role.isEmpty())
            return false;
        The5zigAPI.getLogger().info("writing2");
        String mapName = mapStr == null ? "Unknown Map" : mapStr;
        String[] entries = {role, karmaCounter + "", mapName};
        CsvWriter writer;

        boolean alreadyExists = new File(BeezigMain.mcFile.getAbsolutePath() + "/timv/games.csv").exists();
        if (!alreadyExists) {
            try {
                new File(BeezigMain.mcFile.getAbsolutePath() + "/timv/games.csv").createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            writer = new CsvWriter(new FileWriter(BeezigMain.mcFile.getAbsolutePath() + "/timv/games.csv", true), ',');

            if (!alreadyExists) {
                // Create the header
                writer.write("role");
                writer.write("karma");
                writer.write("map");
                writer.write("points");
                writer.write("i-points");
                writer.write("d-points");
                writer.write("t-points");
                writer.write("gameId");
                writer.endRecord();
            }

            writer.write(role);
            writer.write(karmaCounter + "");
            writer.write(mapStr == null ? "Unknown Map" : mapStr);
            writer.write(iPoints + dPoints + tPoints + "");
            writer.write(iPoints + "");
            writer.write(dPoints + "");
            writer.write(tPoints + "");
            writer.write(gameID);
            writer.endRecord();
            writer.close();

        } catch (Exception e) {
            if (The5zigAPI.getAPI().isInWorld())
                The5zigAPI.getAPI().messagePlayer(Log.error + "Failed to write game csv.");
            e.printStackTrace();
        }
        role = null;
        resetCounter();
        TIMV.activeMap = null;
        mapStr = "";
        return true;
    }

    public static void plus20() {
        karmaCounter += 20;
        dailyKarma += 20;
        APIValues.TIMVkarma += 20;
    }

    public static void plus25() {
        karmaCounter += 25;
        dailyKarma += 25;
        APIValues.TIMVkarma += 25;
    }

    public static void plus10() {
        karmaCounter += 10;
        dailyKarma += 10;
        APIValues.TIMVkarma += 10;
    }

    public static void minus20() {
        karmaCounter -= 20;
        dailyKarma -= 20;
        APIValues.TIMVkarma -= 20;
    }

    public static void minus40() {
        karmaCounter -= 40;
        dailyKarma -= 40;
        APIValues.TIMVkarma -= 40;
    }

    public static void resetCounter() {
        karmaCounter = 0;
        iPoints = 0;
        tPoints = 0;
        dPoints = 0;
        dead = false;
    }

    public static void applyPoints(int points) {
        switch (role) {
            case "Traitor":
                applyPoints(points, "t");
                break;
            case "Innocent":
                applyPoints(points, "i");
                break;
            case "Detective":
                applyPoints(points, "d");
                break;
        }
    }

    public static void applyPoints(int points, String role) {
        switch (role) {
            case "t":
                tPoints += points;
                break;
            case "i":
                iPoints += points;
                break;
            case "d":
                dPoints += points;
                break;
        }
    }

    public static void calculateTraitors(int playersOnline) {
        TIMV.traitorsBefore = (int) Math.floor(playersOnline / 4.0);
        if (TIMV.traitorsBefore == 0) TIMV.traitorsBefore = 1;
    }

    public static void calculateDetectives(int playersOnline) {
        TIMV.detectivesBefore = (int) Math.floor(playersOnline / 8);
        if (TIMV.detectivesBefore == 0) TIMV.detectivesBefore = 1;
    }

    public static void reset(TIMV gm) {

        if (!TIMV.writeCsv()) {
            TIMV.activeMap = null;
            mapStr = "";
            role = null;
            resetCounter();
        }
        TIMV.traitorsBefore = 0;
        TIMV.traitorsDiscovered = 0;
        TIMV.detectivesBefore = 0;
        TIMV.detectivesDiscovered = 0;
        traitorTeam.clear();
        NotesManager.notes.clear();
        TIMV.messagesToSend.clear();
        TIMV.footerToSend.clear();
        TIMV.votesToParse.clear();

        TIMV.hasVoted = false;
        TIMV.actionBarChecked = false;
        lastTestMsg = -1;
        gm.setState(GameState.FINISHED);
        ActiveGame.reset("timv");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
        saveDailyKarma();
        The5zigAPI.getLogger().info(dailyKarma);

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
        return "Trouble in Mineville";
    }

    static class DateFormatter extends Formatter {
        //
        // Create a DateFormat to format the logger timestamp.
        //
        private DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");

            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

    }

}
