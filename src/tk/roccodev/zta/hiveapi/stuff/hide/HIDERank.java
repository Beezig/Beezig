package tk.roccodev.zta.hiveapi.stuff.hide;

import static eu.the5zig.util.minecraft.ChatColor.AQUA;
import static eu.the5zig.util.minecraft.ChatColor.BLACK;
import static eu.the5zig.util.minecraft.ChatColor.BOLD;
import static eu.the5zig.util.minecraft.ChatColor.DARK_AQUA;
import static eu.the5zig.util.minecraft.ChatColor.DARK_BLUE;
import static eu.the5zig.util.minecraft.ChatColor.DARK_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.DARK_RED;
import static eu.the5zig.util.minecraft.ChatColor.GOLD;
import static eu.the5zig.util.minecraft.ChatColor.GRAY;
import static eu.the5zig.util.minecraft.ChatColor.LIGHT_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.MAGIC;
import static eu.the5zig.util.minecraft.ChatColor.RED;
import static eu.the5zig.util.minecraft.ChatColor.WHITE;
import static eu.the5zig.util.minecraft.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;

public enum HIDERank {

	BLIND("Blind", GRAY + "" , 0),
	SHORT_SIGHTED("Short Sighted", DARK_AQUA + "" , 100),
	SNEAKER("Sneaker", AQUA + "" , 1000),
	SNEAKY("Sneaky", LIGHT_PURPLE + "" , 2500),
	MYSTERIOUS("Mysterious", GOLD + "" , 10000),
	CAMOUFLAGED("Camouflaged", YELLOW + "" , 20000),
	CHAMELEON("Chameleon", RED + "" , 30000),
	STEALTHY("Stealthy", AQUA + "" , 40000),
	HUNTER("Hunter", YELLOW + "" , 75000),
	MAGICIAN("Magician", LIGHT_PURPLE + "" , 100000),
	ESCAPIST("Escapist", DARK_AQUA + "" , 150000),
	INVISIBLE("Invisible", DARK_BLUE + "" , 300000),
	SHADOW("Shadow", DARK_PURPLE + "" , 500000),
	HOUDINI("Houdini", AQUA + "" + BOLD + "" , 1000000),
	NINJA("Ninja", BLACK + "" + BOLD + "" , 1750000),
	WALLY("Wally", DARK_RED + "" + BOLD + "", 2500000),
	GHOST("Ghost", WHITE + "" + BOLD + "", 4000000),
	MASTER_OF_DISGUISE("Master of Disguise",  BOLD + "" + MAGIC + "" , -1);


	private String display;
	private String prefix;
	private int startPoints;

	HIDERank(String display, String prefix, int startPoints){
		this.display = display;
		this.prefix = prefix;
		this.startPoints = startPoints;
	}

	public static HIDERank getFromDisplay(String display){
		for(HIDERank rank : HIDERank.values()){
			if(rank.getDisplay().equalsIgnoreCase(display)) return rank;
		}
		return null;
	}


	public String getDisplay() {
		return display;
	}

	public String getTotalDisplay(){
		if(this == MASTER_OF_DISGUISE){
			return "§e§lMaster §a§lof §b§lDisguise";
		}
		return prefix + display;
	}

	public int getStart(){
		return startPoints;
	}

	public String getPointsToNextRank(int points){
		if(this == MASTER_OF_DISGUISE) return "Leaderboard Rank";
		if(this == GHOST) return "Highest Rank";
		ArrayList<HIDERank> ranks = new ArrayList<HIDERank>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		HIDERank next = null;
		try{
			next = ranks.get(newIndex);
		} catch(Exception e){ return "";}

		return next.prefix + "" + (next.getStart() - points) + " to " + next.getTotalDisplay();
	}
	
}
