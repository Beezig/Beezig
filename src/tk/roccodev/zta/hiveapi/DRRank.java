package tk.roccodev.zta.hiveapi;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum DRRank {

	SNAIL("Snail", GRAY + ""),
	TURTLE("Turtle", GOLD + ""),
	JOGGER("Jogger", LIGHT_PURPLE + ""),
	RUNNER("Runner", AQUA + ""),
	RABBIT("Rabbit", YELLOW + ""),
	KANGAROO("Kangaroo", GREEN + ""),
	SPEEDSTER("Speedster", RED + ""),
	BLUR("Blur", BLUE + ""),
	CHEETAH("Cheetah", DARK_PURPLE + ""),
	FLASH("Flash", GOLD + "" + BOLD),
	SPEEDY_GONZALES("Speedy Gonzales", LIGHT_PURPLE + "" + BOLD),
	BOLT("Bolt", AQUA + "" + BOLD),
	FORREST("Forrest", YELLOW + "" + BOLD),
	SONIC("Sonic", RED + "" + BOLD),
	FALCON("Falcon", GREEN + "" + BOLD),
	ROAD_RUNNER("Road Runner", RED + "" + BOLD),
	BASILISK("Basilisk", BLUE + "" + BOLD),
	SAILFFISH("Sailfish", DARK_PURPLE + "" + BOLD),
	FORMULA_1("Formula 1", GOLD + "" + BOLD),
	TORNADO("Tornado", LIGHT_PURPLE + "" + BOLD ),
	JET("Jet", AQUA + "" + BOLD ),
	BLACKBIRD("Blackbird", YELLOW + "" + BOLD ),
	HYPERSPACE("Hyperspace", DARK_BLUE + "" + BOLD ),
	SPEED_OF_LIGHT("Speed of Light", RED + "" + BOLD + "");
	
	private String display;
	private String prefix;
	
	DRRank(String display, String prefix){
		this.display = display;
		this.prefix = prefix;
	}

	public static DRRank getFromDisplay(String display){
		for(DRRank rank : DRRank.values()){
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
