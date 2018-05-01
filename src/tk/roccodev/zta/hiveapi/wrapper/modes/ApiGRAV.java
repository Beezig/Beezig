package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.GRAV;
import tk.roccodev.zta.hiveapi.wrapper.APIGameMode;

public class ApiGRAV extends APIGameMode {

	public ApiGRAV(String playerName, String... UUID) {
		super(playerName, UUID);
	}

	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return GRAV.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "GRAV";
	}

	//GRAV Stuff

	@Override
	public boolean supportsMonthly() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Just a mirror to {@link tk.roccodev.zta.hiveapi.wrapper.APIGameMode#getPoints()}
	 *
	 * @return karma fetched
	 */

	@Override
	public long getPoints(){
		return (long) object("points");
	}

	@Override
	public long getGamesPlayed(){
		return (long) object("gamesplayed");
	}

	public long getVictories(){
		return (long) object("victories");
	}

}
