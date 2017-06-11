package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.DRMap;

public class DR extends GameMode{

	public static DRMap activeMap;
	public static String currentMapPB;
	public static String currentMapWR;
	public static String currentMapWRHolder;
	public static String lastRecords = "";
	public static boolean dead = false;
	public static String role = null;
	public static long lastRecordPoints;
	public static int checkpoints;
	public static int deaths;
	public static int kills;
	
	public static void reset(DR gm){
		
		gm.setState(GameState.FINISHED);
		activeMap = null;
		currentMapPB = null;
		currentMapWR = null;
		currentMapWRHolder = null;
		role = null;
		checkpoints = 0;
		deaths = 0;
		kills = 0;
		ZTAMain.isDR = false;
		The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
	}
	
	@Override
	public String getName(){
		return "Deathrun";
	}

	public static boolean shouldRender(GameState state){
		
		if(state == GameState.GAME) return true;
		if(state == GameState.PREGAME) return true;
		if(state == GameState.STARTING) return true;
		return false;
	}

}
