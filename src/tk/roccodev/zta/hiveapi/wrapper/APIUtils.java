package tk.roccodev.zta.hiveapi.wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import eu.the5zig.mod.The5zigAPI;

public class APIUtils {

	public static String getUUID(String ign){
		if(ign.length() == 32) return ign;
		// ^ input is already a uuid
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		try {
			o = (JSONObject) parser.parse(Parser.read(Parser.mojang(ign)));
		}  catch (Exception e) {
			The5zigAPI.getLogger().info("Failed getUUID (Mojang)");
			e.printStackTrace();
		}		
		return (String) o.get("id");
	}
	
	public static JSONObject getObject(String toParse){
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		try {
			o = (JSONObject) parser.parse(toParse);
		}  catch (Exception e) {
			The5zigAPI.getLogger().info("Failed");
			e.printStackTrace();
		}
		return o;
	}

	static class Parser {
		
		
		public static URL mojang(String ign){
			String url = "https://api.mojang.com/users/profiles/minecraft/@ign@";
			try {
				return new URL(url.replaceAll("@ign@", ign));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public static URL game(String uuid, String game){
			
			String url = "http://api.hivemc.com/v1/player/@player@/" + game;
			if(game.isEmpty()) url = "http://api.hivemc.com/v1/player/@player@";
			try {
				return new URL(url.replaceAll("@player@", uuid));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public static URL hiveLB(int index, String game){
			String url = "http://api.hivemc.com/v1/game/@game@/leaderboard/" + index + "/" + (index + 1);
			try {
				return new URL(url.replaceAll("@game@", game));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public static URL monthlyLB(String game){
			String url = "https://thtmx.rocks/@game@/api/ighGH789fdf5kfHUo";
			try {
				return new URL(url.replaceAll("@game@", game));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public static String read(URL url){
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
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
		}
	
	
	}
	
}
