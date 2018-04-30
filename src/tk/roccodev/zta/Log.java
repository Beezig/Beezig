package tk.roccodev.zta;

import eu.the5zig.mod.The5zigAPI;

public class Log {

	public static String info = "§a[Beezig]§e ";
	public static String error = "§a[Beezig]§c ";
	
	
	public static String getUserAgent() {
		return "Beezig v" + ZTAMain.BEEZIG_VERSION + " (5zig v" + The5zigAPI.getAPI().getModVersion() + " on " + The5zigAPI.getAPI().getMinecraftVersion() + "; Forge=" + The5zigAPI.getAPI().isForgeEnvironment() + ")";
	}

}
