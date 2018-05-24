package tk.roccodev.zta.modules.bp;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.BP;

public class DailyItem extends GameModeItem<BP>{

	public DailyItem(){
		super(BP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		
	return BP.dailyPoints + " Points";
	
		
	}
	


	
	@Override
	public String getName() {
		return "Daily";
	}
	@Override
	public boolean shouldRender(boolean dummy){
		try{
			if(!(getGameMode() instanceof BP)) return false;
		return dummy || (BP.shouldRender(getGameMode().getState()));
		}catch(Exception e){
			return false;
		}
	}

}
