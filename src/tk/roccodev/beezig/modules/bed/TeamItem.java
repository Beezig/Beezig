package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;

public class TeamItem extends GameModeItem<BED>{

	public TeamItem(){
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return BED.team;	
	}
	
	@Override
	public String getName() {
		return Log.t("beezig.module.bed.team");
	}

	@Override
	public boolean shouldRender(boolean dummy){
		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || (BED.shouldRender(getGameMode().getState()) && BED.team != null);
		}catch(Exception e){
			return false;
		}
	}

}
