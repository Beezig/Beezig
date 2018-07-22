package tk.roccodev.beezig.hiveapi.stuff.lab;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.hiveapi.stuff.RankEnum;

import java.util.ArrayList;
import java.util.Arrays;

public enum LABRank implements RankEnum {

    TESTTUBE("Test Tube", "§7", 0),
    MICROSCOPIC("Microscopic", "§6", 20),
    ION("Ion", "§b", 50),
    PHOTON("Photon", "§d", 100),
    THERMAL("Thermal", "§e", 150),
    ATOMIC("Atomic", "§6", 200),
    CORROSIVE("Corrosive", "§c", 300),
    ELEMENTAL("Elemental", "§a", 500),
    MOLECULAR("Molecular", "§b", 750),
    CATALYST("Catalyst", "§e", 1000),
    ACIDIC("Acidic", "§c", 1500),
    ENZYME("Enzyme", "§a", 2000),
    GAMMA("Gamma", "§d", 3000),
    ISOTOPIC("Isotopic", "§b", 4000),
    ELECTRONIC("Electronic", "§5", 6000),
    METALLIC("Metallic", "§e", 8000),
    RADIOACTIVE("Radioactive", "§a", 10000),
    NUCLEAR("Nuclear", "§c", 12500),
    DARWIN("Darwin", "§b§l", 15000),
    DAVINCI("da Vinci", "§a§l", 20000),
    SAGAN("Sagan", "§6§l", 25000),
    NEWTON("Newton", "§e§l", 30000),
    TESLA("Tesla", "§7§l", 35000),
    GALILEO("Galileo", "§5§l", 40000),
    HAWKING("Hawking", "§c§l", 50000),
    EINSTEIN("Einstein", "§f§l", -1);

    private String display, prefix;
    private int start;

    LABRank(String display, String prefix, int start) {
        this.display = display;
        this.prefix = prefix;
        this.start = start;
    }

    public static LABRank getFromDisplay(String display) {
        for (LABRank rank : LABRank.values()) {
            if (rank.getDisplay().equalsIgnoreCase(display)) return rank;
        }
        return null;
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

    public String getPointsToNextRank(int points) {
        if (this == EINSTEIN) return "Leaderboard Rank";
        if (this == HAWKING) return "Highest Rank";
        ArrayList<LABRank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        LABRank next;
        try {
            next = ranks.get(newIndex);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + (next.getStart() - points), next.getTotalDisplay());
    }

}
