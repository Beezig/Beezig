package tk.roccodev.beezig.hiveapi.stuff.mimv;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.hiveapi.stuff.RankEnum;

import java.util.ArrayList;
import java.util.Arrays;

public enum MIMVRank implements RankEnum {

    PACIFIST("Pacifist", "§7", 0),
    CITIZEN("Citizen", "§6", 150),
    OBSERVER("Observer", "§d", 1000),
    BYSTANDER("Bystander", "§b", 2250),
    BUTCHER("Butcher", "§e", 3500),
    BANDIT("Bandit", "§a", 5500),
    REBEL("Rebel", "§c", 8000),
    EXECUTIONER("Executioner", "§e", 11500),
    PSYCHO("Psycho", "§6", 18000),
    ASSASSIN("Assassin", "§5", 25000),
    HUNTSMAN("Huntsman", "§6§l", 35000),
    BOUNTY_HUNTER("Bounty Hunter", "§b§l", 55000),
    SHADOW("Shadow", "§c§l", 75000),
    ENFORCER("Enforcer", "§a§l", 100000),
    ALTAIR("Altair", "§e§l", 125000),
    EZIO("Ezio", "§d§l", 175000),
    MUCKDUCK("❖ Muckduck", "§5§l", -1);

    private String display, prefix;
    private int start;

    MIMVRank(String display, String prefix, int start) {
        this.display = display;
        this.prefix = prefix;
        this.start = start;
    }

    public static MIMVRank getFromDisplay(String display) {
        for (MIMVRank rank : MIMVRank.values()) {
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
        if (this == MUCKDUCK) return "Leaderboard Rank";
        if (this == EZIO) return "Highest Rank";
        ArrayList<MIMVRank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        MIMVRank next;
        try {
            next = ranks.get(newIndex);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + (next.getStart() - points), next.getTotalDisplay());
    }

}
