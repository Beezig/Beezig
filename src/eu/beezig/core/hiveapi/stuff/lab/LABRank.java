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

package eu.beezig.core.hiveapi.stuff.lab;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.the5zig.mod.The5zigAPI;

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


        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + Log.df(next.getStart() - points), next.getTotalDisplay());
    }

}
