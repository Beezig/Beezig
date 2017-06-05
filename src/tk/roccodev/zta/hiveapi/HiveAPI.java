package tk.roccodev.zta.hiveapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public class HiveAPI {
	
	public static long karma = 0;
	
	
	private static URL parsePlayerURL(String name){
		String urls = "http://api.hivemc.com/v1/player/@player@/TIMV";
		try {
			return new URL(urls.replaceAll("@player@", name));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static URL parsePlayerURLGeneric(String name){
		String urls = "http://api.hivemc.com/v1/player/@player@/";
		try {
			return new URL(urls.replaceAll("@player@", name));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public static void updateKarma() throws ParseException, Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
		
		karma =  (long) o.get("total_points");
		
		
	}
	
	public static long getKarmaPerGame(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return (long) o.get("most_points");
	}
	
	public static String getNetworkRank(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURLGeneric(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "Either nicked player, player not found or connection error.";
			}
		
		return (String) o.get("rankName");
	}
	public static String getName(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURLGeneric(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "Either nicked player, player not found or connection error.";
			}
		
		return (String) o.get("username");
	}
	
	public static String getRank(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return (String) o.get("title");
	}
	
	public static int getAchievements(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> keys = o.keySet().iterator();

	        while( keys.hasNext() ){
	            String key = (String)keys.next();
	            Object value = o.get(key);
	            map.put(key, value);

	        }
	       
	       JSONObject o2 = (JSONObject) map.get("achievements");
	       
	       return o2.keySet().size() - 1;
	}
	
	public static long getRolepoints(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return (long) o.get("role_points");
	}
	public static long getKarma(String ign){
		String playername = ign;
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			try {
				o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return (long) o.get("total_points");
	}
	public static ChatColor getRankColor(String rankName){
		ChatColor rankColor = null;
		switch(rankName){
		case "Regular Hive Member": rankColor = ChatColor.BLUE;
			break;
		case "Gold Hive Member": rankColor = ChatColor.GOLD;
			break;
		case "Diamond Hive Member": rankColor = ChatColor.AQUA;
			break;
		case "Lifetime Emerald Hive Member": rankColor = ChatColor.GREEN;
			break;
		case "VIP Player": rankColor = ChatColor.DARK_PURPLE;
			break;
		case "Hive Moderator": rankColor = ChatColor.RED;
			break;
		case "Senior Hive Moderator": rankColor = ChatColor.DARK_RED;
			break;
		case "Hive Developer": rankColor = ChatColor.GRAY;
			break;
		case "Hive Founder and Owner": rankColor = ChatColor.YELLOW;
			break;
		default: rankColor = ChatColor.DARK_RED; //AFAIK custom names are Senior Moderators
			break;
		}
		return rankColor;
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
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}

	
	
	
}
