package tk.roccodev.zta.hiveapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import eu.the5zig.util.minecraft.ChatColor;

public enum BEDRank {

	SLEEPY("§7Sleepy", 0, 100, 300, 600, 1000),
	SNOOZER(ChatColor.BLUE +"Snoozer", 1500, 2100, 2800, 3600, 4500),
	DROWSY("§eDrowsy", 5500, 6600, 7800, 9100, 10500),
	SLOTH("Sloth", 12000, 13600, 15300, 17100, 19000),
	HYPNOTIST(ChatColor.LIGHT_PURPLE + "Hypnotist", 21000, 23100, 25300, 27600, 30000),
	SIESTA("Siesta", 32500, 35100, 37800, 40600, 43500),
	DREAMER("Dreamer", 46500, 49600, 52800, 56100, 59500),
	SLEEP_WALKER("Sleep Walker", 63000, 66600, 70300, 74100, 78000),
	HIBERNATOR("Hibernator", 82000, 86100, 90300, 94600, 99000),
	BED_HEAD("Bed Head", 103500, 108100, 112800, 117600, 122500),
	PANDA("Panda", 127500, 132600, 137800, 143100, 148500),
	INSOMNIAC("Insomniac", 154000, 159600, 165300, 171100, 177000),
	WELL_RESTED("Well Rested", 183000, 189100, 195300, 201600, 208000),
	KOALA("Koala", 214500, 221100, 227800, 234600, 241500),
	DAY_DREAMER("Day Dreamer", 248500, 255600, 262800, 270100, 277500),
	POWER_NAP("Power Nap", 285000, 292600, 300300, 308100, 316000),
	BEAR("Bear", 324000, 332100, 340300, 348600, 357000),
	BED_WARRIOR("Bed Warrior", 365500, 374100, 382800, 391600, 400500),
	SNORLAX("Snorlax", 409500, 418600, 427800, 437100, 446500),
	NIGHTMARE("Nightmare", 456000, 465600, 475300, 485100, 495000),
	ZZZZZZ("Zzzzzz", -1, -1, -1, -1, -1);
	
	
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
		
		ArrayList<BEDRank> ranks = new ArrayList<BEDRank>(Arrays.asList(values()));
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
	
	
	
	
}
