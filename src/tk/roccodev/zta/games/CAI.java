package tk.roccodev.zta.games;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIMap;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIRank;

public class CAI extends GameMode {
	
	public static CAIMap activeMap;
	
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	
	public static boolean hasVoted = false;
	public static List<String> votesToParse = new ArrayList<String>();

	public static String rank;
	public static CAIRank rankObject;

	public static void reset(CAI gameMode){
		
		gameMode.setState(GameState.FINISHED);

		CAI.messagesToSend.clear();
		CAI.footerToSend.clear();
		CAI.votesToParse.clear();
		CAI.isRecordsRunning = false;
		CAI.hasVoted = false;
		CAI.activeMap = null;

		ActiveGame.reset("cai");
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
		return "Cowboys and Indians";
	}

}
