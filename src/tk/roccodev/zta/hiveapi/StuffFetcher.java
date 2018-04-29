package tk.roccodev.zta.hiveapi;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tk.roccodev.zta.hiveapi.stuff.dr.DRMap;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;

public class StuffFetcher {

	private static final String BASE_URL = "https://roccodev.pw/beezighosting/files/";
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, DRMap> getDeathRunMaps(){
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(APIUtils.readURL(new URL(BASE_URL + "dr.json")));
			HashMap<String, DRMap> tr = new HashMap<String, DRMap>();
			obj.forEach((k, v) -> {
				
				String key = (String) k;
				JSONObject value = (JSONObject) v;
				
				DRMap map = new DRMap((long)value.get("checkpoints"), (String)value.get("speedrun"), key, (String)value.get("api"));
				
				tr.put(key.toLowerCase(), map);
			});
			
			return tr;
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
