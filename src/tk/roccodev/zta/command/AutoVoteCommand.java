package tk.roccodev.zta.command;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.TIMVMap;

public class AutoVoteCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "autovote";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/autovote", "/avote"};
		return aliases;
	}
	

	@Override
	public boolean execute(String[] args) {
		
		if(!ZTAMain.isTIMV && !ZTAMain.isDR) return false;
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(args.length == 0){
			//TODO List maps
		}
		//Format would be /autovote add dr_throwback
		if(args.length == 2){
			String mode = args[0];
			if(mode.equalsIgnoreCase("add")){
				String map = args[1];
				String[] data = map.split("_");
				String gamemode = data[0]; // ex: dr
				String mapString = data[1]; //ex: throwback
				if(gamemode.equalsIgnoreCase("dr")){
					DRMap apiMap = DRMap.valueFromDisplay(mapString);
					
					if(apiMap == null){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}
					
					
					List<String> drMaps = (List<String>) AutovoteUtils.get("dr");
					if(drMaps == null) drMaps = new ArrayList<String>();
					drMaps.add(apiMap.name());
					AutovoteUtils.set("dr", drMaps);
					AutovoteUtils.dump();
				}
				else if(gamemode.equalsIgnoreCase("timv")){
					TIMVMap apiMap = null;
					try{
						apiMap = TIMVMap.valueOf(mapString.toUpperCase());
					}
					catch(IllegalArgumentException e){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}
					
					
					List<String> timvMaps = (List<String>) AutovoteUtils.get("timv");
					if(timvMaps == null) timvMaps = new ArrayList<String>();
					timvMaps.add(apiMap.name());
					AutovoteUtils.set("timv", timvMaps);
					AutovoteUtils.dump();
				}
			}
			
		}
		
		return true;
	}

	

	

}
