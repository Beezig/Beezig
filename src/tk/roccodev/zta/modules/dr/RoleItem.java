package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;


public class RoleItem extends GameModeItem<DR>{

	public RoleItem(){
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return DR.role;
	}
	
	@Override
	public String getName() {
		return "Role";
	}
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof DR)) return false;
		return dummy || (DR.shouldRender(getGameMode().getState()) && DR.role != null);
		}catch(Exception e){
			return false;
		}
	}

}
