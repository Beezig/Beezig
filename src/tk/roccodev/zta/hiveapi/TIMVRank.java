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
	INSPECTOR("Inspector", DARK_AQUA + "", 105),
	WITNESS("Witness", AQUA + "", 505),
	SCOUT("Scout", LIGHT_PURPLE + "", 755),
	FORENSIC("Forensic", GOLD + "", 1005),
	TRUSTABLE("Trustable", YELLOW + "", 2005),
	DECEIVER("Deceiver", RED + "", 5005),
	TRACER("Tracer", AQUA + "", 7505),
	AGENT("Agent", GOLD + "", 10005),
	SPY("Spy", YELLOW + "", 15005),
	CONSPIRATOR("Conspirator", LIGHT_PURPLE + "", 25005),
	EAVESDROPPER("Eavesdropper", DARK_AQUA + "", 35005),
	CONSTABLE("Constable", AQUA + "", 45005),
	OFFICER("Officer", YELLOW + "", 60005),
	SERGEANT("Sergeant", RED + "", 80005),
	COMMISSIONER("Commissioner", DARK_PURPLE + "", 100005),
	UNDERCOVER("Undercover", GOLD + "" + BOLD, 125005),
	LESTRADE("Lestrade", BLUE + "" + BOLD, 150005),
	WATSON("Watson", WHITE + "" + BOLD , 200005),
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
