package tk.roccodev.zta.hiveapi.wrapper;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;

import java.util.Date;

public class APIGameMode {

	private String playerName;
	private String uuid;
	private ApiHiveGlobal parent;
	private JSONObject object;
	
	public APIGameMode(String playerName, String... UUID){

		if(UUID.length == 0) {
			this.playerName = playerName;
			this.uuid = APIUtils.getUUID(playerName);
		} else {
			this.playerName = playerName;
			this.uuid = UUID[0].replaceAll("-","");
		}
		if(!(this instanceof ApiHiveGlobal))
		this.parent = new ApiHiveGlobal(playerName);
		
	}
	
	
	
	public Class<? extends GameMode> getGameMode(){
		return null;
	}
	
	public boolean supportsMonthly(){
		return false;
	}
	
	public String getShortcode(){
		return "Hive";
	}
	
	public ApiHiveGlobal getParentMode(){
		return parent;
	}
	
	public Object object(String field){
		if(object == null) object = jsonObject();
		return object.get(field);
	}
	
	public JSONObject jsonObject(){
		return APIUtils.getObject(APIUtils.Parser.read(APIUtils.Parser.game(uuid, getShortcode())));
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
	
	public long getGamesPlayed(){
		return (long) object("games_played");
	}
	
	public String getMonthlyPointsName(){
		return "total_points";
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
	
	
	public Long getLeaderboardsPlacePoints(int index){
		JSONParser parser = new JSONParser();
		JSONObject o1 = null;
		try {
			o1 = (JSONObject) parser.parse(((JSONArray) parser.parse(((JSONObject) parser.parse(APIUtils.Parser.read(APIUtils.Parser.hiveLB(index, getShortcode())))).get("leaderboard").toString())).get(0).toString());
		} catch (Exception e) {
			The5zigAPI.getLogger().info("Failed getLBPlacePoints (JSON 0)");
			e.printStackTrace();
		}
		try {
			return (Long) parser.parse(o1.get(this.getMonthlyPointsName()).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
	
	public int getMonthlyRank(){
		if(!supportsMonthly()) throw new UnsupportedOperationException("Mode does not support Monthly Leaderboards!");
		JSONParser parser = new JSONParser();
		JSONArray o1 = null;
		JSONObject o2 = null;
		try {
			o1 = (JSONArray) parser.parse(((JSONObject) parser.parse(APIUtils.Parser.read(APIUtils.Parser.monthlyLB(getShortcode().toLowerCase())))).get("leaderboard").toString());
		} catch (Exception e) {
			The5zigAPI.getLogger().info("Failed getMonthlyLBRank (thtmx.rocks)");
			e.printStackTrace();				
		}
		for(int i = 0; i<=350; i++){
		 	try {
		 		
				o2 = (JSONObject) parser.parse(o1.get(i).toString());
				
		 	} catch (Exception e) {
				return 0;
			}
		 	if(o2.get("uuid").toString().equals(uuid)){
	    		
		 		return i + 1;
		 	
		 	}
		}
		return 0;
	}

}
