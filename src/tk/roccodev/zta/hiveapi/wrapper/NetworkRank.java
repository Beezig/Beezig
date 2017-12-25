package tk.roccodev.zta.hiveapi.wrapper;

import eu.the5zig.util.minecraft.ChatColor;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum NetworkRank {

	REGULAR("Regular", BLUE),

	GOLD("Gold Premium", ChatColor.GOLD),

	DIAMOND("Diamond Premium", AQUA),

	EMERALD("Emerald Premium", GREEN),

	ULTIMATE("Ultimate Premium", LIGHT_PURPLE),

	VIP("VIP", DARK_PURPLE),
	YOUTUBER("YouTuber", DARK_PURPLE),
	STREAMER("Streamer", DARK_PURPLE),
	CONTRIBUTOR("Contributor", DARK_PURPLE),
	NECTAR("Team Nectar", DARK_PURPLE),

	RESERVERD_STAFF("Reserved Staff", null),

	MODERATOR("Moderator", RED),

	SENIOR_MODERATOR("Senior Moderator", DARK_RED),
	STAFFMANAGER("Staff Manager", DARK_RED),

	DEVELOPER("Hive Developer", GRAY),

	OWNER("Hive Founder and Owner", YELLOW);
	
	
	private String display;
	private ChatColor color;
	
	NetworkRank(String display, ChatColor color){
		this.display = display;
		this.color = color;
	}

	public String getDisplay() {
		return display;
	}

	public ChatColor getColor() {
		return color;
	}
	
	public static NetworkRank fromDisplay(String s){
		for(NetworkRank rank : values()){
			if(rank.getDisplay().equalsIgnoreCase(s)) return rank;
		}
		return null;
	}
	
	
}
