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
import static eu.the5zig.util.minecraft.ChatColor.WHITE;

public enum TIMVRank {

	CIVILIAN("Civilian", GRAY + ""),
	INSPECTOR("Inspector", DARK_AQUA + ""),
	WITNESS("Witness", AQUA + ""),
	SCOUT("Scout", LIGHT_PURPLE + ""),
	FORENSIC("Forensic", GOLD + ""),
	TRUSTABLE("Trustable", YELLOW + ""),
	DECEIVER("Deceiver", RED + ""),
	TRACER("Tracer", AQUA + ""),
	AGENT("Agent", GOLD + ""),
	SPY("Spy", YELLOW + ""),
	CONSPIRATOR("Conspirator", LIGHT_PURPLE + ""),
	EAVESDROPPER("Eavesdropper", DARK_AQUA + ""),
	CONSTABLE("Constable", AQUA + ""),
	OFFICER("Officer", YELLOW + ""),
	SERGEANT("Sergeant", RED + ""),
	COMMISSIONER("Commissioner", DARK_PURPLE + ""),
	UNDERCOVER("Undercover", GOLD + "" + BOLD),
	LESTRADE("Lestrade", BLUE + "" + BOLD),
	WATSON("Watson", WHITE + "" + BOLD ),
	SHERLOCK("✦ Sherlock", GOLD + "" + BOLD + "");
	
	private String display;
	private String prefix;
	
	
	TIMVRank(String display, String prefix){
		this.display = display;
		this.prefix = prefix;
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
}
