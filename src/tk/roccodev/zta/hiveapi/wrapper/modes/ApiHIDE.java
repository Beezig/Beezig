package tk.roccodev.zta.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONObject;
import tk.roccodev.zta.games.HIDE;
import tk.roccodev.zta.hiveapi.wrapper.APIGameMode;

import java.util.Date;

public class ApiHIDE extends APIGameMode {

	public ApiHIDE(String playerName) {
		super(playerName);
	}
	
	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return HIDE.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "HIDE";
	}
	
	@Override
	public Date lastPlayed(){
		return new Date((long) object("lastlogin"));
	}
	
	public String[] getBlocks(){
		String blocks = (String) object("blocks");
		return blocks.split(",");
	}

	public JSONObject getBlockExperience(){
		return (JSONObject) object("blockExperience");
	}

	public JSONObject getRawBlockExperience(){
		return (JSONObject) object("rawBlockExperience");
	}
	
	
	
	
	

}
