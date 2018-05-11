package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.NetworkRank;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;

public class RealRankCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "realrank";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/realrank", "/rr"};
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
				ApiHiveGlobal api = new ApiHiveGlobal(args[0]);
				String ign = api.getCorrectName();
				String networkRank = api.getNetworkTitle();
				ChatColor rankColor = api.getNetworkRankColor();
				//§r§eJollyajaX§r
				//§r§aItsNiklass§r
				//§r§diElena§r
				if(NetworkRank.fromDisplay(networkRank).getLevel() >= 50 && NetworkRank.fromDisplay(networkRank).getLevel() < 80){
					/*Only checks for VIPs & Moderators
					//check if tampered displayName - e.g. TIMV
					rankColor = null;
					for(NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
						if (ChatColor.stripColor(npi.getDisplayName()).equalsIgnoreCase(ign)) {
							rankColor = ChatColor.getByChar(npi.getDisplayName().charAt(3));
							The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.YELLOW + ign + "'s Rank: " + rankColor + NetworkRank.fromColor(rankColor).getDisplay());
						}
					}
					if(rankColor == null)  The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.YELLOW + ign + "'s Rank: " + NetworkRank.REGULAR.getColor() + NetworkRank.REGULAR.getDisplay());
					*/
					//TODO Never works outside the Hub - the reason being Hive's game plugins & .getDisplayName()
					The5zigAPI.getAPI().messagePlayer(Log.info + "¯\\_(ツ)_/¯");
				} else /*default*/ The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + ign + "§3's Rank: " + rankColor + networkRank);
			}
		}).start();
		return true;
	}

	

}
