package tk.roccodev.zta.games;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.DRMap;

public class BED extends GameMode{
	

	
	
	public static List<String> votesToParse = new ArrayList<String>();
	public static boolean hasVoted = false;
	
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	
	// (§8▍ §3§lBed§b§lWars§8 ▏ §e§lVote for a map:)
	
	public static void reset(BED gm){
		
		gm.setState(GameState.FINISHED);
		
		BED.hasVoted = false;
		ActiveGame.reset("bed");
		The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
	}
	
	@Override
	public String getName(){
		return "Bedwars";
	}

	public static boolean shouldRender(GameState state){
		
		if(state == GameState.GAME) return true;
		if(state == GameState.PREGAME) return true;
		if(state == GameState.STARTING) return true;
		return false;
	}

}
