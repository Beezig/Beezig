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

package eu.beezig.core.hiveapi.wrapper;

import eu.the5zig.util.minecraft.ChatColor;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum NetworkRank {

    REGULAR("Regular", BLUE, 0),

    GOLD("Gold Premium", ChatColor.GOLD, 10),

    DIAMOND("Diamond Premium", AQUA, 20),

    EMERALD("Emerald Premium", GREEN, 30),

    ULTIMATE("Ultimate Premium", LIGHT_PURPLE, 40),

    VIP("VIP", DARK_PURPLE, 50),
    YOUTUBER("YouTuber", DARK_PURPLE, 51),
    STREAMER("Streamer", DARK_PURPLE, 52),
    CONTRIBUTOR("Contributor", DARK_PURPLE, 53),

    NECTAR("Team Nectar", DARK_AQUA, 54),

    RESERVERD_STAFF("Reserved Staff", null, 60),

    MODERATOR("Moderator", RED, 70),

    SENIOR_MODERATOR("Senior Moderator", DARK_RED, 80),
    STAFFMANAGER("Staff Manager", DARK_RED, 81),

    DEVELOPER("Developer", GRAY, 90),

    OWNER("Owner", YELLOW, 100);


    private String display;
    private ChatColor color;
    private Integer level;

    NetworkRank(String display, ChatColor color, Integer level) {
        this.display = display;
        this.color = color;
        this.level = level;
    }

    public static NetworkRank fromDisplay(String s) {
        for (NetworkRank rank : values()) {
            if (rank.getDisplay().equalsIgnoreCase(s)) return rank;
        }
        return null;
    }

    public static NetworkRank fromColor(ChatColor cc) {
        for (NetworkRank rank : values()) {
            if (rank.getColor() == cc) return rank;
        }
        return null;
    }

    public String getDisplay() {
        return display;
    }

    public ChatColor getColor() {
        return color;
    }

    public Integer getLevel() {
        return level;
    }


}
