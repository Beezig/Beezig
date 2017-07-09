package tk.roccodev.zta.games;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.BEDMap;
import tk.roccodev.zta.hiveapi.BEDRank;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class BED extends GameMode{
	
	public static char[] NUMBERS = {' ', '➊','➋','➌','➍','➎'};
	
	
	public static BEDMap activeMap;
	public static String team;
	public static String lastRecords = "";
	public static Long lastRecordsPoints = null;
	public static String mode = "";
	
	public static int kills;
	public static int deaths;
	public static int pointsCounter;
	public static int bedsDestroyed;
	
	public static String rank;
	public static BEDRank rankObject;
	
	public static List<String> votesToParse = new ArrayList<String>();
	public static boolean hasVoted = false;
	
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	
	
	
	// (§8▍ §3§lBed§b§lWars§8 ▏ §e§lVote for a map:)
	
	public static void reset(BED gm){
		
		gm.setState(GameState.FINISHED);
		BED.team = null;
		BED.mode = "";
		BED.activeMap = null;
		BED.hasVoted = false;
		BED.kills = 0;
		BED.deaths = 0;
		BED.bedsDestroyed = 0;
		BED.pointsCounter = 0;
		ActiveGame.reset("bed");
		IHive.genericReset();
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
	
	public static String updateResources(){
		StringBuilder sb = new StringBuilder();
		int ironIngots = The5zigAPI.getAPI().getItemCount("iron_ingot");
		int goldIngots = The5zigAPI.getAPI().getItemCount("gold_ingot");
		int diamonds = The5zigAPI.getAPI().getItemCount("diamond");
		int emeralds = The5zigAPI.getAPI().getItemCount("emerald");
		if(ironIngots != 0) sb.append(ironIngots + " Iron / ");
		if(goldIngots != 0) sb.append(goldIngots + " Gold / ");
		if(diamonds != 0) sb.append(diamonds + " Diamonds / ");
		if(emeralds != 0) sb.append(emeralds + " Emeralds / ");
		
		return sb.toString().trim();
	}
	
	public static void updateRank(){
		BED.rank = BEDRank.getRank(HiveAPI.BEDpoints).getName().replaceAll(ChatColor.stripColor(BEDRank.getRank(HiveAPI.BEDpoints).getName()), "") + BED.NUMBERS[BEDRank.getRank(HiveAPI.BEDpoints).getLevel((int)HiveAPI.BEDpoints)] + " " + BEDRank.getRank((int)HiveAPI.BEDpoints).getName();
		BED.rankObject = BEDRank.getRank(HiveAPI.BEDpoints);
	}

}
