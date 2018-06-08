package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;

public class BodiesItem extends GameModeItem<TIMV> {

	public BodiesItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		return TIMV.traitorsDiscovered + "/" + TIMV.traitorsBefore + " " + Log.t("beezig.str.timv.traitors");
		
	}

	@Override
	public String getName() {
		return Log.t("beezig.module.timv.bodies");
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){
		try{
			if(!(getGameMode() instanceof TIMV)) return false;
		return dummy || (getGameMode().getState() == GameState.GAME);
		}catch(Exception e){
			return false;
		}
	}

}
