package tk.roccodev.beezig.hiveapi.stuff.grav;

import java.util.ArrayList;
import java.util.Arrays;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum GRAVRank {

	TUMBLER("Tumbler", GRAY + "", 0),
	FALLER("Faller", GOLD + "", 250),
	FREEFALLER("Freefaller", LIGHT_PURPLE + "", 1000),
	SLIDER("Slider", AQUA + "", 5000),
	SKY_DIVER("Sky Diver", YELLOW + "", 15000),
	BASE_JUMPER("Base Jumper", GREEN + "", 35000),
	SWERVER("Swerver", RED + "", 75000),
	DODGER("Dodger", DARK_BLUE + "", 100000),
	EVADER("Evader", DARK_PURPLE + "", 150000),

	EAGLE("Eagle", GOLD + "" + BOLD, 250000),
	SUPERMAN("Superman", AQUA + "" + BOLD, 350000),
	IRONMAN("Ironman", DARK_BLUE + "" + BOLD, 500000),
	GRAVITON("Graviton", GOLD + "" + BOLD, 750000),
	ORBITAL_FORCE("Orbital Force", LIGHT_PURPLE + "" + BOLD, 1000000),
	CHRONOLOCK("Chronolock",  AQUA + "" + BOLD, 1500000),
	GRAVITY_DEFIANCE("Gravity Defiance", YELLOW + "" + BOLD, 2500000),
	FELIX_BAUMGARTNER("Felix Baumgartner", LIGHT_PURPLE + "" + BOLD, -1);

	private String display;
	private String prefix;
	private int startPoints;

	GRAVRank(String display, String prefix, int startPts){
		this.display = display;
		this.prefix = prefix;
		this.startPoints = startPts;
	}

	public static GRAVRank getFromDisplay(String display){
		for(GRAVRank rank : GRAVRank.values()){
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
		if(this == FELIX_BAUMGARTNER) return "Leaderboard Rank";
		if(this == GRAVITY_DEFIANCE) return "Highest Rank";
		ArrayList<GRAVRank> ranks = new ArrayList<>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		GRAVRank next;
		try{
			next = ranks.get(newIndex);

		}catch(Exception e){
			e.printStackTrace();
			return "";
		}

		return next.prefix + (next.getStart() - points) + " to " +  next.getTotalDisplay();
	}

}
