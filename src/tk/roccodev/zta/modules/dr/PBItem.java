package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;

public class PBItem extends GameModeItem<DR>{

	public PBItem(){
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if(DR.activeMap != null){
			return DR.currentMapPB;
		}
		return null;
	}
	
	@Override
	public String getName() {
		return "Personal Best";
	}
	
	@Override
	public void registerSettings(){
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof DR)) return false;
		return dummy || (DR.shouldRender(getGameMode().getState()) && DR.activeMap != null && DR.role == "Runner");
		}
		catch(Exception e){
			return false;
		}
	}

}
