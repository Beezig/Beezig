package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.GiantMap;

import java.util.ArrayList;
import java.util.List;

public class Giant extends GameMode{

	public static int points;
	public static int teamsEliminated;
	public static int gold;
	public static Giant instance;
	public static String team = "";
	public static GiantMap activeMap;
	public static int giantKills;
	
	public static boolean hasVoted;
	
	//KDR
	
	public static int totalKills;
	public static int totalDeaths;
	public static int gameDeaths;
	public static int gameKills;
	public static double gameKdr;
	public static double totalKdr;
	
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	public static List<String> votesToParse = new ArrayList<String>();
	
	public Giant(){
		instance = this;
	}
	
	
	public boolean isMini(){return false;}


	public static void reset(Giant gameMode){
		
		teamsEliminated = 0;
		gold = 0;
		team = "";
		activeMap = null;
		gameKills = 0;
		gameDeaths = 0;
		gameKdr = 0D;
		giantKills = 0;
		hasVoted = false;
		votesToParse.clear();
		
		gameMode.setState(GameState.FINISHED);
		ActiveGame.set("");
		IHive.genericReset();
		if(The5zigAPI.getAPI().getActiveServer() != null)
		The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
		
		
		
		
	}


	public void cast(){}
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SkyGiants (Unspecified)";
	}


	
	


	

}
