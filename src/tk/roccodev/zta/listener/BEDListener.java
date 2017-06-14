package tk.roccodev.zta.listener;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class BEDListener extends AbstractGameListener<BED>{

	@Override
	public Class<BED> getGameMode() {
		// TODO Auto-generated method stub
		return BED.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		// TODO Auto-generated method stub
		return arg0.equals("BED");
	}

	@Override
	public void onGameModeJoin(BED gameMode) {
		

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("BED");
		new Thread(new Runnable(){
			
			@Override
			public void run(){
				try {
					Thread.sleep(200);
					HiveAPI.BEDupdatePoints();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
		
		
	}

	@Override
	public void onServerConnect(BED gameMode) {
		BED.reset(gameMode);
	}
	
	
	

	

}
