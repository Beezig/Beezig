package tk.roccodev.zta.games;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.stuff.sky.SKYRank;

public class SKY extends GameMode {
	
	
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	
	public static boolean hasVoted = false;
	public static List<String> votesToParse = new ArrayList<String>();

	public static String mode;
	
	public static long gamePoints;
	public static int totalKills;
	
	public static int kills;
	
	public static String rank;
	public static String team;
	public static SKYRank rankObject;
	public static String map;


	public static void reset(SKY gameMode){
		
		gameMode.setState(GameState.FINISHED);

		SKY.messagesToSend.clear();
		SKY.footerToSend.clear();
		SKY.votesToParse.clear();
		SKY.isRecordsRunning = false;
		SKY.hasVoted = false;
		gamePoints = 0;
		team = "";
		mode = "";
		kills = 0;
		ActiveGame.reset("sky");
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
		return "SkyWars";
	}

}
