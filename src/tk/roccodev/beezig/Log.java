package tk.roccodev.beezig;

import eu.the5zig.mod.The5zigAPI;

public class Log {

	
	public static String info = "§7▏ §aBeezig§7 ▏ §3";
	public static String error = "§7▏ §cBeezig§7 ▏ §c";
	
	public static String getUserAgent() {
		return "Beezig/" + BeezigMain.BEEZIG_VERSION + " (5zig/" + The5zigAPI.getAPI().getModVersion() + " on " + The5zigAPI.getAPI().getMinecraftVersion() + "; Forge=" + The5zigAPI.getAPI().isForgeEnvironment() + ")";
	}

}
