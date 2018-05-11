package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZigCheckCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "zigUser";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/5zig", "/zig"};
	}
	

	@Override
	public boolean execute(String[] args) {

		if(args.length > 0) {
			The5zigAPI.getAPI().messagePlayer(Log.info + "Checking...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ApiHiveGlobal hv = new ApiHiveGlobal(args[0]);
						URL url = new URL("http://textures.5zig.net/checkUser/" + hv.getUUID());
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						if (conn.getResponseCode() == 200) {
							The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + hv.getCorrectName() + "§3 is a 5zig user.");
						} else {
							The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + hv.getCorrectName() + "§c is not a 5zig user.");
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else The5zigAPI.getAPI().messagePlayer(Log.error + "You need to specify a player to check.");
		
		return true;
	}

	

	

}
