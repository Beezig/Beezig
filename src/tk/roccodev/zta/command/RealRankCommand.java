package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class RealRankCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "realrank";
	}

	@Override
	public String[] getAliases() {
		String aliases[] = {"/realrank", "/rr"};
		return aliases;
	}

	@Override
	public boolean execute(String[] args) {
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(!(args.length == 1)){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /realrank [player]");
			return true;
		}
		The5zigAPI.getAPI().messagePlayer(Log.info + "Connecting to API...");
		new Thread(new Runnable(){
			@Override
			public void run(){
				String ign = HiveAPI.getName(args[0]);
				
				String networkRank = HiveAPI.getNetworkRank(ign);
				ChatColor rankColor = HiveAPI.getRankColor(networkRank);
				The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.YELLOW + ign + "'s Rank: " + rankColor + networkRank);				
			}
		}).start();
		return true;
	}

	

}
