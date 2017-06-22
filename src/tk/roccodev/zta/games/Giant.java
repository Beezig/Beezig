package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.hiveapi.GiantMap;

public class Giant extends GameMode{

	public static int points;
	public static int teamsEliminated;
	public static int gold;
	public static Giant instance;
	public static String team = "";
	public static GiantMap activeMap;
	
	public Giant(){
		instance = this;
	}
	
	
	public boolean isMini(){return false;};
	
	
	public static void reset(Giant gameMode){
		
		teamsEliminated = 0;
		gold = 0;
		team = "";
		activeMap = null;
		gameMode.setState(GameState.FINISHED);
		ActiveGame.set("");
		The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
		
		
		
		
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SkyGiants (Unspecified)";
	}


	
	


	

}
