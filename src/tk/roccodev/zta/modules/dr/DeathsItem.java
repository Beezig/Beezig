package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;

public class DeathsItem extends GameModeItem<DR>{

	public DeathsItem(){
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return DR.deaths;
	}
	
	@Override
	public String getName() {
		return "Deaths";
	}
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof DR)) return false;
			return dummy || (DR.shouldRender(getGameMode().getState()) && DR.role == "Runner");
		}
		catch(Exception e){
			return false;
		}
	}

}