package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.games.GNT;
import tk.roccodev.zta.games.GNTM;
import tk.roccodev.zta.games.Giant;

public class GiantListener extends AbstractGameListener<Giant>{

	private Class<Giant> gameMode = Giant.class;
	private Giant instance = Giant.instance;
	public static GiantListener listener;
	private String lobby;
	
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
	
		if(arg0.toUpperCase().startsWith("GNT")){ // Support for GNTM
			lobby = arg0;
			return true;
		} 
		else{
			return false;
		}
	}
	
	public void setGameMode(Class<? extends Giant> newMode, Giant instance){
		gameMode = (Class<Giant>) newMode;
		this.instance = instance;
		
	}

	@Override
	public void onGameModeJoin(Giant gameMode) {
		if(this.lobby.equalsIgnoreCase("GNT")) ActiveGame.set("GNT");
		if(this.lobby.equalsIgnoreCase("GNTM")) ActiveGame.set("GNTM");
		gameMode.setState(GameState.STARTING);
		The5zigAPI.getLogger().info(instance.getName());
		
	}


	@Override
	public void onServerConnect(Giant gameMode) {
		
		Giant.reset(gameMode);
		
	}
	
	
	
	
	
	

	
}
