package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.SKY;
import tk.roccodev.zta.hiveapi.wrapper.PvPMode;

public class ApiSKY extends PvPMode {

	public ApiSKY(String playerName, String... UUID) {
		super(playerName, UUID);
		
	}

	
	
	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return SKY.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "SKY";
	}
	
	

	
	
	
	
	

}
