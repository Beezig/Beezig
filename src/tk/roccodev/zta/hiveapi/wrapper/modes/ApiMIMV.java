package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.MIMV;
import tk.roccodev.zta.hiveapi.wrapper.PvPMode;

public class ApiMIMV extends PvPMode {

	public ApiMIMV(String playerName, String... UUID) {
		super(playerName, UUID);
	}

	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return MIMV.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "MIMV";
	}



}
