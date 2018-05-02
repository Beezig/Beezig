package tk.roccodev.zta.hiveapi.wrapper.modes;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

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
	
	public HashMap<String, Double> getMapTimes(){
		HashMap<String, Double> tr = new HashMap<String, Double>();
		JSONObject times = (JSONObject) object("maprecords");
		for(Object o : times.entrySet()) {
			Map.Entry<String, Long> e = (Map.Entry<String, Long>) o;
			tr.put(e.getKey(), (double)((double)e.getValue() / 1000D));
		}
		
		return tr;
	}

}
