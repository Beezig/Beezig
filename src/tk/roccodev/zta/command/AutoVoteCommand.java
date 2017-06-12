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
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");
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
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");
				}
			}
			else if(mode.equalsIgnoreCase("listmaps")){
				String game = args[1];
				if(game.equalsIgnoreCase("dr")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "DeathRun Maps");
					for(DRMap map : DRMap.values()){
						The5zigAPI.getAPI().messagePlayer("§e - " + map.name() + " (" + map.getDisplayName() + ")");
					}
				}
				else if(game.equalsIgnoreCase("timv")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "Trouble in Mineville Maps");
					for(TIMVMap map : TIMVMap.values()){
						The5zigAPI.getAPI().messagePlayer("§e - " + map.name() + " (" + map.getDisplayName() + ")");
					}
				}
			}
			else if(mode.equalsIgnoreCase("list")){
				String game = args[1];
				if(game.equalsIgnoreCase("dr")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "DeathRun Maps");
					for(String s : AutovoteUtils.getMapsForMode("dr")){
						The5zigAPI.getAPI().messagePlayer("§e - " + s);
					}
				}
				else if(game.equalsIgnoreCase("timv")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "Trouble in Mineville Maps");
					for(String s : AutovoteUtils.getMapsForMode("timv")){
						The5zigAPI.getAPI().messagePlayer("§e - " + s);
					}
				}
			}
			else if(mode.equalsIgnoreCase("remove")){
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
					if(drMaps.contains(apiMap.name()))
					drMaps.remove(apiMap.name());
					AutovoteUtils.set("dr", drMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");
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
					if(timvMaps.contains(apiMap.name()))
					timvMaps.remove(apiMap.name());
					AutovoteUtils.set("timv", timvMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");
				}
			}
			
			
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage:"
					+ "\n" + "/autovote add mode_map - Adds a favorite map to a gamemode" 
					+ "\n" + "/autovote list mode - Lists your favorite maps for that gamemode"
					+ "\n" + "/autovote listmaps mode - Lists the available maps for that gamemode"
					+ "\n" + "/autovote remove mode_map - Removes a favorite map from a gamemode");
		}
		
		return true;
	}

	

	

}
