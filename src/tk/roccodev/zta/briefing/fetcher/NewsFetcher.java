package tk.roccodev.zta.briefing.fetcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tk.roccodev.zta.briefing.News;
import tk.roccodev.zta.games.CAI;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;

public class NewsFetcher {

	
	
	public static ArrayList<News> getApplicableNews(long lastLogin){
		
		ArrayList<News> tr = new ArrayList<News>();
		
		try {
			JSONParser parser = new JSONParser();
			JSONArray arr = (JSONArray) parser.parse(APIUtils.readURL(new URL(Links.OUR_NEWS)));
			
			for(Object o : arr) {
				JSONObject j = (JSONObject)o;
				
				long postedAt = (long) j.get("postedAt");
				System.out.println(postedAt + " / " + lastLogin);
				if(postedAt < lastLogin) continue;
				
				tr.add(new News((String)j.get("title"), (String)j.get("content"), (long)j.get("postedAt")));
				
			}
			
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return tr;
	}
	
	
}
