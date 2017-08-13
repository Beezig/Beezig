package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiDR;

public class PBCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "pb";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/pb"};
		return aliases;
	}

	@Override
	public boolean execute(String[] args) {
		if(!(ActiveGame.is("dr"))) return false;
		if(args.length == 1 && DR.activeMap != null){
			
			String ign = args[0];
			
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiDR api = new ApiDR(ign);
					The5zigAPI.getAPI().messagePlayer(Log.info + api.getParentMode().getNetworkRankColor() + api.getParentMode().getCorrectName() + "§e's Personal Best on map §6" + DR.activeMap.getDisplayName() + "§e is §6" + api.getPersonalBest(DR.activeMap));
				}
			}).start();
	
		}
		else if(args.length == 2){
			
			String ign = args[0];
			DRMap map = DRMap.getFromDisplay(args[1]);
			
				
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiDR api = new ApiDR(ign);
					The5zigAPI.getAPI().messagePlayer(Log.info + api.getParentMode().getNetworkRankColor() + api.getParentMode().getCorrectName() + "§e's Personal Best on map §6" + map.getDisplayName() + "§e is §6" + api.getPersonalBest(map));
				}
			}).start();
	
		}
		else if(args.length == 3){
			String map1 = args[1] + " " + args[2];
			String ign = args[0];
			DRMap map = DRMap.getFromDisplay(map1);
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiDR api = new ApiDR(ign);
					The5zigAPI.getAPI().messagePlayer(Log.info + api.getParentMode().getNetworkRankColor() + api.getParentMode().getCorrectName() + "§e's Personal Best on map §6" + map.getDisplayName() + "§e is §6" + api.getPersonalBest(map));
				}
			}).start();
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /pb [player] or /pb [player] [map]");
		}		
		return true;
	}

	

}
