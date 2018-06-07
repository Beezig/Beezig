package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.TIMV;

public class DailyKarmaItem extends GameModeItem<TIMV>{

	public DailyKarmaItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		
	return TIMV.dailyKarma + " Karma";
	
		
	}
	


	
	@Override
	public String getName() {
		return "Daily";
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
