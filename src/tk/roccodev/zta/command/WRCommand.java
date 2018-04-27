package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.stuff.dr.DRMap;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiDR;

public class WRCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "wr";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/wr"};
	}

	@Override
	public boolean execute(String[] args) {
		if(!(ActiveGame.is("dr"))) return false;
		if(args.length == 0 && DR.activeMap != null){
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
					The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §6" + DR.activeMap.getDisplayName() + "§e is §6" + api.getWorldRecord(DR.activeMap) + "§e by §6" + DR.currentMapWRHolder);
				}
			}).start();
	
		}
		else if(args.length == 1){			
			DRMap map = DRMap.getFromDisplay(args[0]);			
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
					The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §6" + map.getDisplayName() + "§e is §6" + api.getWorldRecord(map) + "§e by §6" + api.getWorldRecordHolder(map));
				}
			}).start();
	
		}
		else if(args.length == 2){
			String map1 = args[0] + " " + args[1];
			
			DRMap map = DRMap.getFromDisplay(map1);
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
					The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §6" + map.getDisplayName() + "§e is §6" + api.getWorldRecord(map) + "§e by §6" + api.getWorldRecordHolder(map));
				}
			}).start();
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /wr or /wr [map]");
		}
		
		return true;
	}

	

}
