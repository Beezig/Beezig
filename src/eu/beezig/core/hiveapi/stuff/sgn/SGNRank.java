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

package eu.beezig.core.hiveapi.stuff.sgn;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public enum SGNRank implements RankEnum {

    STRAY("Stray", "§7", 0),
    WANDERER("Wanderer", "§3", 1000),
    DEFENDER("Defender", "§b", 1500),
    SOLDIER("Soldier", "§6", 3000),
    CHAMPION("Champion", "§e", 6000),
    SARGEANT("Sargeant", "§a", 10000),
    WAR_HERO("War Hero", "§c", 15000),
    VETERAN("Veteran", ChatColor.LIGHT_PURPLE + "", 20000),
    GUARDIAN("Guardian", "§b§l", 30000),
    ELITE("Elite", "§a§l", 40000);


    private String display, prefix;
    private int requiredPoints;

    SGNRank(String display, String prefix, int requiredPoints) {
        this.display = display;
        this.prefix = prefix;
        this.requiredPoints = requiredPoints;
    }

    public static SGNRank getRank(long points) {
        ArrayList<SGNRank> ranks = new ArrayList<>(Arrays.asList(values()));
        Collections.reverse(ranks);
        for (SGNRank rank : ranks) {
            if (rank.requiredPoints != -1 && rank.requiredPoints <= points) {
                //Rank found
                return rank;

            }

        }
        return null;

    }

    public String getDisplay() {
        return display;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public String getTotalDisplay() {
        return prefix + display;
    }

    public String getPointsToNextRank(int points) {
        if (this == ELITE) return "Highest Rank";
        ArrayList<SGNRank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        SGNRank next;
        try {
            next = ranks.get(newIndex);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + Log.df(next.getRequiredPoints() - points), next.getTotalDisplay());
    }


}
