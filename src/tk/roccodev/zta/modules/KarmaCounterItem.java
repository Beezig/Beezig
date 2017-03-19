package tk.roccodev.zta.modules;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.TIMV;

public class KarmaCounterItem extends GameModeItem<TIMV>{

	public KarmaCounterItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		
		
		return TIMV.karmaCounter;
		
	}
	
	@Override
	public String getName() {
		return "Game";
	}
	@Override
	public boolean shouldRender(boolean dummy){
		try{
			if(!(getGameMode() instanceof TIMV)) return false;
		return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.karmaCounter != 0);
		}catch(Exception e){
			return false;
		}
	}

}
