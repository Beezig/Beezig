package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.ZTAMain;
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
	public boolean onServerChat(BED gameMode, String message) {
		
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("BedWars Color Debug: (" + message + ")");
		}
		
		
		return false;
		
		
	}

	@Override
	public void onTitle(BED gameMode, String title, String subTitle) {
		if(subTitle != null && subTitle.equals("§r§7Protect your bed, destroy others!§r")){
			gameMode.setState(GameState.GAME);
			
			//As Hive sends this subtitle like 13 times, don't do anything here please :)
		}
		
	}

	@Override
	public void onServerConnect(BED gameMode) {
		BED.reset(gameMode);
	}
	
	
	

	

}
