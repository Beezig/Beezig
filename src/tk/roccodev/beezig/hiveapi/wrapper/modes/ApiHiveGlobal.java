package tk.roccodev.beezig.hiveapi.wrapper.modes;

import org.json.simple.JSONObject;

import eu.the5zig.mod.server.GameMode;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.hiveapi.wrapper.APIGameMode;
import tk.roccodev.beezig.hiveapi.wrapper.NetworkRank;

public class ApiHiveGlobal extends APIGameMode {

	public ApiHiveGlobal(String playerName, String... UUID) {
		super(playerName, UUID);
		
	}

	
	
	@Override
	public Class<? extends GameMode> getGameMode() {
		// TODO Auto-generated method stub
		return GameMode.class;
	}

	@Override
	public String getShortcode() {
		// TODO Auto-generated method stub
		return "";
	}
	
	
	
	public String getNetworkTitle(){
		JSONObject o = (JSONObject) object("modernRank");
		return (String) o.get("human");
	}
	
	public ChatColor getNetworkRankColor(){
		return NetworkRank.fromDisplay(getNetworkTitle()).getColor();
	}
	
	public String getCorrectName(){
		return (String) object("username");
	}
	
	public long getLastLogout(){
		return (long) object("lastLogout");
	}
	
	public String getPlayerLocation(){
		//?
		JSONObject o = (JSONObject) object("status");
		
		if(o.get("game").toString().equals("Hubs"))	{
			return "Hub";
		}
		return (String) o.get("game");
	}
	
	public boolean isOnline() {
		return !getPlayerLocation().equals("the Land of Nods!");
	}
	
	

}
