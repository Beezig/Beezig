package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;

public class DailyKarmaItem extends GameModeItem<TIMV>{

	public DailyKarmaItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		
	return TIMV.dailyKarma + " " + Log.t("beezig.module.timv.karma");
	
		
	}
	


	
	@Override
	public String getName() {
		return Log.t("beezig.module.daily");
	}
	@Override
	public boolean shouldRender(boolean dummy){
		try{
			if(!(getGameMode() instanceof TIMV)) return false;
		return dummy || (TIMV.shouldRender(getGameMode().getState()));
		}catch(Exception e){
			return false;
		}
	}

}
