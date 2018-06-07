package tk.roccodev.beezig.hiveapi.stuff.bed;

import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.hiveapi.HiveAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum BEDRank {

	SLEEPY(ChatColor.GRAY + "Sleepy", 0, 100, 300, 600, 1000),
	SNOOZER(ChatColor.BLUE +"Snoozer", 1500, 2100, 2800, 3600, 4500),
	DROWSY(ChatColor.YELLOW + "Drowsy", 5500, 6600, 7800, 9100, 10500),
	SLOTH(ChatColor.GOLD + "Sloth", 12000, 13600, 15300, 17100, 19000),
	HYPNOTIST(ChatColor.LIGHT_PURPLE + "Hypnotist", 21000, 23100, 25300, 27600, 30000),
	SIESTA(ChatColor.GREEN + "Siesta", 32500, 35100, 37800, 40600, 43500),
	DREAMER(ChatColor.AQUA + "Dreamer", 46500, 49600, 52800, 56100, 59500),
	SLEEP_WALKER(ChatColor.RED + "Sleep Walker", 63000, 66600, 70300, 74100, 78000),
	HIBERNATOR(ChatColor.DARK_AQUA + "Hibernator", 82000, 86100, 90300, 94600, 99000),
	
	BED_HEAD(ChatColor.YELLOW +""+ ChatColor.BOLD + "Bed Head", 103500, 108100, 112800, 117600, 122500),
	PANDA(ChatColor.GOLD +""+ ChatColor.BOLD + "Panda", 127500, 132600, 137800, 143100, 148500),
	INSOMNIAC(ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD + "Insomniac", 154000, 159600, 165300, 171100, 177000),
	WELL_RESTED(ChatColor.GREEN +""+ ChatColor.BOLD + "Well Rested", 183000, 189100, 195300, 201600, 208000),
	KOALA(ChatColor.AQUA +""+ ChatColor.BOLD + "Koala", 214500, 221100, 227800, 234600, 241500),
	DAY_DREAMER(ChatColor.RED +""+ ChatColor.BOLD + "Day Dreamer", 248500, 255600, 262800, 270100, 277500),
	
	POWER_NAP(ChatColor.YELLOW +""+ ChatColor.BOLD +""+ ChatColor.ITALIC + "Power Nap", 285000, 292600, 300300, 308100, 316000),
	BEAR(ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC + "Bear", 324000, 332100, 340300, 348600, 357000),
	BED_WARRIOR(ChatColor.AQUA +""+ ChatColor.BOLD +""+ ChatColor.ITALIC + "Bed Warrior", 365500, 374100, 382800, 391600, 400500),
	SNORLAX(ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD +""+ ChatColor.ITALIC + "Snorlax", 409500, 418600, 427800, 437100, 446500),
	SANDMAN(ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC + "Sandman", 456000, 465600, 475300, 485100, 495000),
	
	LULLABY(ChatColor.YELLOW +""+ ChatColor.BOLD +""+ ChatColor.UNDERLINE + "Lullaby", 505000, 515100, 525300, 535600, 546000),
	BED_BUG(ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.UNDERLINE + "Bed Bug", 556500, 567100, 577800, 588800, 600300),
	SLEEPING_LION(ChatColor.AQUA +""+ ChatColor.BOLD +""+ ChatColor.UNDERLINE + "Sleeping Lion", 612500, 625600, 639800, 655300, 672300),
	TRANQUILISED(ChatColor.LIGHT_PURPLE +""+ ChatColor.BOLD +""+ ChatColor.UNDERLINE + "Tranquilised", 691000, 711600, 734300, 759300, 786800),
	NIGHTMARE(ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.UNDERLINE + "Nightmare", 817000, 850100, 886300, 925800, 968800),
	
	ZZZZZZ(ChatColor.WHITE +""+ ChatColor.BOLD +"✸ Zzzzzz", -1, -1, -1, -1, -1);
	
	
	private String name;
	private int start, lvl4, lvl3, lvl2, lvl1;
	
	
	
	
	public String getName() {
		return name;
	}
	
	




	public int getStart() {
		return start;
	}




	public int getLvl4() {
		return lvl4;
	}




	public int getLvl3() {
		return lvl3;
	}




	public int getLvl2() {
		return lvl2;
	}




	public int getLvl1() {
		return lvl1;
	}




	BEDRank(String name, int start, int lvl4, int lvl3, int lvl2, int lvl1){
		this.name = name;
		this.start = start;
		this.lvl4 = lvl4;
		this.lvl3 = lvl3;
		this.lvl2 = lvl2;
		this.lvl1 = lvl1;
	}
	
	
	public static BEDRank getRank(long points){
		
		ArrayList<BEDRank> ranks = new ArrayList<>(Arrays.asList(values()));
		Collections.reverse(ranks);
		for(BEDRank rank : ranks){
			if(rank.getStart() != -1 && rank.getStart() <= points){
				//Rank found
				return rank;
				
				
				
			}
			
		}
		return null;
		
	}
	
	public int getLevel(int points){
		if(this == ZZZZZZ){
			return 0;
		}
		if(points >= getLvl1()){
			return 1;
		}
		else if(points >= getLvl2()){
			return 2;
		}
		else if(points >= getLvl3()){
			return 3;
		}
		else if(points >= getLvl4()){
			return 4;
		}
		else if(points >= getStart()){
			return 5;
		}
		return -1;
	}
	
	public String getPointsToNextRank(int points){
		
		return getPointsToNextRank(points, true);
		
		
	}
	
	public String getPointsToNextRank(int points, boolean withColor){
		if(this == ZZZZZZ){
			return "Highest Rank";
		}
		if(this == NIGHTMARE && this.getLevel(points) == 1){
			return "Highest obtainable rank";
		}
		int level = getLevel(points);
		if(level == 1){
			ArrayList<BEDRank> ranks = new ArrayList<>(Arrays.asList(values()));
			int newIndex = ranks.indexOf(this) + 1;
			BEDRank next;
			try{
				next = ranks.get(newIndex);
				
			}catch(Exception e){
				return "";
			}
			String color = withColor ? next.getName().replaceAll(ChatColor.stripColor(next.getName()), "") : "";
			
			
			return color + (next.getStart() - points) + " to " + BED.NUMBERS[5] +  " " + next.getName();
			
		}
		else if(level == 2){
			String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
			return getLvl1() - points + " to " + color + BED.NUMBERS[1] +  " " + getName();
		}
		else if(level == 3){
			String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
			return getLvl2() - points + " to " + color + BED.NUMBERS[2] +  " " + getName();
		}
		else if(level == 4){
			String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
			return getLvl3() - points + " to " + color + BED.NUMBERS[3] +  " " + getName();
		}
		else if(level == 5){
			String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
			return getLvl4() - points + " to " + color + BED.NUMBERS[4] +  " " + getName();
		}
		return null;
		
		
	}
	
	
	public static boolean isNo1(String ign){
		if(BED.lastRecordsPoints < 500000L){
			//Saved another API operation
			return false;
		}
		String no1 = HiveAPI.getLeaderboardsPlaceHolder(0, "BED");
		return no1.equalsIgnoreCase(ign);
	}
	
	
	
}
