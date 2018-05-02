package tk.roccodev.zta.games;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.stuff.mimv.MIMVRank;

public class MIMV extends GameMode {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Murder in Mineville";
	}

	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	
	public static String role;
	public static boolean hasVoted = false;
	public static String map;
	
	public static String rank;
	public static MIMVRank rankObject;
	
	public static List<String> votesToParse = new ArrayList<String>();

	public static void reset(MIMV gameMode) {

		gameMode.setState(GameState.FINISHED);

		role = "";
		map = "";
		votesToParse.clear();
		hasVoted = false;
		ActiveGame.reset("mimv");
		IHive.genericReset();
		if (The5zigAPI.getAPI().getActiveServer() != null)
			The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
	}

	public static boolean shouldRender(GameState state) {

		if (state == GameState.GAME)
			return true;
		if (state == GameState.PREGAME)
			return true;
		if (state == GameState.STARTING)
			return true;
		return false;
	}

}
