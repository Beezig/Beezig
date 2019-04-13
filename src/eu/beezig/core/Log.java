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

package eu.beezig.core;

import eu.beezig.core.settings.Setting;
import eu.the5zig.mod.The5zigAPI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Log {


    public static final String info = "§7▏ §aBeezig§7 ▏ §3";
    public static final String error = "§7▏ §cBeezig§7 ▏ §c";
    public static final String bar = "    §7§m                                                                                    ";
    private static final DecimalFormat bigintFormatter = new DecimalFormat("#,###");
    private static final DecimalFormat ratioFormatter = new DecimalFormat("#,###.##");

    static List<String> toSendQueue = new ArrayList<>();


    public static String getUserAgent() {
        String framework = BeezigMain.laby ? "LabyMod" : "5zig";
        return "Beezig/" + BeezigMain.BEEZIG_VERSION + (BeezigMain.VERSION_HASH.isEmpty() ? ""
                : "/" + BeezigMain.VERSION_HASH) + " (" + framework + "/" + The5zigAPI.getAPI().getModVersion() + " on "
                + The5zigAPI.getAPI().getMinecraftVersion() + "; Forge=" + The5zigAPI.getAPI().isForgeEnvironment()
                + "; BeezigForge=" + BeezigMain.hasExpansion + ")";
    }

    public static String t(String key) {
        return The5zigAPI.getAPI().translate(key);
    }

    public static String df(long l) {
        return Setting.THOUSANDS_SEPARATOR.getValue()
                ? bigintFormatter.format(l).replaceAll("\u00A0", " ")
                : Long.toString(l);
    }

    public static String ratio(double d) {
        return ratioFormatter.format(d);
    }

    public static void addToSendQueue(String message) {
        toSendQueue.add(message);
    }

}
