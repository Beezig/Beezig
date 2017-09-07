package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.HIDEMap;

import java.util.ArrayList;
import java.util.List;

public class HIDE extends GameMode {
	
	public static HIDEMap activeMap;
	
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	
	public static boolean hasVoted = false;
	public static List<String> votesToParse = new ArrayList<String>();


	public static void reset(HIDE gameMode){
		
		gameMode.setState(GameState.FINISHED);

		HIDE.messagesToSend.clear();
		HIDE.footerToSend.clear();
		HIDE.votesToParse.clear();
		HIDE.isRecordsRunning = false;
		HIDE.hasVoted = false;
		HIDE.activeMap = null;

		ActiveGame.reset("hide");
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
		return "Hide & Seek";
	}

}
