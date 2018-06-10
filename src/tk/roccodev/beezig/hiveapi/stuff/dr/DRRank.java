package tk.roccodev.beezig.hiveapi.stuff.dr;

import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.Arrays;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum DRRank {

    SNAIL("Snail", GRAY + "", 0),
    TURTLE("Turtle", GOLD + "", 101),
    JOGGER("Jogger", LIGHT_PURPLE + "", 1001),
    RUNNER("Runner", AQUA + "", 3501),
    RABBIT("Rabbit", YELLOW + "", 10001),
    KANGAROO("Kangaroo", GREEN + "", 20001),
    SPEEDSTER("Speedster", RED + "", 35001),
    BLUR("Blur", BLUE + "", 60001),
    CHEETAH("Cheetah", DARK_PURPLE + "", 100001),
    FLASH("Flash", GOLD + "" + BOLD, 200001),
    SPEEDY_GONZALES("Speedy Gonzales", LIGHT_PURPLE + "" + BOLD, 300001),
    BOLT("Bolt", AQUA + "" + BOLD, 400001),
    FORREST("Forrest", YELLOW + "" + BOLD, 500001),
    SONIC("Sonic", RED + "" + BOLD, 600001),
    FALCON("Falcon", GREEN + "" + BOLD, 700001),
    ROAD_RUNNER("Road Runner", RED + "" + BOLD, 800001),
    BASILISK("Basilisk", BLUE + "" + BOLD, 950001),
    SAILFFISH("Sailfish", DARK_PURPLE + "" + BOLD, 1200001),
    FORMULA_1("Formula 1", GOLD + "" + BOLD, 1500001),
    TORNADO("Tornado", LIGHT_PURPLE + "" + BOLD, 2000001),
    JET("Jet", AQUA + "" + BOLD, 2500001),
    BLACKBIRD("Blackbird", YELLOW + "" + BOLD, 3500001),
    HYPERSPACE("Hyperspace", DARK_BLUE + "" + BOLD, 5000001),
    SPEED_OF_LIGHT("Speed of Light", RED + "" + BOLD + "", -1);

    private String display;
    private String prefix;
    private int startPoints;

    DRRank(String display, String prefix, int startPoints) {
        this.display = display;
        this.prefix = prefix;
        this.startPoints = startPoints;
    }

    public static DRRank getFromDisplay(String display) {
        for (DRRank rank : DRRank.values()) {
            if (rank.getDisplay().equalsIgnoreCase(display)) return rank;
        }
        return null;
    }


    public String getDisplay() {
        return display;
    }

    public String getTotalDisplay() {
        return prefix + display;
    }

    public int getStart() {
        return startPoints;
    }

    public String getPointsToNextRank(int points) {
        if (this == SPEED_OF_LIGHT) return "Leaderboard Rank";
        if (this == HYPERSPACE) return "Highest Rank";
        ArrayList<DRRank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        DRRank next;
        try {
            next = ranks.get(newIndex);
        } catch (Exception e) {
            return "";
        }

        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + (next.getStart() - points), next.getTotalDisplay());

    }
}
