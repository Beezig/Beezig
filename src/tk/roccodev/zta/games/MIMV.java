package tk.roccodev.zta.games;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
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
	
	private static PrintWriter dailyPointsWriter;
	private static String dailyPointsName;
	public static int dailyPoints;
	
	public static int gamePts;
	
	public static String rank;
	public static MIMVRank rankObject;
	
	public static List<String> votesToParse = new ArrayList<String>();

	public static void initDailyPointsWriter() throws IOException {
		File f = new File(ZTAMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName);
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

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName, "UTF-8");
		dailyPointsWriter.println(0);

		dailyPointsWriter.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/mimv/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	public static void setDailyPointsFileName(String newName) {
		dailyPointsName = newName;
	}

	private static void saveDailyPoints() {
		dailyPointsWriter.println(dailyPoints);
		dailyPointsWriter.flush();
		dailyPointsWriter.close();
	}
	
	public static void reset(MIMV gameMode) {

		gameMode.setState(GameState.FINISHED);

		role = "";
		map = "";
		gamePts = 0;
		votesToParse.clear();
		hasVoted = false;
		ActiveGame.reset("mimv");
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
		if (state == GameState.STARTING)
			return true;
		return false;
	}

}
