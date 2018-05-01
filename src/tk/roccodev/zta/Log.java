package tk.roccodev.zta;

import eu.the5zig.mod.The5zigAPI;

public class Log {

	public static String info = "§a[Beezig]§e ";
	public static String error = "§a[Beezig]§c ";
	
	
	public static String getUserAgent() {
		return "Beezig/" + ZTAMain.BEEZIG_VERSION + " (5zig/" + The5zigAPI.getAPI().getModVersion() + " on " + The5zigAPI.getAPI().getMinecraftVersion() + "; Forge=" + The5zigAPI.getAPI().isForgeEnvironment() + ")";
	}

}
