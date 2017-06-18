package tk.roccodev.zta.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class BedsDestroyedItem extends GameModeItem<BED>{

	public BedsDestroyedItem(){
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try{		
			return BED.bedsDestroyed;
		}catch(Exception e){
				e.printStackTrace();
				return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "Destroyed";
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || (BED.shouldRender(getGameMode().getState()) && BED.bedsDestroyed != 0);
		}catch(Exception e){
			return false;
		}
	}

}
