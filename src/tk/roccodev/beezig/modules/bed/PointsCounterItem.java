package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;

public class PointsCounterItem extends GameModeItem<BED>{

	public PointsCounterItem(){
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try{		
			return BED.pointsCounter;
		}catch(Exception e){
				e.printStackTrace();
				return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return Log.t("beezig.module.game");
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || (BED.shouldRender(getGameMode().getState()) && BED.pointsCounter != 0);
		}catch(Exception e){
			return false;
		}
	}

}
