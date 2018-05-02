package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.stuff.grav.GRAVRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GRAV extends GameMode {

	public static HashMap<String, String> mapsPool = new HashMap<String, String>();

	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";

	public static boolean hasVoted = false;
	public static HashMap<Integer, ArrayList<String>> mapsToParse = new HashMap<Integer, ArrayList<String>>();
	
	public static long gamePoints;
	
	public static int deaths;

	public static int apiGamesPlayed, apiVictories;

	public static String rank;
	public static GRAVRank rankObject;
	
	public static ArrayList<String> maps = new ArrayList<String>();
	public static HashMap<String, Double> mapPBs = new HashMap<String, Double>();
	public static HashMap<Integer, String> toDisplay = new HashMap<Integer, String>();
	public static int currentMap = 0; //(0,1,2,3,4)
	

	public static void reset(GRAV gameMode){

		gameMode.setState(GameState.FINISHED);

		GRAV.messagesToSend.clear();
		GRAV.footerToSend.clear();
		GRAV.mapsToParse.clear();
		GRAV.isRecordsRunning = false;
		GRAV.hasVoted = false;
		currentMap = 0;
		gamePoints = 0;
		deaths = 0;
		toDisplay.clear();
		maps.clear();
		mapPBs.clear();
		ActiveGame.reset("grav");
		IHive.genericReset();
		if(The5zigAPI.getAPI().getActiveServer() != null)
			The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
	}

	public static boolean shouldRender(GameState state){

		if(state == GameState.GAME) return true;
		if(state == GameState.PREGAME) return true;
		if(state == GameState.STARTING) return true;
		return false;
	}

	
 
	

	@Override
	public String getName() {
		return "Gravity";
	}

}
