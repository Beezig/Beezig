package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.wrapper.APIGameMode;

public class ApiTIMV extends APIGameMode {

	public ApiTIMV(String playerName, String... UUID) {
		super(playerName, UUID);
	}

	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return TIMV.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "TIMV";
	}
	
	
	
	//TIMV Stuff
	
	@Override
	public boolean supportsMonthly() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getMonthlyPointsName() {
		// TODO Auto-generated method stub
		return "karma";
	}

	/**
	 * Just a mirror to {@link tk.roccodev.zta.hiveapi.wrapper.APIGameMode#getPoints()}
	 * 
	 * @return karma fetched
	 */
	public long getKarma(){
		return getPoints();
	}
	
	public long getRolepoints(){
		return (long) object("role_points");
	}
	
	
	public long getMostKarma(){
		return (long) object("most_points");
	}

	

}
