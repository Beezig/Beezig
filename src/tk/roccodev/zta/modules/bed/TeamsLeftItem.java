package tk.roccodev.zta.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.BED;

public class TeamsLeftItem extends GameModeItem<BED>{

	public TeamsLeftItem(){
		super(BED.class);
	}
	
	@Override
	protected Object getValue(boolean dummy) {
		
			return BED.teamsLeft;

	}
	
	@Override
	public String getName() {
		return "Teams Left";
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || (BED.shouldRender(getGameMode().getState()) && BED.teamsLeft != 0);
		}catch(Exception e){
			return false;
		}
	}
}
