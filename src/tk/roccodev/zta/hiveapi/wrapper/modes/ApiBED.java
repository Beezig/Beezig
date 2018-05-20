package tk.roccodev.zta.hiveapi.wrapper.modes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.stuff.bed.MonthlyPlayer;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.PvPMode;

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
		
		return new MonthlyPlayer((int)(long)j.get("_____place"), 
							(long)j.get("__points"), (long)j.get("_kills"), (long)j.get("_kjdeaths"), (long)j.get("_victories"), 
							(long)j.get("played"), (long)j.get("zBeds"), (long)j.get("zTeams"));
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
