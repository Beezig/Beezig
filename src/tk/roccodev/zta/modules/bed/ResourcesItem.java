package tk.roccodev.zta.modules.bed;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class ResourcesItem extends GameModeItem<BED>{

	public ResourcesItem(){
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try{
		StringBuilder sb = new StringBuilder();
		
		int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
		int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
		int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
		int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");
		if(ironIngots != 0) sb.append(ironIngots + " Iron / ");
		if(goldIngots != 0) sb.append(goldIngots + " Gold / ");
		if(diamonds != 0) sb.append(diamonds + " Diamonds / ");
		if(emeralds != 0) sb.append(emeralds + " Emeralds / ");
		if(sb.length() > 2) sb.deleteCharAt(sb.length()-2); 

		
		return sb.toString().trim();
		}catch(Exception e){
			e.printStackTrace();
			return "Error";
		}
		
	}
	
	@Override
	public String getName() {
		return "Resources";
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || getGameMode().getState() == GameState.GAME;
		}catch(Exception e){
			return false;
		}
	}

}
