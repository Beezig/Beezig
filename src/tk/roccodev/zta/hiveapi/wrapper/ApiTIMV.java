package tk.roccodev.zta.hiveapi.wrapper;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.TIMV;

public class ApiTIMV extends APIGameMode {

	public ApiTIMV(String playerName) {
		super(playerName);
		
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
