package tk.roccodev.beezig.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.hiveapi.stuff.hide.HIDERank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HIDE extends GameMode {

	public static String activeMap;

	public static List<String> messagesToSend = new ArrayList<>();
	public static List<String> footerToSend = new ArrayList<>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";

	public static boolean hasVoted = false;
	public static List<String> votesToParse = new ArrayList<>();

	private static PrintWriter dailyPointsWriter;
	private static String dailyPointsName;
	public static int dailyPoints;
	
	public static boolean seeking;
	public static int lastPts;

	public static String rank;
	public static HIDERank rankObject;

	public static void initDailyPointsWriter() throws IOException {
		File f = new File(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName);
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
			HIDE.dailyPoints = Integer.parseInt(line);
		}
		stream.close();

		dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
		dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName, "UTF-8");
		dailyPointsWriter.println(0);

		dailyPointsWriter.close();

		dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/hide/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	public static void setDailyPointsFileName(String newName) {
		dailyPointsName = newName;
	}

	private static void saveDailyPoints() {
		dailyPointsWriter.println(dailyPoints);
		dailyPointsWriter.flush();
		dailyPointsWriter.close();
	}

	public static void reset(HIDE gameMode) {

		gameMode.setState(GameState.FINISHED);

		HIDE.messagesToSend.clear();
		HIDE.footerToSend.clear();
		HIDE.votesToParse.clear();
		HIDE.isRecordsRunning = false;
		HIDE.hasVoted = false;
		HIDE.activeMap = null;
		lastPts = 0;
		seeking = false;
		ActiveGame.reset("hide");
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

	@Override
	public String getName() {
		return "Hide & Seek";
	}

}
