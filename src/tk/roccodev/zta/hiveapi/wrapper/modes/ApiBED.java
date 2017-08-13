package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.wrapper.PvPMode;

public class ApiBED extends PvPMode {

	public ApiBED(String playerName) {
		super(playerName);
		
	}

	
	
	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return BED.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "BED";
	}
	
	

	
	
	
	
	

}
