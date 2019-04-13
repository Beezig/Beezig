/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.hiveapi.stuff.hide;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.Arrays;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum HIDERank implements RankEnum {

    BLIND("Blind", GRAY + "", 0),
    SHORT_SIGHTED("Short Sighted", DARK_AQUA + "", 100),
    SNEAKER("Sneaker", AQUA + "", 1000),
    SNEAKY("Sneaky", LIGHT_PURPLE + "", 2500),
    DECEPTIVE("Deceptive", "§6", 5000),
    MYSTERIOUS("Mysterious", YELLOW + "", 10000),
    DISGUISED("Disguised", "§a", 15000),
    CAMOUFLAGED("Camouflaged", DARK_GREEN + "", 20000),
    CHAMELEON("Chameleon", RED + "", 30000),
    STEALTHY("Stealthy", AQUA + "", 40000),
    MASKED("Masked", GOLD + "", 50000),
    HUNTER("Hunter", YELLOW + "", 75000),
    MAGICIAN("Magician", LIGHT_PURPLE + "", 100000),
    ESCAPIST("Escapist", DARK_AQUA + "", 150000),
    INVISIBLE("Invisible", DARK_BLUE + "", 300000),
    SHADOW("Shadow", DARK_PURPLE + "", 500000),
    HOUDINI("Houdini", AQUA + "" + BOLD + "", 1000000),
    NINJA("Ninja", DARK_GRAY + "" + BOLD + "", 1750000),
    WALLY("Wally", RED + "" + BOLD + "", 2500000),
    GHOST("Ghost", WHITE + "" + BOLD + "", 4000000),
    SILHOUETTE("Silhouette", "§3§l", 6000000),
    PHANTOM("Phantom", "§5§l", 8000000),
    VANISHED("Vanished", "§1§l", 10000000),
    MASTER_OF_DISGUISE("Master of Disguise", BOLD + "" + MAGIC + "", -1);


    private String display;
    private String prefix;
    private int startPoints;

    HIDERank(String display, String prefix, int startPoints) {
        this.display = display;
        this.prefix = prefix;
        this.startPoints = startPoints;
    }

    public static HIDERank getFromDisplay(String display) {
        for (HIDERank rank : HIDERank.values()) {
            if (rank.getDisplay().equalsIgnoreCase(display)) return rank;
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }


    public String getDisplay() {
        return display;
    }

    public String getTotalDisplay() {
        if (this == MASTER_OF_DISGUISE) {
            return "§a§lMaster §e§lof §b§lDisguise";
        }
        return prefix + display;
    }

    public int getStart() {
        return startPoints;
    }

    public String getPointsToNextRank(int points) {
        if (this == MASTER_OF_DISGUISE) return "Leaderboard Rank";
        if (this == GHOST) return "Highest Rank";
        ArrayList<HIDERank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        HIDERank next;
        try {
            next = ranks.get(newIndex);
        } catch (Exception e) {
            return "";
        }

        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + Log.df(next.getStart() - points), next.getTotalDisplay());
    }

}
