package tk.roccodev.beezig.utils;

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
        if(component == null) return null;
        String regex = "Current members: ', siblings=\\[(.*), TextComponent\\{text='.'";
        Pattern regexPattern = Pattern.compile(regex);

        Matcher matcher = regexPattern.matcher(component);
        if(!matcher.find()) return null;
        String group = matcher.group(1);

        Pattern newRegex = Pattern.compile("'(.*?)'");
        StringBuilder members = new StringBuilder();

        Matcher newMatcher = newRegex.matcher(group);
        while(newMatcher.find()) {
            members.append(newMatcher.group(1));
        }


        return members.toString().trim();
    }

}
