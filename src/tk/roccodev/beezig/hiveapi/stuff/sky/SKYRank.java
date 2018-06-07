package tk.roccodev.beezig.hiveapi.stuff.sky;

import java.util.ArrayList;
import java.util.Arrays;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum SKYRank {

	BRAWLER("Brawler", GRAY + "", 0),
	DEFENDER("Defender", GOLD + "", 250),
	SKYLANDER("Skylander", LIGHT_PURPLE + "", 750),
	WARRIOR("Warrior", AQUA + "", 1250),
	GLADIATOR("Gladiator", YELLOW + "", 2500),
	VETERAN("Veteran", GREEN + "", 5000),
	VOYAGER("Voyager", RED + "", 10000),
	WARLORD("Warlord", BLUE + "", 25000),
	WARMONGER("Warmonger", DARK_PURPLE + "", 50000),
	KEEPER("Keeper", AQUA + "", 75000),
	OVERSEER("Overseer", GOLD + "" + BOLD, 100000),
	HERO("Hero", AQUA + "" + BOLD, 125000),
	GUARDIAN("Guardian", YELLOW + "" + BOLD, 150000),
	PALADIN("Paladin",  GREEN + "" + BOLD, 175000),
	CHAMPION("Champion", RED + "" + BOLD, 200000),
	ZEUS("Zeus", YELLOW + "" + BOLD, 225000),
	GODLY("Godly", GOLD + "" + BOLD, 250000),
	SKYLORD("✹ Skylord", LIGHT_PURPLE + "" + BOLD, -1);
	
	private String display;
	private String prefix;
	private int startPoints;
	
	
	SKYRank(String display, String prefix, int startPts){
		this.display = display;
		this.prefix = prefix;
		this.startPoints = startPts;
	}

	public static SKYRank getFromDisplay(String display){
		for(SKYRank rank : SKYRank.values()){
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
		if(this == SKYLORD) return "Leaderboard Rank";
		if(this == GODLY) return "Highest Rank";
		ArrayList<SKYRank> ranks = new ArrayList<>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		SKYRank next;
		try{
			next = ranks.get(newIndex);
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
		
		
		return next.prefix + (next.getStart() - points) + " to " +  next.getTotalDisplay();
	}
	
}
