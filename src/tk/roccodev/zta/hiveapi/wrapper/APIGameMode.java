package tk.roccodev.zta.hiveapi.wrapper;

import java.util.Date;
import java.util.UUID;

import org.json.simple.JSONObject;

import eu.the5zig.mod.server.GameMode;

public class APIGameMode {

	private String playerName;
	private UUID uuid;
	
	public APIGameMode(String playerName){
		this.playerName = playerName;
		this.uuid = UUID.fromString(APIUtils.getUUID(playerName));
		
	}
	
	public Class<? extends GameMode> getGameMode(){
		return null;
	}
	
	public String getShortcode(){
		return "Hive";
	}
	
	
	public Object object(String field){
		return APIUtils.getObject(APIUtils.Parser.read(APIUtils.Parser.game(uuid.toString(), getShortcode() ))).get(field);
	}
	
	public JSONObject jsonObject(){
		return APIUtils.getObject(APIUtils.Parser.read(APIUtils.Parser.game(uuid.toString(), getShortcode() )));
	}
	
	/**
	 * Fetches points for the gamemode
	 * Note: Override if custom points
	 * 
	 * @return the points fetched
	 * 
	 */
	public long getPoints(){
		return (long) object("total_points");
	}
	
	public int getAchievements(){
		JSONObject obj = (JSONObject) object("achievements");
		return obj.size() - 1;
	}
	
	public String getTitle(){
		return (String) object("title");
	}
	
	public Date lastPlayed(){
		long time = (long) object("lastlogin");
		return new Date(time * 1000);
	}
	

}
