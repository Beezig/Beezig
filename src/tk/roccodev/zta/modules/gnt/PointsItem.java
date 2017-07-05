package tk.roccodev.zta.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class PointsItem extends GameModeItem<Giant>{

	public PointsItem(){
		super(Giant.class);
	}
	
	
	
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			
			return HiveAPI.GiantPoints;
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "Points";
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			
		return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")));
		}catch(Exception e){
			return false;
		}
	}

}
