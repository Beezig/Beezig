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

package eu.beezig.core.hiveapi.stuff.gnt;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.the5zig.mod.The5zigAPI;

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
    HERCULES("Hercules", "§2§l", 175000),
    BIGFRIENDLYGIANT("Big Friendly Giant", "§6§l", 250000),
    GARGANTUA("Gargantua", "§d§l", 375000),
    GULLIVER("Gulliver", "§b§l", 500000),
    THOR("Thor", "§e§l", 675000),
    BIGFOOT("Bigfoot", "§a§l", 750000),
    YETI("Yeti", "§5§l", 875000),
    TITAN("Titan", "§c§l", 1000000),
    HADES("Hades", "§9§l", 1250000),
    HAGRID("Hagrid", "§3§l", 1500000),
    ANAKIM("Anakim", "§6§l", 1750000),
    GOLIATH("Goliath", "§c§l", 2000000),
    SKYGIANT("✹ Giant Slayer", "§4§l", -1);

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
