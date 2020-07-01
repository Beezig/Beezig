/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.utils;

import eu.the5zig.mod.gui.ingame.Scoreboard;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreboardUtils {
    private static final Pattern SB_REGEX = Pattern.compile("§3(.+): §b(.+)");

    public static int getValue(Scoreboard sb, String prefix) {
        Set<String> keys = sb.getLines().keySet();
        for(String key : keys) {
            Matcher m = SB_REGEX.matcher(key);
            if(m.matches()) {
                String k = m.group(1);
                String value = m.group(2);
                if(prefix.equals(k)) {
                    return Integer.parseInt(value.replaceAll("\\D", ""));
                }
            }
        }
        return 0;
    }
}
