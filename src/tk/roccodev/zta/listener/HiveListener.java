package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class HiveListener extends AbstractGameListener<GameMode>{

	@Override
	public Class<GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matchLobby(String lobby) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    public void onMatch(GameMode gameMode, String key, IPatternResult match) {
		 if (gameMode != null && gameMode.getState() != GameState.FINISHED) {
	            return;
	        }
		if(key.equals("timv.welcome")){
			getGameListener().switchLobby("TIMV");
			
			The5zigAPI.getLogger().info("Connected to TIMV! -Hive");
		}
		
	}

	

}
