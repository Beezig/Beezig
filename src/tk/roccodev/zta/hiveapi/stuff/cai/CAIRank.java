package tk.roccodev.zta.hiveapi.stuff.cai;

import static eu.the5zig.util.minecraft.ChatColor.AQUA;
import static eu.the5zig.util.minecraft.ChatColor.BLUE;
import static eu.the5zig.util.minecraft.ChatColor.BOLD;
import static eu.the5zig.util.minecraft.ChatColor.DARK_AQUA;
import static eu.the5zig.util.minecraft.ChatColor.DARK_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.GOLD;
import static eu.the5zig.util.minecraft.ChatColor.LIGHT_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.RED;
import static eu.the5zig.util.minecraft.ChatColor.WHITE;
import static eu.the5zig.util.minecraft.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;

import eu.the5zig.util.minecraft.ChatColor;

public enum CAIRank {

	CAMPFIRE("Campfire", GOLD + "", 0),
	BLACKSMITH("Blacksmith", LIGHT_PURPLE + "", 100),
	RUSTLER("Rustler", AQUA + "", 1000),
	BRONCO("Bronco", YELLOW + "", 2500),
	WRANGLER("Wrangler", ChatColor.GREEN + "", 3500),
	BUCKAROO("Buckaroo", RED + "", 5000),
	WIGWAM("Wigwam", BLUE + "", 7500),
	CABOOSE("Caboose", DARK_PURPLE + "", 15000),
	AMBUSHER("Ambusher", GOLD + "", 25000),
	PISTOLPETE("Pistol Pete", LIGHT_PURPLE + "", 35000),
	RATTLESNAKE("Rattlesnake", AQUA + "", 45000),
	OUTLAW("Outlaw", YELLOW + "", 75000),
	CHIEF("Chief", RED + "", 125000),
	MUSTANG("Mustang",  AQUA + "" + BOLD, 250000),
	STALLION("Stallion", GOLD + "" + BOLD, 500000),
	REVOLVER("Revolver", LIGHT_PURPLE + "" + BOLD, 750000),
	TOMAHAWK("Tomahawk", YELLOW + "" + BOLD, 1000000),
	SHERIFF("Sheriff", RED + "" + BOLD, -1);
	
	private String display;
	private String prefix;
	private int startPoints;
	
	
	CAIRank(String display, String prefix, int startPts){
		this.display = display;
		this.prefix = prefix;
		this.startPoints = startPts;
	}

	public static CAIRank getFromDisplay(String display){
		for(CAIRank rank : CAIRank.values()){
			if(rank.getDisplay().equalsIgnoreCase(display)) return rank;
		}
		return null;
	}
	
	
	public String getDisplay() {
		return display;
	}
	
	public String getTotalDisplay(){
		return prefix + display;
	}
	
	public int getStart(){
		return startPoints;
	}
	
	public String getPointsToNextRank(int points){
		if(this == SHERIFF) return "Leaderboard Rank";
		if(this == TOMAHAWK) return "Highest Rank";
		ArrayList<CAIRank> ranks = new ArrayList<CAIRank>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		CAIRank next = null;
		try{
			next = ranks.get(newIndex);
			
		}catch(Exception e){
			return "";
		}
		
		
		
		return next.getStart() - points + " to " + next.prefix + next.getTotalDisplay();
	}
	
}
