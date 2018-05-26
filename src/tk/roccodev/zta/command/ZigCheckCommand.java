package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
			if(args[0].equals("-a")) {
				List<String> players = new ArrayList<>();
				new Thread(() -> {
					int t = The5zigAPI.getAPI().getServerPlayers().size();
					for(NetworkPlayerInfo i : The5zigAPI.getAPI().getServerPlayers()) {
						if(check(i.getGameProfile().getId().toString().replace("-", ""))) players.add(ChatColor.stripColor(i.getGameProfile().getName()));
					}
					The5zigAPI.getAPI().messagePlayer("\n"
							+ "    §7§m                                                                                    ");
					for(String s : players) {
						The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + s);
					}
					The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "Found §b" + players.size() + "§3 5zig users out of §b" + t + " §3players.");
					The5zigAPI.getAPI().messagePlayer(
							"    §7§m                                                                                    "
									+ "\n");
				}).start();
				
			}
			else {
				new Thread(() -> {
					ApiHiveGlobal hv = new ApiHiveGlobal(args[0]);
					if(check(hv.getUUID())) {
						The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + hv.getCorrectName() + "§3 is a 5zig user.");

					} else {
						The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + hv.getCorrectName() + "§c is not a 5zig user.");
					}
				}).start();
			}
			
		} else The5zigAPI.getAPI().messagePlayer(Log.error + "You need to specify a player to check, or -a to check everyone in the current lobby.");
		
		return true;
	}

	
	private boolean check(String user) {
		HttpURLConnection conn = null;
		try {
			ApiHiveGlobal hv = new ApiHiveGlobal(user);
			URL url = new URL("http://textures.5zig.net/checkUser/" + hv.getUUID());
			conn = (HttpURLConnection) url.openConnection();
			return conn.getResponseCode() == 200;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			conn.disconnect();
		}
		return false;
	}
	

}
