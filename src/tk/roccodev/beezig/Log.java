package tk.roccodev.beezig;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.settings.Setting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Log {


    public static final String info = "§7▏ §aBeezig§7 ▏ §3";
    public static final String error = "§7▏ §cBeezig§7 ▏ §c";
    public static final String bar = "    §7§m                                                                                    ";
    private static final DecimalFormat bigintFormatter = new DecimalFormat("#,###");
    private static final DecimalFormat ratioFormatter = new DecimalFormat("#.##");

    static List<String> toSendQueue = new ArrayList<>();


    public static String getUserAgent() {
        return "Beezig/" + BeezigMain.BEEZIG_VERSION + (BeezigMain.VERSION_HASH.isEmpty() ? ""
                : "/" + BeezigMain.VERSION_HASH) + " (5zig/" + The5zigAPI.getAPI().getModVersion() + " on "
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
