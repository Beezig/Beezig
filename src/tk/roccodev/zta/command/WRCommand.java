package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class WRCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "wr";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/wr"};
		return aliases;
	}

	@Override
	public boolean execute(String[] args) {
		if(!(ZTAMain.isDR)) return false;
		if(args.length == 0 && DR.activeMap != null){
			new Thread(new Runnable(){
				@Override
				public void run(){
					The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §6" + DR.activeMap.getDisplayName() + "§e is §6" + HiveAPI.DRgetWR(DR.activeMap) + "§e by §6" + DR.currentMapWRHolder);
				}
			}).start();
	
		}
		else if(args.length == 1){			
			DRMap map = DRMap.getFromDisplay(args[0]);			
			new Thread(new Runnable(){
				@Override
				public void run(){
					The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §6" + map.getDisplayName() + "§e is §6" + HiveAPI.DRgetWR(map) + "§e by §6" + DR.currentMapWRHolder);
				}
			}).start();
	
		}
		else if(args.length == 2){
			String map1 = args[0] + " " + args[1];
			
			DRMap map = DRMap.getFromDisplay(map1);
			new Thread(new Runnable(){
				@Override
				public void run(){
					The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §6" + map.getDisplayName() + "§e is §6" + HiveAPI.DRgetWR(map) + "§e by §6" + DR.currentMapWRHolder);
				}
			}).start();
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /wr or /wr [map]");
		}		
		return true;
	}

	

}
