package tk.roccodev.zta.listener;

import org.json.simple.parser.ParseException;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.TIMV;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class TIMVListener extends AbstractGameListener<TIMV>{

	@Override
	public Class<TIMV> getGameMode() {
		// TODO Auto-generated method stub
		return TIMV.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		
		return lobby.equals("TIMV");
		
	}
	
	@Override
	public void onGameModeJoin(TIMV gameMode){
		gameMode.setState(GameState.STARTING);
		try {
			HiveAPI.updateKarma();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean onServerChat(TIMV gameMode, String message) {
		
		if(message.equals("§8▍ §3TIMV§8 ▏ §6Welcome to Trouble in Mineville!")){
			gameMode.setState(GameState.STARTING);
			The5zigAPI.getLogger().info("DEBUG = Joined TIMV");
			try {
				HiveAPI.updateKarma();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(message.contains("§cLost §e20§c karma") && gameMode != null){
			TIMV.minus20();
		}
		else if(message.contains("§cLost §e40§c karma") && gameMode != null){
			TIMV.minus40();
		}
		else if(message.contains("§aAwarded §e20§a karma") && gameMode != null){
			TIMV.plus20();
			
		}
		else if(message.contains("§aAwarded §e10§a karma") && gameMode != null){
			TIMV.plus10();
		}
		else if(message.contains("§aAwarded §e25§a karma") && gameMode != null){
			TIMV.plus25();
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §cGame Over§7") && gameMode != null){
			TIMV.reset(gameMode);
		}
		
		
		
		
		return false;
	}

	@Override
	public void onServerConnect(TIMV gameMode) {
		TIMV.reset(gameMode);
		
		
	}
	
	
	
	
	
	
	

	

}
