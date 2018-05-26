package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.stuff.bp.BPRank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BP extends GameMode {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "BlockParty";
	}

	public static List<String> messagesToSend = new ArrayList<>();
	public static List<String> footerToSend = new ArrayList<>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	
	public static String song;
	public static String artist;
	
	public static int gamePts;
	
	private static PrintWriter dailyPointsWriter;
	private static String dailyPointsName;
	public static int dailyPoints;

	
	public static String rank;
	public static BPRank rankObject;
	
	public static List<String> votesToParse = new ArrayList<>();

	
	public static void initDailyPointsWriter() throws IOException {
		File f = new File(ZTAMain.mcFile + "/bp/dailyPoints/" + dailyPointsName);
		if (!f.exists()) {
			f.createNewFile();
			initPointsWriterWithZero();
			return;
		}
		FileInputStream stream = new FileInputStream(f);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = reader.readLine();
		if (line == null) {
			initPointsWriterWithZero();
			stream.close();
			return;
		} else {
			BP.dailyPoints = Integer.parseInt(line);
		}
		stream.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");
		dailyPointsWriter.println(0);

		dailyPointsWriter.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	public static void setDailyPointsFileName(String newName) {
		dailyPointsName = newName;
	}
	
	private static void saveDailyPoints() {
		dailyPointsWriter.println(dailyPoints);
		dailyPointsWriter.flush();
		dailyPointsWriter.close();
	}
	
	
	public static void reset(BP gameMode) {

		gameMode.setState(GameState.FINISHED);

		song = null;
		artist = null;

		gamePts = 0;
		votesToParse.clear();
		ActiveGame.reset("bp");
		IHive.genericReset();
		if (The5zigAPI.getAPI().getActiveServer() != null)
			The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
		saveDailyPoints();
	}

	public static boolean shouldRender(GameState state) {

		if (state == GameState.GAME)
			return true;
		if (state == GameState.PREGAME)
			return true;
		return state == GameState.STARTING;
	}

}
