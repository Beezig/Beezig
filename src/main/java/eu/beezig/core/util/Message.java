/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.util;

import eu.beezig.core.Beezig;
import eu.beezig.core.Constants;
import eu.beezig.core.config.Settings;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
    private static final String error = "§7▏ §cBeezig§7 ▏ §c";
    private static final String bar = "    §7§m                                                                                    ";
    private static final DecimalFormat bigintFormatter = new DecimalFormat("#,###");
    private static final DecimalFormat ratioFormatter = new DecimalFormat("#,###.##");
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    static List<String> toSendQueue = new ArrayList<>();

    public static String getUserAgent() {
        /*String framework = BeezigMain.laby ? "LabyMod" : "5zig";
        /return "Beezig/" + BeezigMain.BEEZIG_VERSION + (BeezigMain.VERSION_HASH.isEmpty() ? ""
                : "/" + BeezigMain.VERSION_HASH) + " (" + framework + "/" + The5zigAPI.getAPI().getModVersion() + " on "
                + The5zigAPI.getAPI().getMinecraftVersion() + "; Forge=" + The5zigAPI.getAPI().isForgeEnvironment()
                + "; BeezigForge=" + BeezigMain.hasExpansion + ")";*/
        return String.format("Beezig/%s", Constants.VERSION);
    }

    public static String translate(String key) {
        return Beezig.api().translate(key);
    }

    public static String formatNumber(long l) {
        return Settings.THOUSANDS_SEPARATOR.get().getBoolean()
                ? bigintFormatter.format(l).replaceAll("\u00A0", " ")
                : Long.toString(l);
    }

    public static String date(Date date) {
        return dateFormatter.format(date);
    }

    public static String ratio(Number d) {
        return ratioFormatter.format(d);
    }

    public static void addToSendQueue(String message) {
        toSendQueue.add(message);
    }

    public static void info(String text) {
        String prefix = String.format("§7▏ §aBeezig§7 ▏ %s", Color.primary());
        Beezig.api().messagePlayer(prefix + text);
    }

    public static void error(String text) {
        Beezig.api().messagePlayer(error + text);
    }

    public static void bar() {
        Beezig.api().messagePlayer(bar);
    }
}
