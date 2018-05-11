package tk.roccodev.zta.hiveapi.stuff.grav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.GRAV;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.ChatComponentUtils;

public class GRAVListenerv2 {
	
	@EventHandler
	public void onServerChat(ChatEvent evt) {
		if(evt.getMessage().startsWith("§8▍ §bGra§avi§ety§8 ▏ §6§e§e§l") && ActiveGame.is("grav") && !GRAV.hasVoted && Setting.AUTOVOTE.getValue()) {
			Pattern p = Pattern.compile(Pattern.quote("§6§e§e§l") + "(.*?)" + Pattern.quote(".§f§6"));
			Matcher matcher = p.matcher(evt.getMessage());
			String index = "";
			while(matcher.find()) {
				index = matcher.group(1);
			}
			String componentMaps = ChatComponentUtils.getHoverEventValue(evt.getChatComponent().toString());
			String[] maps = componentMaps.split("\n");
			ArrayList<String> mapsL = new ArrayList<String>();
			for(String s : maps) {
				mapsL.add(ChatColor.stripColor(s.replaceFirst("\\-", "").trim().replace(' ', '_').toUpperCase()));
			}
			
			GRAV.mapsToParse.put(Integer.parseInt(index), mapsL);
			
			if(evt.getMessage().startsWith("§8▍ §bGra§avi§ety§8 ▏ §6§e§e§l5.§f§6")) {
				
				HashMap<Integer, Integer> indexFavorites = new HashMap<Integer, Integer>();
				List<String> mapsav = AutovoteUtils.getMapsForMode("grav");
				
				for(Map.Entry<Integer,ArrayList<String>> e : GRAV.mapsToParse.entrySet()) {
					int groupFavs = 0;
					ArrayList<String> v = e.getValue();
					for(String s : v) {
						
						if(mapsav.contains(s)) groupFavs++;
					}
					indexFavorites.put(e.getKey(), groupFavs);
				}
				Map.Entry<Integer, Integer> maxEntry = null;
				for(Map.Entry<Integer, Integer> e : indexFavorites.entrySet()) {
					
					if(maxEntry == null || e.getValue() > maxEntry.getValue()) maxEntry = e;
					
				}
				
				GRAV.hasVoted = true;
				
				if(maxEntry.getValue() == 0) {
					System.out.println("Found absolutely no favorites.");
					return;
				}
				
				final String index1 = maxEntry.getKey() + "";
				
				//Voting
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						The5zigAPI.getAPI().sendPlayerMessage("/v " + index1);
						The5zigAPI.getAPI().messagePlayer("§8▍ §bGra§avi§ety§8 ▏ §eAutomatically voted for map §6#" + index1);
					}
				}).start();
				
				
				
			}
				
			
			
		}
	}
	
	
}
