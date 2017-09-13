package tk.roccodev.zta.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.games.TIMV;

public class DBodiesItem extends GameModeItem<TIMV> {

	public DBodiesItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		return TIMV.detectivesDiscovered + "/" + TIMV.detectivesBefore + " Detectives";
		
	}

	@Override
	public String getName() {
		return "Discovered";
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
