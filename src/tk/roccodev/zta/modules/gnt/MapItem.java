package tk.roccodev.zta.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.Giant;

public class MapItem extends GameModeItem<Giant>{

	public MapItem(){
		super(Giant.class);
	}
	
	
	
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			if(Giant.activeMap == null) return "No Map";
			return Giant.activeMap;
		}catch(Exception e){
			e.printStackTrace();
			return "No Map";
		}
	}
	
	@Override
	public String getName() {
		return "Map";
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			
		return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.activeMap != null);
		}catch(Exception e){
			return false;
		}
	}

}
