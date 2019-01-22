package eu.beezig.core.hiveapi.stuff.gnt;

import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;
import eu.beezig.core.hiveapi.stuff.RankEnum;

import java.util.ArrayList;
import java.util.Arrays;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum GiantRank implements RankEnum {

    DWARF("Dwarf", GRAY + "", 0),
    LITTLEJOHN("Little John", GOLD + "", 500),
    GENTLEGIANT("Gentle Giant", "§d", 1500),
    COLOSSAL("Colossal", AQUA + "", 2500),
    GALACTUS("Galactus", YELLOW + "", 5000),
    BEHEMOTH("Behemoth", GREEN + "", 10000),
    GRAWP("Grawp", RED + "", 25000),
    ANDRE("Andre", "§9", 50000),
    CYCLOPS("Cyclops", "§5", 100000),
    BIGFRIENDLYGIANT("Big Friendly Giant", "§6§l", 250000),
    GULLIVER("Gulliver", "§b§l", 500000),
    BIGFOOT("Bigfoot", "§e§l", 750000),
    TITAN("Titan", "§a§l", 1000000),
    HAGRID("Hagrid", "§c§l", 1500000),
    GOLIATH("Goliath", "§c§l", 2000000),
    SKYGIANT("✹ Sky Giant", "§d§l", -1);

    private String display, prefix;
    private int start;

    GiantRank(String display, String prefix, int start) {
        this.display = display;
        this.prefix = prefix;
        this.start = start;
    }

    public static GiantRank getFromDisplay(String display) {
        for (GiantRank rank : GiantRank.values()) {
            if (rank.getDisplay().equalsIgnoreCase(display)) return rank;
        }
        return null;
    }

    public int getStart() {
        return start;
    }

    public String getDisplay() {
        return display;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTotalDisplay() {
        return prefix + display;
    }

    public String getPointsToNextRank(int points) {
        if (this == SKYGIANT) return "Leaderboard Rank";
        if (this == GOLIATH) return "Highest Rank";
        ArrayList<GiantRank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        GiantRank next;
        try {
            next = ranks.get(newIndex);
        } catch (Exception e) {
            return "";
        }

        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + Log.df(next.getStart() - points), next.getTotalDisplay());
    }

}
