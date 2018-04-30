package tk.roccodev.zta.briefing.fetcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tk.roccodev.zta.briefing.News;
import tk.roccodev.zta.briefing.Pools;
import tk.roccodev.zta.briefing.lergin.NewMap;
import tk.roccodev.zta.briefing.lergin.StaffChangeType;
import tk.roccodev.zta.briefing.lergin.StaffUpdate;
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
				if(postedAt < lastLogin) continue;
				
				tr.add(new News((String)j.get("title"), (String)j.get("content"), (long)j.get("postedAt")));
				
			}
			
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return tr;
	}
	
	public static ArrayList<NewMap> getApplicableNewMaps(long lastLogin){
		
		ArrayList<NewMap> tr = new ArrayList<NewMap>();
		
		try {
			JSONParser parser = new JSONParser();
			JSONArray arr = (JSONArray) parser.parse(APIUtils.readURL(new URL(Links.LERGIN_MAP)));
			
			for(Object o : arr) {
				JSONObject j = (JSONObject) o;
				
				long postedAt = (long) j.get("date");
				if(postedAt < lastLogin) continue;
				
				tr.add(new NewMap((String)j.get("gameType"), (String)j.get("mapName")));
			}
			
		} catch (Exception e) {
			
			Pools.error = true;
			e.printStackTrace();
		}
		if(tr.size() < 10) return tr;
		return new ArrayList<NewMap>(tr.subList(0, 10));
	}
	
	public static ArrayList<StaffUpdate> getApplicableStaffUpdates(long lastLogin){
		
		ArrayList<StaffUpdate> tr = new ArrayList<StaffUpdate>();
		
		try {
			JSONParser parser = new JSONParser();
			JSONArray arr = (JSONArray) parser.parse(APIUtils.readURL(new URL(Links.LERGIN_STAFF)));
			
			for(Object o : arr) {
				JSONObject j = (JSONObject) o;
				
				long postedAt = (long) j.get("date");
				if(postedAt < lastLogin) continue;
				
				StaffUpdate s = new StaffUpdate(StaffChangeType.valueOf((String)j.get("type")), (String)j.get("name"));
				
				tr.add(s);
			}
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tr.size() > 10) tr = new ArrayList<StaffUpdate>(tr.subList(0, 10));
		Collections.sort(tr, new Comparator<StaffUpdate>() {

			@Override
			public int compare(StaffUpdate o1, StaffUpdate o2) {
				// TODO Auto-generated method stub
				return o1.getType().compareTo(o2.getType());
			}
			
			
		});
		
		
		return tr;
	}
	
	
}
