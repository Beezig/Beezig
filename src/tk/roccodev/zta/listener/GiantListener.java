package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import tk.roccodev.zta.games.Giant;

public class GiantListener extends AbstractGameListener<Giant>{

	private Class<Giant> gameMode = Giant.class;
	private Giant instance = Giant.instance;
	public static GiantListener listener;
	
	public GiantListener(){
		listener = this;
	}
	
	
	@Override
	public Class<Giant> getGameMode() {
		// TODO Auto-generated method stub
		return Giant.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		// TODO Auto-generated method stub
		return arg0.toUpperCase().startsWith("GNT"); // Support for GNTM
	}
	
	public void setGameMode(Class<? extends Giant> newMode, Giant instance){
		gameMode = (Class<Giant>) newMode;
		this.instance = instance;
		The5zigAPI.getLogger().info("lol");
	}

	@Override
	public void onGameModeJoin(Giant gameMode) {
		
		The5zigAPI.getLogger().info(instance.getName());
		
	}
	
	
	
	
	
	

	
}
