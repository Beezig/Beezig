package tk.roccodev.zta.hiveapi;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tk.roccodev.zta.games.TIMV;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class HiveAPI {

	public static long GiantPoints = 0;
	
	public static long medals = 0;
	public static long tokens = 0;
	
	
	
	private static URL GameParsePlayerURL(String name, String game){
		String urls = "http://api.hivemc.com/v1/player/@player@/" + game;
		try {
			return new URL(urls.replaceAll("@player@", getUUID(name)));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static URL parsePlayerURLGeneric(String name){
		String urls = "http://api.hivemc.com/v1/player/@player@/";
		try {
			return new URL(urls.replaceAll("@player@", getUUID(name)));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static URL parseMojangPlayerAPI(String ign){
		String url = "https://api.mojang.com/users/profiles/minecraft/@ign@";
		try {
			return new URL(url.replaceAll("@ign@", ign));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static URL parseMonthlyURL(String game){
		//APIKey is only used for analytics => doesn't have to be kept secret; be aware that the endpoint URL might change in the future, there will be a notification prior to that
		String url = "https://thtmx.rocks/@game@/api/ighGH789fdf5kfHUo";
		try {
			return new URL(url.replaceAll("@game@", game));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static URL parseLeaderboardPlaceURL(int index, String game){
		String url = "http://api.hivemc.com/v1/game/@game@/leaderboard/" + index + "/" + (index + 1);
		try {
			return new URL(url.replaceAll("@game@", game));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//Giant
	public static void GiantupdatePoints(boolean mini) throws Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(playername, mini ? "GNTM" : "GNT")));
		
		GiantPoints =  (long) o.get("total_points");
	}
	public static String GiantgetRank(String ign, String mode){
		boolean mini = mode.equalsIgnoreCase("GNTM");
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, mini ? "GNTM" : "GNT")));
			} catch (Exception e) {
				e.printStackTrace();
				The5zigAPI.getLogger().info("Failed GiantgetRank");
			}
		
		return (String) o.get("title");
	}
	public static long GiantgetGamesPlayed(String ign, String mode){
		boolean mini = mode.equalsIgnoreCase("GNTM");
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, mini ? "GNTM" : "GNT")));
			} catch (Exception e) {
				e.printStackTrace();
				The5zigAPI.getLogger().info("Failed GiantgetGamesPlayed");
			}
		
		return (long) o.get("games_played");
	}
	public static long GiantgetPoints(String ign, String mode){
		boolean mini = mode.equalsIgnoreCase("GNTM");
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, mini ? "GNTM" : "GNT")));
			} catch (Exception e) {
				e.printStackTrace();
				The5zigAPI.getLogger().info("Failed GiantgetPoints");
			}
		
		return (long) o.get("total_points");
	}
	public static long GiantgetWins(String ign, String mode){
		boolean mini = mode.equalsIgnoreCase("GNTM");
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, mini ? "GNTM" : "GNT")));
			} catch (Exception e) {
				e.printStackTrace();
				The5zigAPI.getLogger().info("Failed GiantgetWins");
			}
		
		return (long) o.get("victories");
	}
	public static long GiantgetKills(String ign, String mode){
		boolean mini = mode.equalsIgnoreCase("GNTM");
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, mini ? "GNTM" : "GNT")));
			} catch (Exception e) {
				e.printStackTrace();
				The5zigAPI.getLogger().info("Failed GiantgetKills");
			}
		
		return (long) o.get("kills");
	}
	public static long GiantgetDeaths(String ign, String mode){
		boolean mini = mode.equalsIgnoreCase("GNTM");
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, mini ? "GNTM" : "GNT")));
			} catch (Exception e) {
				e.printStackTrace();
				The5zigAPI.getLogger().info("Failed GiantgetDeaths");
			}
		
		return (long) o.get("deaths");
	}

	public static long getKills(String ign, String game) {
		
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(HiveAPI.GameParsePlayerURL(ign, game)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return (long) o.get("kills");
	}
	public static long getDeaths(String ign, String game) {
		
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(HiveAPI.GameParsePlayerURL(ign, game)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return (long) o.get("deaths");
	}
	
	public static void updateMedals() throws Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();
		medals = getMedals(playername);
	}
	public static long getMedals(String ign) throws Exception{
		
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(parsePlayerURLGeneric(ign)));
		
		return (long) o.get("medals");
	}
	
	public static void updateTokens() throws Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();		
		tokens =  getTokens(playername);
	}
	public static long getTokens(String ign) throws Exception{
		
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(parsePlayerURLGeneric(ign)));
		
		return (long) o.get("tokens");
	}


	public static Date lastGame(String ign, String game){
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(GameParsePlayerURL(ign, game)));
			} catch (Exception e) {
				The5zigAPI.getLogger().info("Failed lastGame");
				e.printStackTrace();
			}
			long time = (long) o.get("lastlogin");
			return new Date(time * 1000);
	}

	public static String getUUID(String ign){
		if(ign.length() == 32) return ign;
		// ^ input is already a uuid
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		try {
			o = (JSONObject) parser.parse(readUrl(parseMojangPlayerAPI(ign)));
		}  catch (Exception e) {
			The5zigAPI.getLogger().info("Failed getUUID (Mojang)");
			e.printStackTrace();
		}		
		return (String) o.get("id");
	}
	
	public static String getMonthlyLeaderboardsPlayerInfo(int index, String game){
		String unit = "points";
		if(game.equals("TIMV")){
				unit = "karma";
		}	
		JSONParser parser = new JSONParser();
		JSONArray o1 = null;
		JSONObject o2 = null;
			try {
				o1 = (JSONArray) parser.parse(((JSONObject) parser.parse(readUrl(parseMonthlyURL(game.toLowerCase())))).get("leaderboard").toString());
			} catch (Exception e) {
				The5zigAPI.getLogger().info("Failed getMonthlyLBPlayerInfo (thtmx.rocks) (JSON 1)");
				e.printStackTrace();
			}
			try {
				o2 = (JSONObject) parser.parse(o1.get(index).toString());
				if(game.equals("TIMV")){
					// Calculating K/R
					Long rp = Long.valueOf(o2.get("innocent").toString()) + Long.valueOf(o2.get("traitor").toString()) + Long.valueOf(o2.get("detective").toString());
					Double krr = (double)Math.round( Double.valueOf(o2.get(unit).toString()) / rp.doubleValue() * 100D) / 100D;
					Double tratio = Math.round(((double)Long.valueOf(o2.get("traitor").toString()) / (double)rp) * 1000d) / 10d;
					ChatColor ratioColor = ChatColor.GOLD;
					if(tratio >= TIMV.TRATIO_LIMIT){
						ratioColor = ChatColor.RED;
					}
					return o2.get("name").toString() + "," + o2.get(unit).toString() + "," + krr.toString() + "," + ratioColor + tratio.toString();
				}
				if(game.equals("DR")){
					
					Integer winr = (int) (Math.floor((Double.valueOf(o2.get("wins").toString()) / Double.valueOf(o2.get("games").toString())) * 1000) / 10);
					Double ppg = Math.round(((Long)o2.get("points") / (Long)o2.get("games")) * 10d) / 10d;

					return o2.get("name").toString() + "," + o2.get(unit).toString() + "," + ppg.toString() + "," + winr.toString();
				}
				return "Error.";
				
			} catch (Exception e) {
				The5zigAPI.getLogger().info("Failed getMonthlyLBPlayerInfo (thtmx.rocks) (JSON 2)");
				e.printStackTrace();
			}	
		return "";
	}
	
	public static String getLeaderboardsPlaceHolder(int index, String game){
		JSONParser parser = new JSONParser();
		JSONObject o1 = null;
		try {
			o1 = (JSONObject) parser.parse(((JSONArray) parser.parse(((JSONObject) parser.parse(readUrl(HiveAPI.parseLeaderboardPlaceURL(index, game)))).get("leaderboard").toString())).get(0).toString());
		} catch (Exception e) {
			The5zigAPI.getLogger().info("Failed getLBPlacePoints (JSON 0)");
			e.printStackTrace();
		}
		try {
				return (String) o1.get("username").toString();
			} catch (Exception e) {
				The5zigAPI.getLogger().info("Failed getLBPlacePoints (JSON 1)");
				e.printStackTrace();
				return null;
			}
		
		
	}
		
	private static String readUrl(URL url) throws Exception {
	    BufferedReader reader = null;
	    try {
	       URLConnection conn = url.openConnection();
	       conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36(KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
	        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        
	        return buffer.toString();
	    } catch(Exception e){
	    	
	    	e.printStackTrace();
			return null;
	    } finally {
	        if (reader != null)
	            reader.close();
	    }

	}

}
