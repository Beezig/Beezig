package tk.roccodev.zta.hiveapi.stuff.bp;

import java.util.ArrayList;
import java.util.Arrays;

public enum BPRank {

	FIRST_STEP("First Step", "§7", 0),
	PARTYANIMAL("Party Animal", "§6", 100),
	BALLERINA("Ballerina", "§3", 500),
	RAVER("Raver", "§d", 1000),
	FREESTYLER("Freestyler", "§b", 2500),
	BREAKDANCER("Breakdancer", "§5", 5000),
	STAR("Star", "§e", 10000),
	MCHAMMER("MC Hammer", "§a", 20000),
	CARLTON("Carlton", "§c", 35000),
	DESTROYER("Destroyer", "§9", 50000),
	FAMOUS("Famous", "§d", 75000),
	DOMINATOR("Dominator", "§5", 100000),
	FABULOUS("Fabulous", "§3", 150000),
	KING("King of Dance", "§6§l", 200000),
	CHOREOGRAPHER("Choreographer", "§b§l", 300000),
	HAPPY_FEET("Happy Feet", "§v§l", 400000),
	JACKSON("Jackson", "§e§l", 500000),
	ASTAIRE("Astaire", "§9§l", 625000),
	SWAYZE("Swayze", "§a§l", 750000),
	LEGENDARY("Legendary", "§5§l", 1000000),
	BILLYELLIOT("Billy Elliot", "§d§l", -1);
	
	private String display, prefix;
	private int start;
	
	BPRank(String display, String prefix, int start){
		this.display = display;
		this.prefix = prefix;
		this.start = start;
	}

	public String getDisplay() {
		return display;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getStart() {
		return start;
	}
	
	public String getTotalDisplay() {
		return prefix + display;
	}
	
	public static BPRank getFromDisplay(String display){
		for(BPRank rank : BPRank.values()){
			if(rank.getDisplay().equalsIgnoreCase(display)) return rank;
		}
		return null;
	}
	
	public String getPointsToNextRank(int points){
		if(this == LEGENDARY) return "Leaderboard Rank";
		if(this == BILLYELLIOT) return "Highest Rank";
		ArrayList<BPRank> ranks = new ArrayList<>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		BPRank next;
		try{
			next = ranks.get(newIndex);
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
		
		
		return next.prefix + (next.getStart() - points) + " to " +  next.getTotalDisplay();
	}
	
}
