package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
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
	public void execute(String[] args) {
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return;
		if(!(args.length == 1)){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /realrank [player]");
			return;
		}
		The5zigAPI.getAPI().messagePlayer(Log.info + "Connecting to API...");
		new Thread(new Runnable(){
			@Override
			public void run(){
				
				The5zigAPI.getAPI().messagePlayer("§eRank: §r" + HiveAPI.getNetworkRank(args[0]));	
				
			}
		}).start();
		
	}

	

}
