package tk.roccodev.beezig;

import eu.the5zig.mod.The5zigAPI;

import java.text.DecimalFormat;

public class Log {


    public static final String info = "§7▏ §aBeezig§7 ▏ §3";
    public static final String error = "§7▏ §cBeezig§7 ▏ §c";
    public static final String bar = "    §7§m                                                                                    ";
    private static final DecimalFormat bigintFormatter = new DecimalFormat("#,###");

    public static String getUserAgent() {
        return "Beezig/" + BeezigMain.BEEZIG_VERSION + " (5zig/" + The5zigAPI.getAPI().getModVersion() + " on "
                + The5zigAPI.getAPI().getMinecraftVersion() + "; Forge=" + The5zigAPI.getAPI().isForgeEnvironment()
                + "; BeezigForge=" + BeezigMain.hasExpansion + ")";
    }

    public static String t(String key) {
        return The5zigAPI.getAPI().translate(key);
    }

    public static String df(long l) {
        return bigintFormatter.format(l);
    }

}
