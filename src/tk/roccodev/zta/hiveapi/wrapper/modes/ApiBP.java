package tk.roccodev.zta.hiveapi.wrapper.modes;

import java.util.Date;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.BP;
import tk.roccodev.zta.hiveapi.wrapper.APIGameMode;

public class ApiBP extends APIGameMode {

	public ApiBP(String playerName, String... UUID) {
		super(playerName, UUID);
	}

	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return BP.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "BP";
	}

	@Override
	public Date lastPlayed() {
		// TODO Auto-generated method stub
		return new Date((long)object("cached"));
	}
	
	



}
