package tk.roccodev.zta.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.stuff.gnt.GiantRank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Giant extends GameMode{

	public static int points;
	public static int teamsEliminated;
	public static int gold;
	public static Giant instance;
	public static String team = "";
	public static String activeMap;
	public static int giantKills;
	
	public static boolean hasVoted;
	
	public static String rank;
	public static GiantRank rankObject;
	
	
	//KDR
	
	public static int totalKills;
	public static int totalDeaths;
	public static int gameDeaths;
	public static int gameKills;
	public static double gameKdr;
	public static double totalKdr;
	
	private static PrintWriter dailyPointsWriter;
	private static String dailyPointsName;
	public static int dailyPoints;
	
	public static List<String> messagesToSend = new ArrayList<>();
	public static List<String> footerToSend = new ArrayList<>();
	public static boolean isRecordsRunning = false;
	public static String lastRecords = "";
	public static List<String> votesToParse = new ArrayList<>();
	
	public Giant(){
		instance = this;
	}
	
	
	public boolean isMini(){return false;}


	public static void initDailyPointsWriter() throws IOException {
		File f = new File(ZTAMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName);
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
			Giant.dailyPoints = Integer.parseInt(line);
		}
		stream.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName, "UTF-8");
		dailyPointsWriter.println(0);

		dailyPointsWriter.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/gnt/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	public static void setDailyPointsFileName(String newName) {
		dailyPointsName = newName;
	}

	private static void saveDailyPoints() {
		dailyPointsWriter.println(dailyPoints);
		dailyPointsWriter.flush();
		dailyPointsWriter.close();
	}
	
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


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SkyGiants (Unspecified)";
	}


	
	


	

}
