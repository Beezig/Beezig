package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;

public class KillsItem extends GameModeItem<DR>{

	public KillsItem(){
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return DR.kills;
	}
	
	@Override
	public String getName() {
		return "Kills";
	}
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof DR)) return false;
		return dummy || (DR.shouldRender(getGameMode().getState()) && DR.role == "Death");
		}catch(Exception e){
			return false;
		}
	}

}