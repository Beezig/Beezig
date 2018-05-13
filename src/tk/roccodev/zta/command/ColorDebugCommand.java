package tk.roccodev.zta.command;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;

public class ColorDebugCommand implements Command {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "colordebug";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "/colordebug", "/cdebug" };
	}

	@Override
	public boolean execute(String[] args) {

		if(args.length != 0) {
			
			String txt = String.join(" ", args);
			The5zigAPI.getAPI().messagePlayer(Log.info + "§r" + ChatColor.translateAlternateColorCodes('&', txt));
			
			return true;
		}
		
		ZTAMain.isColorDebug = !ZTAMain.isColorDebug;

		The5zigAPI.getAPI()
				.messagePlayer(Log.info + "Color debug is now: " + (ZTAMain.isColorDebug ? "§aON" : "§cOFF"));

		return true;

	}

}
