package tk.roccodev.beezig.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.hiveapi.stuff.grav.GRAVRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GRAV extends GameMode {

    public static HashMap<String, String> mapsPool = new HashMap<>();

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static boolean isRecordsRunning = false;
    public static String lastRecords = "";

    public static boolean hasVoted = false;
    public static HashMap<Integer, ArrayList<String>> mapsToParse = new HashMap<>();

    public static long gamePoints;


    public static int fails;
    public static int failsCache;

    public static int apiGamesPlayed, apiVictories;

    public static String rank;
    public static GRAVRank rankObject;

    public static ArrayList<String> maps = new ArrayList<>();
    public static HashMap<String, Double> mapPBs = new HashMap<>();
    public static HashMap<Integer, String> toDisplay = new HashMap<>();
    public static HashMap<Integer, String> toDisplayWithFails = new HashMap<>();
    public static int currentMap = -1;


    public static void reset(GRAV gameMode) {

        gameMode.setState(GameState.FINISHED);

        GRAV.messagesToSend.clear();
        GRAV.footerToSend.clear();
        GRAV.mapsToParse.clear();
        GRAV.isRecordsRunning = false;
        GRAV.hasVoted = false;
        currentMap = -1;
        gamePoints = 0;
        fails = 0;
        failsCache = 0;
        toDisplay.clear();
        toDisplayWithFails.clear();
        maps.clear();
        mapPBs.clear();
        ActiveGame.reset("grav");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
    }

    public static boolean shouldRender(GameState state) {

        if (state == GameState.GAME) return true;
        if (state == GameState.PREGAME) return true;
        return state == GameState.STARTING;
    }


    @Override
    public String getName() {
        return "Gravity";
    }

}
