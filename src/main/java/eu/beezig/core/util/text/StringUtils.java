/*
 * Copyright (C) 2017-2021 Beezig Team
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

import com.google.common.base.Joiner;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.text.center.DefaultFontInfo;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

public class StringUtils {
    private static final String MAP_REPLACE_REGEX = "[^a-zA-Z0-9]";

    public static String getColor(String input) {
        Matcher matcher = ChatColor.STRIP_COLOR_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();
        while(matcher.find()) sb.append(matcher.group(0));
        return sb.toString();
    }

    public static String localizedJoin(List<String> list) {
        if(list == null || list.isEmpty()) return "";
        return list.size() == 1
                ? list.get(0)
                : Joiner.on(", ")
                .join(list.subList(0, list.size() - 1))
                .concat(" " + Message.translate("msg.list.and") + " ")
                .concat(list.get(list.size() - 1));
    }

    public static String englishJoin(List<String> list) {
        if(list == null || list.isEmpty()) return "";
        return list.size() == 1
            ? list.get(0)
            : Joiner.on(", ")
            .join(list.subList(0, list.size() - 1))
            .concat(" and ")
            .concat(list.get(list.size() - 1));
    }

    public static String normalizeMapName(String mapIn) {
        return mapIn.replaceAll(MAP_REPLACE_REGEX, "").toLowerCase(Locale.ROOT);
    }

    public static String linedCenterText(String color, String text) {
        String lines = " " + color + ChatColor.STRIKETHROUGH + "             ";
        return centerWithSpaces(lines + "§r " + text + lines);
    }

    // https://www.spigotmc.org/threads/95872/
    public static String centerWithSpaces(String message) {
        if (message == null || message.equals("")) return "";
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 160 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }

    /*
     * Copyright 2012 Google Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return Message.translate("time.now");
        } else if (diff < 2 * MINUTE_MILLIS) {
            return Message.translate("time.minute_ago");
        } else if (diff < 50 * MINUTE_MILLIS) {
            return Beezig.api().translate("time.minutes_ago",  diff / MINUTE_MILLIS);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return Message.translate("time.hour_ago");
        } else if (diff < 24 * HOUR_MILLIS) {
            return Beezig.api().translate("time.hours_ago",  diff / HOUR_MILLIS);
        } else if (diff < 48 * HOUR_MILLIS) {
            return  Message.translate("time.yesterday");
        } else {
            return Beezig.api().translate("time.days_ago",  diff / DAY_MILLIS);
        }
    }
    // end Apache stuff
}
