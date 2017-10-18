package tk.roccodev.zta.command;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDMap;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIMap;
import tk.roccodev.zta.hiveapi.stuff.dr.DRMap;
import tk.roccodev.zta.hiveapi.stuff.gnt.GiantMap;
import tk.roccodev.zta.hiveapi.stuff.hide.HIDEMap;
import tk.roccodev.zta.hiveapi.stuff.timv.TIMVMap;

public class AutoVoteCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "autovote";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/autovote", "/avote"};
	}
	

	@Override
	public boolean execute(String[] args) {

		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		
		//Format would be /autovote add dr_throwback
		if(args.length == 2){
			String mode = args[0];
			if(mode.equalsIgnoreCase("add")){
				String map = args[1];
				String[] data = map.split("_");
				String gamemode = data[0]; // ex: dr
				String mapString = data[1]; //ex: throwback
				if(data.length == 3){
					mapString = (data[1] + "_" + data[2]);
				}
				if(data.length == 4){
					mapString = (data[1]+"_"+data[2]+"_"+data[3]);
				}
				if(data.length == 5){
					mapString = (data[1]+"_"+data[2]+"_"+data[3]+"_"+data[4]);
				}
				// ¯\_(ツ)_/¯
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
				else if(gamemode.equalsIgnoreCase("bed")){
					mapString = "BED_" + mapString;
					BEDMap apiMap = null;
					try{
						apiMap = BEDMap.valueOf(mapString.toUpperCase());
						The5zigAPI.getLogger().info("apiMap = " + apiMap);
					}
					catch(IllegalArgumentException e){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}	
					List<String> bedMaps = (List<String>) AutovoteUtils.get("bed");
					if(bedMaps == null) bedMaps = new ArrayList<String>();
					bedMaps.add(apiMap.name());
					AutovoteUtils.set("bed", bedMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");
				}
				else if(gamemode.equalsIgnoreCase("giant")){
					
					GiantMap apiMap = null;
					try{
						apiMap = GiantMap.valueOf(mapString.toUpperCase());
						The5zigAPI.getLogger().info("apiMap = " + apiMap);
					}
					catch(IllegalArgumentException e){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}	
					List<String> gntMaps = (List<String>) AutovoteUtils.get("giant");
					if(gntMaps == null) gntMaps = new ArrayList<String>();
					gntMaps.add(apiMap.name());
					AutovoteUtils.set("giant", gntMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");
				}
				else if(gamemode.equalsIgnoreCase("hide")){
					HIDEMap apiMap = HIDEMap.getFromDisplay(mapString);

					if(apiMap == null){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}


					List<String> hideMaps = (List<String>) AutovoteUtils.get("hide");
					if(hideMaps == null) hideMaps = new ArrayList<String>();
					hideMaps.add(apiMap.name());
					AutovoteUtils.set("hide", hideMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");
				}
				else if(gamemode.equalsIgnoreCase("cai")){
					CAIMap apiMap = CAIMap.getFromDisplay(mapString.replaceAll("_", " "));

					if(apiMap == null){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}


					List<String> caiMaps = (List<String>) AutovoteUtils.get("cai");
					if(caiMaps == null) caiMaps = new ArrayList<String>();
					caiMaps.add(apiMap.name());
					AutovoteUtils.set("cai", caiMaps);
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
				else if(game.equalsIgnoreCase("bed")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "BedWars Maps");
					for(BEDMap map : BEDMap.values()){
						The5zigAPI.getAPI().messagePlayer("§e - " + map.name() + " (" + map.getDisplayName() + ")");
					}
				}
				else if(game.equalsIgnoreCase("giant")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "SkyGiants Maps");
					for(GiantMap map : GiantMap.values()){
						The5zigAPI.getAPI().messagePlayer("§e - " + map.name() + " (" + map.getDisplay() + ")");
					}
				}
				else if(game.equalsIgnoreCase("hide")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "HideAndSeek Maps");
					for(HIDEMap map : HIDEMap.values()){
						The5zigAPI.getAPI().messagePlayer("§e - " + map.name() + " (" + map.getDisplayName() + ")");
					}
				}
				else if(game.equalsIgnoreCase("cai")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "Cowboys And Indians Maps");
					for(CAIMap map : CAIMap.values()){
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
				else if(game.equalsIgnoreCase("bed")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "BedWars Maps");
					for(String s : AutovoteUtils.getMapsForMode("bed")){
						The5zigAPI.getAPI().messagePlayer("§e - " + s);
					}
				}
				else if(game.equalsIgnoreCase("bed")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "SkyGiants Maps");
					for(GiantMap map : GiantMap.values()){
						The5zigAPI.getAPI().messagePlayer("§e - " + map.name() + " (" + map.getDisplay() + ")");
					}
				}
				else if(game.equalsIgnoreCase("hide")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "HideAndSeek Maps");
					for(String s : AutovoteUtils.getMapsForMode("hide")){
						The5zigAPI.getAPI().messagePlayer("§e - " + s);
					}
				}
				else if(game.equalsIgnoreCase("cai")){
					The5zigAPI.getAPI().messagePlayer(Log.info + "Cowboys and Indians Maps");
					for(String s : AutovoteUtils.getMapsForMode("cai")){
						The5zigAPI.getAPI().messagePlayer("§e - " + s);
					}
				}
			}
			else if(mode.equalsIgnoreCase("remove")){
				String map = args[1];
				String[] data = map.split("_");
				String gamemode = data[0]; // ex: dr
				String mapString = data[1]; //ex: throwback
				if(data.length == 3){
					mapString = (data[1] + "_" + data[2]);
				}
				if(data.length == 4){
					mapString = (data[1]+"_"+data[2]+"_"+data[3]);
				}
				if(data.length == 5){
					mapString = (data[1]+"_"+data[2]+"_"+data[3]+"_"+data[4]);
				}
				// ¯\_(ツ)_/¯
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
				else if(gamemode.equalsIgnoreCase("bed")){
					mapString = "BED_" + mapString;
					BEDMap apiMap = null;
					try{
						apiMap = BEDMap.valueOf(mapString.toUpperCase());
					}
					catch(IllegalArgumentException e){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}
					
					List<String> bedMaps = (List<String>) AutovoteUtils.get("bed");
					if(bedMaps == null) bedMaps = new ArrayList<String>();
					if(bedMaps.contains(apiMap.name()))
					bedMaps.remove(apiMap.name());
					AutovoteUtils.set("bed", bedMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");
				}
				else if(gamemode.equalsIgnoreCase("giant")){
					GiantMap apiMap = null;
					try{
						apiMap = GiantMap.valueOf(mapString.toUpperCase());
					}
					catch(IllegalArgumentException e){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}
					
					
					List<String> gntMaps = (List<String>) AutovoteUtils.get("giant");
					if(gntMaps == null) gntMaps = new ArrayList<String>();
					if(gntMaps.contains(apiMap.name()))
					gntMaps.remove(apiMap.name());
					AutovoteUtils.set("giant", gntMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");
				}
				else if(gamemode.equalsIgnoreCase("hide")){
					HIDEMap apiMap = HIDEMap.getFromDisplay(mapString);

					if(apiMap == null){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}


					List<String> hideMaps = (List<String>) AutovoteUtils.get("hide");
					if(hideMaps == null) hideMaps = new ArrayList<String>();
					if(hideMaps.contains(apiMap.name()))
						hideMaps.remove(apiMap.name());
					AutovoteUtils.set("hide", hideMaps);
					AutovoteUtils.dump();
					The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");
				}
				else if(gamemode.equalsIgnoreCase("cai")){
					CAIMap apiMap = CAIMap.getFromDisplay(mapString.replaceAll("_", " "));

					if(apiMap == null){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found.");
						return true;
					}


					List<String> caiMaps = (List<String>) AutovoteUtils.get("cai");
					if(caiMaps == null) caiMaps = new ArrayList<String>();
					if(caiMaps.contains(apiMap.name()))
						caiMaps.remove(apiMap.name());
					AutovoteUtils.set("cai", caiMaps);
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
