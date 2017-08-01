package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.wrapper.APIGameMode;
import tk.roccodev.zta.hiveapi.wrapper.NetworkRank;

public class ApiBED extends APIGameMode {

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
