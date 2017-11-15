package tk.roccodev.zta.hiveapi.stuff.gnt;
import static eu.the5zig.util.minecraft.ChatColor.AQUA;
import static eu.the5zig.util.minecraft.ChatColor.GOLD;
import static eu.the5zig.util.minecraft.ChatColor.GRAY;
import static eu.the5zig.util.minecraft.ChatColor.GREEN;
import static eu.the5zig.util.minecraft.ChatColor.RED;
import static eu.the5zig.util.minecraft.ChatColor.YELLOW;

public enum GiantRank {
	
	DWARF("Dwarf", GRAY + ""),
	LITTLEJOHN("Little John", GOLD + ""),
	GENTLEGIANT("Gentle Giant", "§d"),
	COLOSSAL("Colossal", AQUA + ""),
	GALACTUS("Galactus", YELLOW + ""),
	BEHEMOTH("Behemoth", GREEN + ""),
	GRAWP("Grawp", RED + ""),
	ANDRE("Andre", "§9"),
	CYCLOPS("Cyclops", "§5"),
	BIGFRIENDLYGIANT("Big Friendly Giant", "§6§l"),
	GULLIVER("Gulliver", "§b§l"),
	BIGFOOT("Bigfoot", "§e§l"),
	TITAN("Titan", "§a§l"),
	HAGRID("Hagrid", "§c§l"),
	GOLIATH("Goliath", "§c§l"),
	SKYGIANT("✹ Sky Giant", "§d§l");
	
	private String display, prefix;
	
	GiantRank(String display, String prefix){
		this.display = display;
		this.prefix = prefix;
	}
	
	public static GiantRank getFromDisplay(String display){
		for(GiantRank rank : GiantRank.values()){
			if(rank.getDisplay().equalsIgnoreCase(display)) return rank;
		}
		return null;
	}	

	public String getDisplay() {
		return display;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public String getTotalDisplay(){
		return prefix + display;
	}

}
