package tk.roccodev.zta.hiveapi.wrapper;

import static eu.the5zig.util.minecraft.ChatColor.AQUA;
import static eu.the5zig.util.minecraft.ChatColor.BLUE;
import static eu.the5zig.util.minecraft.ChatColor.DARK_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.DARK_RED;
import static eu.the5zig.util.minecraft.ChatColor.GRAY;
import static eu.the5zig.util.minecraft.ChatColor.GREEN;
import static eu.the5zig.util.minecraft.ChatColor.RED;
import static eu.the5zig.util.minecraft.ChatColor.YELLOW;

import eu.the5zig.util.minecraft.ChatColor;

public enum NetworkRank {

	REGULAR("Regular Hive Member", BLUE),
	GOLD("Gold Hive Member", ChatColor.GOLD),
	DIAMOND("Diamond Hive Member", AQUA),
	EMERALD("Lifetime Emerald Hive Member", GREEN),
	VIP("VIP Player", DARK_PURPLE),
	MODERATOR("Hive Moderator", RED),
	SENIOR_MODERATOR("Senior Hive Moderator", DARK_RED),
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
