package tk.roccodev.beezig.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.hiveapi.stuff.bed.MonthlyPlayer;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.PvPMode;

import org.json.simple.JSONObject;

import java.net.URL;
import java.util.Map;

public class ApiBED extends PvPMode {

	public ApiBED(String playerName, String... UUID) {
		super(playerName, UUID);
		
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

	public MonthlyPlayer getMonthlyStatus() {
		try {
		JSONObject o = APIUtils.getObject(APIUtils.readURL(new URL("https://roccodev-misc.firebaseio.com/bed-monthly.json")));
		JSONObject j = (JSONObject) o.get(this.getUUID());
		
		return new MonthlyPlayer(this.getUUID(), (String)j.get("____name"), (int)(long)j.get("_____place"), 
							(long)j.get("__points"), (long)j.get("_kills"), (long)j.get("_kjdeaths"), (long)j.get("_victories"), 
							(long)j.get("played"), (long)j.get("zBeds"), (long)j.get("zTeams"));
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public static MonthlyPlayer getMonthlyStatusByPlace(int place) {
		try {
			JSONObject o = APIUtils.getObject(APIUtils.readURL(new URL("https://roccodev-misc.firebaseio.com/bed-monthly.json")));
			for(Object e : o.entrySet()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) e;
				JSONObject j = (JSONObject) entry.getValue();
				if((long)j.get("_____place") == place) {
					return new MonthlyPlayer(entry.getKey(), (String)j.get("____name"), (int)(long)j.get("_____place"), 
							(long)j.get("__points"), (long)j.get("_kills"), (long)j.get("_kjdeaths"), (long)j.get("_victories"), 
							(long)j.get("played"), (long)j.get("zBeds"), (long)j.get("zTeams"));
				}
				
				
			}
			
			return null;
			
			} catch(Exception e) {
				return null;
			}
	}
	


	@Override
	public boolean supportsMonthly() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	

	
	
	
	
	

}
