package tk.roccodev.zta.hiveapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.the5zig.mod.The5zigAPI;

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
	
	
	public static void updateKarma() throws ParseException, Exception{
		String playername = The5zigAPI.getAPI().getGameProfile().getName();
		JSONParser parser = new JSONParser();
		JSONObject o = null;
		
			o = (JSONObject) parser.parse(readUrl(parsePlayerURL(playername)));
		
		karma =  (long) o.get("total_points");
		
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
