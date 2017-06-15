package tk.roccodev.zta.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.BEDMap;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class BEDListener extends AbstractGameListener<BED>{

	@Override
	public Class<BED> getGameMode() {
		// TODO Auto-generated method stub
		return BED.class;
	}

	
	
	@Override
	public boolean matchLobby(String arg0) {
		// TODO Auto-generated method stub
		return arg0.equals("BED");
	}

	@Override
	public void onGameModeJoin(BED gameMode) {
		

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("BED");
		new Thread(new Runnable(){
			
			@Override
			public void run(){
				try {
					Thread.sleep(200);
					HiveAPI.BEDupdatePoints();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
		
		
	}
	
	

	@Override
	public boolean onServerChat(BED gameMode, String message) {
		
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("BedWars Color Debug: (" + message + ")");
		}
		//§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map §fEthereal§b has won!
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map")){
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map")[1];
			String map = "";    
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    BEDMap map1 = BEDMap.getFromDisplay(map);
		    BED.activeMap = map1;			
		}
		
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §a§lVote received.")){
			BED.hasVoted = true;
		}
		
		else if(message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l6. ")){
			BED.votesToParse.add(message);
			//Adding the 6th option, the normal method doesn't work
			new Thread(new Runnable(){
				@Override
				public void run(){
					
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(BED.votesToParse);
					List<String> mapNames = new ArrayList<String>();
					List<BEDMap> parsedMaps = new ArrayList<BEDMap>();
					for(String s1 : AutovoteUtils.getMapsForMode("bed")){
						BEDMap map1 = BEDMap.valueOf(s1);					
						if(map1 == null) continue;
						parsedMaps.add(map1);
						The5zigAPI.getLogger().info("Parsed " + map1);
					}			
					for(String s : votesCopy){
						
						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l", "").replaceAll("▍ BedWars ▏", "").trim();
						String toConsider = data[1];
						String[] data2 = ChatColor.stripColor(toConsider).split("\\[");
						String consider = data2[0].trim();
						
						BEDMap map = BEDMap.getFromDisplay(consider);
						if(map == null){
							The5zigAPI.getAPI().messagePlayer(Log.error + "Error while autovoting: Map not found for " + consider);
							return;
						}
						The5zigAPI.getLogger().info("trying to match " + map);
						if(parsedMaps.contains(map)){
							
							The5zigAPI.getAPI().sendPlayerMessage("/vote " + index);
							BED.votesToParse.clear();
							BED.hasVoted = true;
							The5zigAPI.getAPI().messagePlayer(Log.info + "Automatically voted for §6" + map.getDisplayName());
							return;
						}
						else{
							The5zigAPI.getLogger().info("no matches in parsedMaps (yet)");
						}
						if(index.equals("6")){
							The5zigAPI.getLogger().info("Done, couldn't find matches");
							BED.votesToParse.clear();
							BED.hasVoted = true;
							//he hasn't but we don't want to check again and again
							return;
						}
				
					}
			
				}
			}).start();
		}
		//	
		else if(message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l") && !BED.hasVoted){		
			BED.votesToParse.add(message);
		}
		
		return false;
			
	}

	@Override
	public void onTitle(BED gameMode, String title, String subTitle) {
		if(subTitle != null && subTitle.equals("§r§7Protect your bed, destroy others!§r")){
			gameMode.setState(GameState.GAME);
			
			//As Hive sends this subtitle like 13 times, don't do anything here please :)
		}
		
	}

	@Override
	public void onServerConnect(BED gameMode) {
		BED.reset(gameMode);
	}
	
	
	

	

}
