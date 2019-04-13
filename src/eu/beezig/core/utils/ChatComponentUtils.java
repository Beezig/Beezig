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

package eu.beezig.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatComponentUtils {

    public static String getHoverEventValue(String component) {

        String[] hasHoverEvent = component.split("HoverEvent\\{");
        if (hasHoverEvent.length <= 1)
            return "";
        String hoverEvent = hasHoverEvent[1].split("\\}")[0];
        String[] hasNewTxtComponent = hoverEvent.split("TextComponent\\{");
        if (hasNewTxtComponent.length <= 1)
            return "";
        return hasNewTxtComponent[1].split("\\'\\,")[0].replace("text='", "");

    }

    public static String getClickEventValue(String component) {

        String[] hasHoverEvent = component.split("ClickEvent\\{");
        if (hasHoverEvent.length <= 1)
            return "";
        String hoverEvent = hasHoverEvent[1].split("\\}")[0];
        String[] hasNewTxtComponent = hoverEvent.split("value=\\'");
        if (hasNewTxtComponent.length <= 1)
            return "";
        return hasNewTxtComponent[1].split("\\'")[0].trim();

    }

    public static String getPartyMembers(String component) {
        if (component == null) return null;
        String regex = "Current members: ', siblings=\\[(.*), TextComponent\\{text='.'";
        Pattern regexPattern = Pattern.compile(regex);

        Matcher matcher = regexPattern.matcher(component);
        if (!matcher.find()) return null;
        String group = matcher.group(1);

        Pattern newRegex = Pattern.compile("'(.*?)'");
        StringBuilder members = new StringBuilder();

        Matcher newMatcher = newRegex.matcher(group);
        while (newMatcher.find()) {
            members.append(newMatcher.group(1));
        }


        return members.toString().trim();
    }

}
