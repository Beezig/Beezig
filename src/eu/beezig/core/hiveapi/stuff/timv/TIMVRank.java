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

package eu.beezig.core.hiveapi.stuff.timv;

import eu.beezig.core.Log;
import eu.beezig.core.games.BED;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.Arrays;

import static eu.the5zig.util.minecraft.ChatColor.*;

public enum TIMVRank implements RankEnum {

    CIVILIAN("Civilian", GRAY + "", 0),
    INSPECTOR("Inspector", DARK_AQUA + "", 105),
    WITNESS("Witness", AQUA + "", 505),
    SCOUT("Scout", LIGHT_PURPLE + "", 755),
    FORENSIC("Forensic", GOLD + "", 1005),
    TRUSTABLE("Trustable", YELLOW + "", 2005),
    DIRECTOR("Director", AQUA + "", 3505),
    DECEIVER("Deceiver", RED + "", 5005),
    TRACER("Tracer", AQUA + "", 7505),
    AGENT("Agent", GOLD + "", 10005),
    SPY("Spy", YELLOW + "", 15005),
    CONSPIRATOR("Conspirator", LIGHT_PURPLE + "", 25005),
    EAVESDROPPER("Eavesdropper", DARK_AQUA + "", 35005),
    CONSTABLE("Constable", AQUA + "", 45005),
    OFFICER("Officer", YELLOW + "", 60005),
    SERGEANT("Sergeant", RED + "", 80005),
    COMMISSIONER("Commissioner", DARK_PURPLE + "", 100005),
    UNDERCOVER("Undercover", GOLD + "" + BOLD, 125005),
    LESTRADE("Lestrade", BLUE + "" + BOLD, 150005),
    WATSON("Watson", WHITE + "" + BOLD, 200005),
    SHERLOCK("✦ Sherlock", GOLD + "" + BOLD + "", -1);

    private String display;
    private String prefix;
    private int startKarma;


    TIMVRank(String display, String prefix, int startKarma) {
        this.display = display;
        this.prefix = prefix;
        this.startKarma = startKarma;
    }

    public static TIMVRank getFromDisplay(String display) {
        for (TIMVRank rank : TIMVRank.values()) {
            if (rank.display.equalsIgnoreCase(display)) return rank;
        }
        return null;
    }

    @Override
    public String getTotalDisplay() {
        return getTotalDisplay(200_000);
    }


    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getDisplay() {
        return getDisplay(200_000);
    }

    public String getDisplay(long pts) {
        if (this == WATSON) {
            long consider = pts - 200_000;
            long level = consider / 100_000 + 1;
            return BED.NUMBERS[Math.toIntExact(level)] + " " + display;
        }
        return display;
    }

    public String getTotalDisplay(long pts) {
        return prefix + getDisplay(pts);
    }

    public int getStart() {
        return startKarma;
    }

    public String getKarmaToNextRank(int karma) {
        if (this == SHERLOCK) return "Leaderboard Rank";
        if (this == WATSON) return "Highest Rank";
        ArrayList<TIMVRank> ranks = new ArrayList<>(Arrays.asList(values()));
        int newIndex = ranks.indexOf(this) + 1;
        TIMVRank next;
        try {
            next = ranks.get(newIndex);

        } catch (Exception e) {
            return "";
        }


        return The5zigAPI.getAPI().translate("beezig.str.tonextrank", next.prefix + Log.df(next.getStart() - karma), next.getTotalDisplay());
    }

}
