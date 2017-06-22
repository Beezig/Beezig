package tk.roccodev.zta.hiveapi;

import static eu.the5zig.util.minecraft.ChatColor.AQUA;
import static eu.the5zig.util.minecraft.ChatColor.BLUE;
import static eu.the5zig.util.minecraft.ChatColor.BOLD;
import static eu.the5zig.util.minecraft.ChatColor.DARK_AQUA;
import static eu.the5zig.util.minecraft.ChatColor.DARK_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.GOLD;
import static eu.the5zig.util.minecraft.ChatColor.GRAY;
import static eu.the5zig.util.minecraft.ChatColor.LIGHT_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.RED;
import static eu.the5zig.util.minecraft.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;

import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.games.BED;

import static eu.the5zig.util.minecraft.ChatColor.WHITE;

public enum TIMVRank {

	CIVILIAN("Civilian", GRAY + "", 0),
	INSPECTOR("Inspector", DARK_AQUA + "", 101),
	WITNESS("Witness", AQUA + "", 501),
	SCOUT("Scout", LIGHT_PURPLE + "", 751),
	FORENSIC("Forensic", GOLD + "", 1001),
	TRUSTABLE("Trustable", YELLOW + "", 2001),
	DECEIVER("Deceiver", RED + "", 5001),
	TRACER("Tracer", AQUA + "", 7501),
	AGENT("Agent", GOLD + "", 10001),
	SPY("Spy", YELLOW + "", 15001),
	CONSPIRATOR("Conspirator", LIGHT_PURPLE + "", 25001),
	EAVESDROPPER("Eavesdropper", DARK_AQUA + "", 35001),
	CONSTABLE("Constable", AQUA + "", 45001),
	OFFICER("Officer", YELLOW + "", 60001),
	SERGEANT("Sergeant", RED + "", 80001),
	COMMISSIONER("Commissioner", DARK_PURPLE + "", 100001),
	UNDERCOVER("Undercover", GOLD + "" + BOLD, 125001),
	LESTRADE("Lestrade", BLUE + "" + BOLD, 150001),
	WATSON("Watson", WHITE + "" + BOLD , 200001),
	SHERLOCK("✦ Sherlock", GOLD + "" + BOLD + "", -1);
	
	private String display;
	private String prefix;
	private int startKarma;
	
	
	TIMVRank(String display, String prefix, int startKarma){
		this.display = display;
		this.prefix = prefix;
		this.startKarma = startKarma;
	}

	public static TIMVRank getFromDisplay(String display){
		for(TIMVRank rank : TIMVRank.values()){
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
		return startKarma;
	}
	
	public String getKarmaToNextRank(int karma){
		if(this == SHERLOCK) return "Leaderboard Rank";
		if(this == WATSON) return "Highest Rank";
		ArrayList<TIMVRank> ranks = new ArrayList<TIMVRank>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		TIMVRank next = null;
		try{
			next = ranks.get(newIndex);
			
		}catch(Exception e){
			return "";
		}
		
		
		
		return next.getStart() - karma + " to " + next.prefix + next.getTotalDisplay();
	}
	
}
