package tk.roccodev.beezig.hiveapi.stuff.timv;

import java.util.ArrayList;
import java.util.Arrays;

import eu.the5zig.mod.The5zigAPI;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum TIMVRank {

	CIVILIAN("Civilian", GRAY + "", 0),
	INSPECTOR("Inspector", DARK_AQUA + "", 105),
	WITNESS("Witness", AQUA + "", 505),
	SCOUT("Scout", LIGHT_PURPLE + "", 755),
	FORENSIC("Forensic", GOLD + "", 1005),
	TRUSTABLE("Trustable", YELLOW + "", 2005),
	DIRECTOR("Director", AQUA + "", 3505),
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
		ArrayList<TIMVRank> ranks = new ArrayList<>(Arrays.asList(values()));
		int newIndex = ranks.indexOf(this) + 1;
		TIMVRank next;
		try{
			next = ranks.get(newIndex);
			
		}catch(Exception e){
			return "";
		}
		
		
		
		return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + (next.getStart() - karma), next.getTotalDisplay());
	}
	
}
