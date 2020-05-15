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

package eu.beezig.core.util.text;

import com.google.common.base.Strings;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Locale;

public class StringUtils {

    private static final String MAP_REPLACE_REGEX = "[^a-zA-Z0-9]";
    private static final int SPACE_PIXEL_WIDTH = pixelWidth(' ');

    public static String normalizeMapName(String mapIn) {
        return mapIn.replaceAll(MAP_REPLACE_REGEX, "").toLowerCase(Locale.ROOT);
    }

    public static String linedCenterText(String color, String text) {
        StringBuilder sb = new StringBuilder();
        int spaces = Math.max(0, ((300 - text.chars().map(c -> pixelWidth((char) c)).sum()) / 2 + 1) / (SPACE_PIXEL_WIDTH + 1));
        String line = Strings.repeat(" ", spaces / 2);
        sb.append(line).append(color).append(ChatColor.STRIKETHROUGH).append(line).append("§r ")
                .append(text).append(" §r").append(color).append(ChatColor.STRIKETHROUGH).append(line).append(ChatColor.RESET).append(line);
        return sb.toString();
    }

    // Source: https://github.com/Electroid/PGM/blob/master/util/src/main/java/tc/oc/pgm/util/component/ComponentUtils.java
    // License: AGPLv3
    private static int pixelWidth(char c) {
        if (Character.isUpperCase(c)) {
            return c == 'I' ? 3 : 5;
        } else if (Character.isDigit(c)) {
            return 5;
        } else if (Character.isLowerCase(c)) {
            switch (c) {
                case 'i':
                    return 1;
                case 'l':
                    return 2;
                case 't':
                    return 3;
                case 'f':
                case 'k':
                    return 4;
                default:
                    return 5;
            }
        } else {
            switch (c) {
                case '!':
                case '.':
                case ',':
                case ';':
                case ':':
                case '|':
                    return 1;
                case '\'':
                    return 2;
                case '[':
                case ']':
                case ' ':
                    return 3;
                case '*':
                case '(':
                case ')':
                case '{':
                case '}':
                case '<':
                case '>':
                    return 4;
                case '@':
                    return 6;
                default:
                    return 5;
            }
        }
    }
}
