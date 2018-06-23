package tk.roccodev.beezig.utils;

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

}
