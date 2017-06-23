package tk.roccodev.zta.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class DeathsItem extends GameModeItem<Giant>{

	public DeathsItem(){
		super(Giant.class);
	}
	
	
	
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			
			return Giant.gameDeaths + " (" + (Giant.gameDeaths + Giant.totalDeaths) + ")";
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "Deaths";
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			
		return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.gameDeaths != 0);
		}catch(Exception e){
			return false;
		}
	}

}
