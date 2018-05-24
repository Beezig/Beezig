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
import java.util.Map;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDRank;

public class BED extends GameMode {

	public static char[] NUMBERS = { ' ', '➊', '➋', '➌', '➍', '➎' };

	public static String activeMap;
	public static String team;
	public static String lastRecords = "";
	public static Long lastRecordsPoints = null;
	public static String mode = "";

	public static int kills;
	public static int deaths;
	public static int pointsCounter;
	public static int bedsDestroyed;
	public static int teamsLeft;

	public static int apiKills;
	public static int apiDeaths;

	// Generators (0: None, 1: Level 1, 2: Level 2, 3: Level 3)

	public static int ironGen;
	public static int goldGen;
	public static int diamondGen;

	private static PrintWriter dailyPointsWriter;
	private static String dailyPointsName;
	public static int dailyPoints;

	public static double apiKdr;
	public static double gameKdr;

	public static String rank;
	public static BEDRank rankObject;

	public static List<String> votesToParse = new ArrayList<String>();
	public static boolean hasVoted = false;
	public static Boolean hasWon = null;

	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;

	public static int winstreak;

	public static void initDailyPointsWriter() throws IOException {
		File f = new File(ZTAMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName);
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
			BED.dailyPoints = Integer.parseInt(line);
		}
		stream.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName, "UTF-8");
		dailyPointsWriter.println(0);

		dailyPointsWriter.close();

		dailyPointsWriter = new PrintWriter(ZTAMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName, "UTF-8");

	}

	public static void setDailyPointsFileName(String newName) {
		dailyPointsName = newName;
	}

	private static void saveDailyPoints() {
		dailyPointsWriter.println(dailyPoints);
		dailyPointsWriter.flush();
		dailyPointsWriter.close();
	}

	// (§8▍ §3§lBed§b§lWars§8 ▏ §e§lVote for a map:)

	public static void reset(BED gm) {

		gm.setState(GameState.FINISHED);
		if (hasWon != null) {
			if (!hasWon) {
				winstreak = 0;
			}
		}
		BED.team = null;
		BED.mode = "";
		BED.activeMap = null;
		BED.hasVoted = false;
		BED.kills = 0;
		BED.deaths = 0;
		BED.bedsDestroyed = 0;
		BED.pointsCounter = 0;
		BED.teamsLeft = 0;
		BED.votesToParse.clear();
		ironGen = 0;
		goldGen = 0;
		diamondGen = 0;
		ActiveGame.reset("bed");
		hasWon = null;
		IHive.genericReset();
		if (The5zigAPI.getAPI().getActiveServer() != null)
			The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
		saveDailyPoints();
	}

	@Override
	public String getName() {
		return "Bedwars";
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

	public static void updateTeamsLeft() {
		Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();

		for (Map.Entry<String, Integer> entry : sb.getLines().entrySet()) {

			if (entry.getValue() == 13) {
				BED.teamsLeft = ChatColor.stripColor(entry.getKey()).toCharArray().length;
			}
		}
	}

	public static String updateResources() {
		StringBuilder sb = new StringBuilder();
		int ironIngots = The5zigAPI.getAPI().getItemCount("iron_ingot");
		int goldIngots = The5zigAPI.getAPI().getItemCount("gold_ingot");
		int diamonds = The5zigAPI.getAPI().getItemCount("diamond");
		int emeralds = The5zigAPI.getAPI().getItemCount("emerald");
		if (ironIngots != 0)
			sb.append(ironIngots).append(" Iron / ");
		if (goldIngots != 0)
			sb.append(goldIngots).append(" Gold / ");
		if (diamonds != 0)
			sb.append(diamonds).append(" Diamonds / ");
		if (emeralds != 0)
			sb.append(emeralds).append(" Emeralds / ");

		return sb.toString().trim();
	}

	public static void updateRank() {
		BED.rank = BEDRank.getRank(APIValues.BEDpoints).getName()
				.replaceAll(ChatColor.stripColor(BEDRank.getRank(APIValues.BEDpoints).getName()), "")
				+ BED.NUMBERS[BEDRank.getRank(APIValues.BEDpoints).getLevel((int) APIValues.BEDpoints)] + " "
				+ BEDRank.getRank((int) APIValues.BEDpoints).getName();
		BED.rankObject = BEDRank.getRank(APIValues.BEDpoints);
	}

	public static void updateKdr() {
		apiKdr = (double) apiKills / (apiDeaths == 0 ? 1 : apiDeaths);
		gameKdr = (double) (kills + apiKills) / (deaths + apiDeaths == 0 ? 1 : apiDeaths + deaths);
	}

	public static void updateMode() {
		// ffs mode is so annoying
		Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
		The5zigAPI.getLogger().info(sb.getTitle());
		if (sb != null && sb.getTitle().contains("BED ")) {
			BED.mode = "Solo";
		}
		if (sb != null && sb.getTitle().contains("BEDD ")) {
			BED.mode = "Duo";
		}
		if (sb != null && sb.getTitle().contains("BEDT ")) {
			BED.mode = "Teams";
		}
	}

}
