package tk.roccodev.zta.hiveapi.wrapper;

import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
	
	public static String getTimeAgo(long time) {
		
		/*
		 * Copyright 2012 Google Inc.
		 *
		 * Licensed under the Apache License, Version 2.0 (the "License");
		 * you may not use this file except in compliance with the License.
		 * You may obtain a copy of the License at
		 *
		 *      http://www.apache.org/licenses/LICENSE-2.0
		 *
		 * Unless required by applicable law or agreed to in writing, software
		 * distributed under the License is distributed on an "AS IS" BASIS,
		 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		 * See the License for the specific language governing permissions and
		 * limitations under the License.
		 */
		
		if (time < 1000000000000L) {
		        // if timestamp given in seconds, convert to millis
		        time *= 1000;
		    }
		
		int SECOND_MILLIS = 1000;
		int MINUTE_MILLIS = 60 * SECOND_MILLIS;
		int HOUR_MILLIS = 60 * MINUTE_MILLIS;
		int DAY_MILLIS = 24 * HOUR_MILLIS;
		long MONTH_MILLIS = 30L * DAY_MILLIS;
		long YEAR_MILLIS = 12L * MONTH_MILLIS;
	    long now = System.currentTimeMillis();
	    
	    if (time > now || time <= 0) {
	        return null;	
	    }
	    
	    final long diff = now - time;

	    if (diff < MINUTE_MILLIS) {
	        return "Just now";
	    } else if (diff < 2 * MINUTE_MILLIS) {
	        return "A minute ago";
	    } else if (diff < 50 * MINUTE_MILLIS) {
	        return diff / MINUTE_MILLIS + " minutes ago";
	    } else if (diff < 90 * MINUTE_MILLIS) {
	        return "An hour ago";
	    } else if (diff < 24 * HOUR_MILLIS) {
	        return diff / HOUR_MILLIS + " hours ago";
	    } else if (diff < 48L * HOUR_MILLIS) {
	        return "Yesterday";
	    } else if (diff < 29L * DAY_MILLIS){
	        return diff / DAY_MILLIS + " days ago";
	    } else if (diff < 2L * MONTH_MILLIS){
		    return "1 month ago";
	    } else if (diff < 11L * MONTH_MILLIS){
	        return diff / MONTH_MILLIS + " months ago";
	    }  else if (diff < 2L * YEAR_MILLIS){
		    return "1 year ago";  
	    } else {
	    	return diff / YEAR_MILLIS + " years ago";
	    }
	}
	
	public static String getTimePassed(long time) {
		
		
		if (time < 1000000000000L) {
		        // if timestamp given in seconds, convert to millis
		        time *= 1000;
		    }
		
		if (time < 0) {
	        // HiveAPI error
	        return "Error";
	    }
		
		
		int SECOND_MILLIS = 1000;
		int MINUTE_MILLIS = 60 * SECOND_MILLIS;
		int HOUR_MILLIS = 60 * MINUTE_MILLIS;
		int DAY_MILLIS = 24 * HOUR_MILLIS;
		long MONTH_MILLIS = 30L * DAY_MILLIS;

	    if (time < MINUTE_MILLIS) {
	        return "Less a minute";
	    } else if (time < 2 * MINUTE_MILLIS) {
	        return "A minute";
	    } else if (time < 50 * MINUTE_MILLIS) {
	        return time / MINUTE_MILLIS + " minutes";
	    } else if (time < 90 * MINUTE_MILLIS) {
	        return "An hour";
	    } else if (time < 24 * HOUR_MILLIS) {
	        return time / HOUR_MILLIS + " hours";
	    } else if (time < 48L * HOUR_MILLIS) {
	        return "A day";
	    } else if (time < 29L * DAY_MILLIS){
	        return time / DAY_MILLIS + " days";
	    } else if (time < 2L * MONTH_MILLIS){
		    return "1 month";
	    } else {
	        return time / MONTH_MILLIS + " months";
	    }
	}
	
	public static JSONObject getSpeedrunObject(String toParse, int mode){
		return getObject(Parser.read(Parser.speedrun(toParse, mode)));
	}
	
	public static String readURL(URL url){
		return Parser.read(url);
	}
	
	public static URL speedrunPublic(String id, int mode){
		return Parser.speedrun(id, mode);
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
		
		/**
		 * 
		 * Parses url from speedrun.com
		 * 
		 * @param mapId for mode 0, or userId for mode 1
		 * @param 0 for map parsing, 1 for user parsing
		 * @return the parsed URL
		 */
		public static URL speedrun(String param, int mode){
			String url = "";
			switch(mode){
			case 0:
				url =  "http://www.speedrun.com/api/v1/leaderboards/369ep8dl/level/@id@/824xzvmd?top=1";
				break;
			case 1:
				url = "http://www.speedrun.com/api/v1/users/@id@";
			}
			try {
				return new URL(url.replaceAll("@id@", param));
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
	
		/* LEGACY
		 			
	
	
	//TIMV
	public static void TIMVupdateKarma() throws ParseException, Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(TIMVparsePlayerURL(playername)));
		
		TIMVkarma =  (long) o.get("total_points");
		
		
	}
		 
		 
		 
		 */
	}
	
}
