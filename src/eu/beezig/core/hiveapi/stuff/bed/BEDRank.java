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

package eu.beezig.core.hiveapi.stuff.bed;

import eu.beezig.core.Log;
import eu.beezig.core.games.BED;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.game.Game;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.BedStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum BEDRank implements RankEnum {

    SLEEPY(ChatColor.GRAY + "Sleepy", 0, 100, 300, 600, 1000),
    SNOOZER(ChatColor.BLUE + "Snoozer", 1500, 2100, 2800, 3600, 4500),
    DROWSY(ChatColor.YELLOW + "Drowsy", 5500, 6600, 7800, 9100, 10500),
    SLOTH(ChatColor.GOLD + "Sloth", 12000, 13600, 15300, 17100, 19000),
    HYPNOTIST(ChatColor.LIGHT_PURPLE + "Hypnotist", 21000, 23100, 25300, 27600, 30000),
    SIESTA(ChatColor.GREEN + "Siesta", 32500, 35100, 37800, 40600, 43500),
    DREAMER(ChatColor.AQUA + "Dreamer", 46500, 49600, 52800, 56100, 59500),
    SLEEP_WALKER(ChatColor.RED + "Sleep Walker", 63000, 66600, 70300, 74100, 78000),
    HIBERNATOR(ChatColor.DARK_AQUA + "Hibernator", 82000, 86100, 90300, 94600, 99000),

    BED_HEAD(ChatColor.YELLOW + "Bed Head", 103500, 108100, 112800, 117600, 122500),
    PANDA(ChatColor.DARK_PURPLE + "Panda", 127500, 132600, 137800, 143100, 148500),
    INSOMNIAC(ChatColor.BLUE + "Insomniac", 154000, 159600, 165300, 171100, 177000),
    WELL_RESTED(ChatColor.GREEN + "Well Rested", 183000, 189100, 195300, 201600, 208000),
    KOALA(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Koala", 214500, 221100, 227800, 234600, 241500),
    DAY_DREAMER(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Day Dreamer", 248500, 255600, 262800, 270100, 277500),

    POWER_NAP(ChatColor.GOLD + "" + ChatColor.BOLD + "Power Nap", 285000, 292600, 300300, 308100, 316000),
    BEAR(ChatColor.AQUA + "" + ChatColor.BOLD + "Bear", 324000, 332100, 340300, 348600, 357000),
    BED_WARRIOR(ChatColor.RED + "" + ChatColor.BOLD + "Bed Warrior", 365500, 374100, 382800, 391600, 400500),
    SNORLAX(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Snorlax", 409500, 418600, 427800, 437100, 446500),
    SANDMAN(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sandman", 456000, 465600, 475300, 485100, 495000),

    LULLABY(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Lullaby", 505000, 515100, 525300, 535600, 546000),
    BED_BUG(ChatColor.GREEN + "" + ChatColor.BOLD + "Bed Bug", 556500, 567100, 577800, 588800, 600300),
    SLEEPING_LION(ChatColor.GOLD + "" + ChatColor.BOLD + "Sleeping Lion", 612500, 625600, 639800, 655300, 672300),
    TRANQUILISED(ChatColor.AQUA + "" + ChatColor.BOLD + "Tranquilised", 691000, 711600, 734300, 759300, 786800),
    SLEEPLESS(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Sleepless", 817000, 850100, 886300, 925800, 968800),

    DREAM_CATCHER(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Dream Catcher", 1015500, 1066100, 1120800, 1179800, 1243300),
    MORPHEUS(ChatColor.BLUE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Morpheus", 1311500, 1384600, 1462800, 1546300, 1635300),
    SLEEPING_BEAUTY(ChatColor.GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Sleeping Beauty",
            1730000, 1830600, 1937300, 2050300, 2169800),
    ETERNAL_REST(ChatColor.GRAY + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Eternal Rest",
            2296000, 2429100, 2569300, 2716800, 2871800),

    NIGHTMARE(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Nightmare", 3034500, 3205100, 3383800, 3570800, 3766300),
    NIGHTMARE0(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "" + ChatColor.UNDERLINE + "Nightmare", 4000000, -1, -1, -1, -1),

    ZZZZZZ(ChatColor.WHITE + "" + ChatColor.BOLD + "✸ Zzzzzz", -1, -1, -1, -1, -1);


    private String name;
    private int cachedPts;
    private int start, lvl4, lvl3, lvl2, lvl1;


    BEDRank(String name, int start, int lvl4, int lvl3, int lvl2, int lvl1) {
        this.name = name;
        this.start = start;
        this.lvl4 = lvl4;
        this.lvl3 = lvl3;
        this.lvl2 = lvl2;
        this.lvl1 = lvl1;
    }

    public static BEDRank getRank(long points) {

        ArrayList<BEDRank> ranks = new ArrayList<>(Arrays.asList(values()));
        Collections.reverse(ranks);
        for (BEDRank rank : ranks) {
            if (rank.getStart() != -1 && rank.getStart() <= points) {
                //Rank found
                rank.cachedPts = Math.toIntExact(points);
                return rank;


            }

        }
        return null;

    }

    public static boolean newIsNo1(BedStats stats) {
        return newIsNo1(stats.getTitle(), stats.getPoints());
    }

    public static boolean newIsNo1(String title, long points) {
        return title.startsWith("Sleepy ") && points > 1500L;
    }

    public static boolean isNo1(String ign) {
        if (BED.lastRecordsPoints < 2000000L) {
            //Saved another API operation
            return false;
        }
        String no1 = (String) new Game("BED").getLeaderboard(0, 1).getPlayers().get(0).get("username");
        return no1.equalsIgnoreCase(ign);
    }

    public static boolean isNo1(String ign, long validate) {
        if (validate < 2000000L) return false;
        String no1 = (String) new Game("BED").getLeaderboard(0, 1).getPlayers().get(0).get("UUID");
        return no1.equalsIgnoreCase(ign);
    }

    public static BEDRank getFromDisplay(String display) {
        for (BEDRank rank : BEDRank.values()) {
            String group = display.replaceAll("\\d", "").trim();
            if (ChatColor.stripColor(rank.getDisplay()).equals(group)) return rank;
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public String getDisplay() {
        return name;
    }

    @Override
    public String getTotalDisplay() {
        if (this == ZZZZZZ || this == NIGHTMARE0) {
            return getName();
        } else {
            return getName().replace(ChatColor.stripColor(getName()), "") + BED.NUMBERS[getLevel(cachedPts)] + " " + ChatColor.stripColor(name);
        }

    }

    public String getName() {
        return name;
    }

    public int getStart() {
        return start;
    }

    public int getLvl4() {
        return lvl4;
    }

    public int getLvl3() {
        return lvl3;
    }

    public int getLvl2() {
        return lvl2;
    }

    public int getLvl1() {
        return lvl1;
    }

    public int getLevel(int points) {
        if (this == ZZZZZZ || this == NIGHTMARE0) {
            return 0;
        }
        if (points >= getLvl1()) {
            return 1;
        } else if (points >= getLvl2()) {
            return 2;
        } else if (points >= getLvl3()) {
            return 3;
        } else if (points >= getLvl4()) {
            return 4;
        } else if (points >= getStart()) {
            return 5;
        }
        return -1;
    }

    public String getPointsToNextRank(int points) {

        return getPointsToNextRank(points, true);


    }

    public String rankStringForge(String level) {
        int lvl = Integer.parseInt(level);
        String color = getName().replaceAll(ChatColor.stripColor(getName()), "");
        return color + BED.NUMBERS[lvl] + " " + getName();
    }

    public String getPointsToNextRank(int points, boolean withColor) {

        if (this == ZZZZZZ) {
            return "Highest Rank";
        }
        if (this == NIGHTMARE && this.getLevel(points) == 0) {
            return "Highest obtainable rank";
        }
        int level = getLevel(points);
        if (level == 1) {
            ArrayList<BEDRank> ranks = new ArrayList<>(Arrays.asList(values()));
            int newIndex = ranks.indexOf(this) + 1;
            BEDRank next;
            try {
                next = ranks.get(newIndex);

            } catch (Exception e) {
                return "";
            }
            String color = withColor ? next.getName().replaceAll(ChatColor.stripColor(next.getName()), "") : "";
            if (next == NIGHTMARE0) {
                return The5zigAPI.getAPI().translate("beezig.str.tonextrank", color + Log.df((next.getStart() - points)) + "", next.getName());
            }

            return The5zigAPI.getAPI().translate("beezig.str.tonextrank", color + Log.df((next.getStart() - points)) + "", BED.NUMBERS[5] + " " + next.getName());


        } else if (level == 2) {
            String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";

            return The5zigAPI.getAPI().translate("beezig.str.tonextrank", Log.df(getLvl1() - points) + "", color + BED.NUMBERS[1] + " " + getName());
        } else if (level == 3) {
            String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
            return The5zigAPI.getAPI().translate("beezig.str.tonextrank", Log.df(getLvl2() - points) + "", color + BED.NUMBERS[2] + " " + getName());
        } else if (level == 4) {
            String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
            return The5zigAPI.getAPI().translate("beezig.str.tonextrank", Log.df(getLvl3() - points) + "", color + BED.NUMBERS[3] + " " + getName());
        } else if (level == 5) {
            String color = withColor ? getName().replaceAll(ChatColor.stripColor(getName()), "") : "";
            return The5zigAPI.getAPI().translate("beezig.str.tonextrank", Log.df(getLvl4() - points) + "", color + BED.NUMBERS[4] + " " + getName());
        }
        return null;


    }


}
