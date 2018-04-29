package tk.roccodev.zta.hiveapi.wrapper;

import static eu.the5zig.util.minecraft.ChatColor.AQUA;
import static eu.the5zig.util.minecraft.ChatColor.BLUE;
import static eu.the5zig.util.minecraft.ChatColor.DARK_AQUA;
import static eu.the5zig.util.minecraft.ChatColor.DARK_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.DARK_RED;
import static eu.the5zig.util.minecraft.ChatColor.GRAY;
import static eu.the5zig.util.minecraft.ChatColor.GREEN;
import static eu.the5zig.util.minecraft.ChatColor.LIGHT_PURPLE;
import static eu.the5zig.util.minecraft.ChatColor.RED;
import static eu.the5zig.util.minecraft.ChatColor.YELLOW;

import eu.the5zig.util.minecraft.ChatColor;

public enum NetworkRank {

	REGULAR("Regular", BLUE, 0),

	GOLD("Gold Premium", ChatColor.GOLD, 10),

	DIAMOND("Diamond Premium", AQUA, 20),

	EMERALD("Emerald Premium", GREEN, 30),

	ULTIMATE("Ultimate Premium", LIGHT_PURPLE, 40),

	VIP("VIP", DARK_PURPLE, 50),
	YOUTUBER("YouTuber", DARK_PURPLE, 51),
	STREAMER("Streamer", DARK_PURPLE, 52),
	CONTRIBUTOR("Contributor", DARK_PURPLE, 53),

	NECTAR("Team Nectar", DARK_AQUA, 54),

	RESERVERD_STAFF("Reserved Staff", null, 60),

	MODERATOR("Moderator", RED, 70),

	SENIOR_MODERATOR("Senior Moderator", DARK_RED, 80),
	STAFFMANAGER("Staff Manager", DARK_RED, 81),

	DEVELOPER("Developer", GRAY, 90),

	OWNER("Owner", YELLOW, 100);
	
	
	private String display;
	private ChatColor color;
	private Integer level;
	
	NetworkRank(String display, ChatColor color, Integer level){
		this.display = display;
		this.color = color;
		this.level = level;
	}

	public String getDisplay() {
		return display;
	}

	public ChatColor getColor() {
		return color;
	}

	public Integer getLevel() {
		return level;
	}
	
	public static NetworkRank fromDisplay(String s){
		for(NetworkRank rank : values()){
			if(rank.getDisplay().equalsIgnoreCase(s)) return rank;
		}
		return null;
	}

	public static NetworkRank fromColor(ChatColor cc){
		for(NetworkRank rank : values()){
			if(rank.getColor() == cc) return rank;
		}
		return null;
	}
	
	
}
