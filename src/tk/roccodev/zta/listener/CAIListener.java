package tk.roccodev.zta.listener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.CAI;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIMap;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiCAI;
import tk.roccodev.zta.settings.Setting;

public class CAIListener extends AbstractGameListener<CAI> {

	@Override
	public Class<CAI> getGameMode() {
		return CAI.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("CAI");
	}

	@Override
	public void onGameModeJoin(CAI gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("CAI");
		IHive.genericJoin();
		
		new Thread(new Runnable(){

			@Override
			public void run(){
				try {

					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());
					
					if(sb != null && sb.getTitle().contains("Your CAI Stats")){
						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						APIValues.CAIpoints = (long) points;
					}

					CAI.rankObject = CAIRank.getFromDisplay(new ApiCAI(The5zigAPI.getAPI().getGameProfile().getName()).getTitle());
					CAI.rank = CAI.rankObject.getTotalDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	
	}



	@Override
	public boolean onServerChat(CAI gameMode, String message) {

		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("CAI Color Debug: (" + message + ")");
		}

		if(message.startsWith("§8▍ §6CaI§8 ▏ §3Voting has ended! §bThe map §f")){
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §6CaI§8 ▏ §3Voting has ended! §bThe map ")[1];
			String map = "";
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
			CAI.activeMap = CAIMap.getFromDisplay(map);
		}

		//Autovoting

		else if(message.startsWith("§8▍ §6CaI§8 ▏ §a§lVote received. §3Your map now has ") && Setting.AUTOVOTE.getValue()){
			CAI.hasVoted = true;
		}
		else if(message.startsWith("§8▍ §6CaI§8 ▏ §6§e§e§l6. §f§6") && !CAI.hasVoted && Setting.AUTOVOTE.getValue()){
			CAI.votesToParse.add(message);
			new Thread(new Runnable(){
				@Override
				public void run(){
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(CAI.votesToParse);
					List<CAIMap> parsedMaps = new ArrayList<CAIMap>();

					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					for(String s1 : AutovoteUtils.getMapsForMode("cai")){
						CAIMap map1 = CAIMap.valueOf(s1);
						if(map1 == null) continue;
						parsedMaps.add(map1);
						The5zigAPI.getLogger().info("Parsed " + map1);
					}

					for(String s : votesCopy){

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §6CaI§8 ▏ §6§e§e§l", "").replaceAll("▍ CaI ▏", "").trim();
						String[] toConsider = ChatColor.stripColor(data[1]).split("\\[");
						String consider = ChatColor.stripColor(toConsider[0]).trim();
						CAIMap map = CAIMap.getFromDisplay(consider);
						String votes = toConsider[1].split(" ")[0].trim();

						if(map == null){
							The5zigAPI.getAPI().messagePlayer(Log.error + "Error while autovoting: map not found for " + consider);
						}
						The5zigAPI.getLogger().info("trying to match " + map);
						if(parsedMaps.contains(map)){
							votesindex.add(votes + "-" + index);
							The5zigAPI.getLogger().info("Added " + map + " Index #" + index + " with " + votes + " votes");
						}else{
							The5zigAPI.getLogger().info(map + " is not a favourite");
						}

						The5zigAPI.getLogger().info("\"" + index + "\"");

						if(index.equals("5")){
							if(votesindex.size() != 0){
								for(String n : votesindex){
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);

								CAI.votesToParse.clear();
								CAI.hasVoted = true;
								//we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer("§8▍ §6CaI§8 ▏ " + "§eAutomatically voted for map §6#" + finalindex);
								return;
							}
							else{
								The5zigAPI.getLogger().info("Done, couldn't find matches");
							
								CAI.votesToParse.clear();
								CAI.hasVoted = true;
								//he hasn't but we don't want to check again and again
								return;
							}
						}
					}
				}
			}).start();
		}
		else if(message.startsWith("§8▍ §6CaI§8 ▏ §6§e§e§l") && !CAI.hasVoted && Setting.AUTOVOTE.getValue()){
			CAI.votesToParse.add(message);
		}

		return false;
		
	}

	@Override
	public void onTitle(CAI gameMode, String title, String subTitle) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("CAI TitleColor Debug: (" +

					title != null ? title : "ERR_TITLE_NULL"

						+ " *§* " +


					subTitle != null ? subTitle : "ERR_SUBTITLE_NULL"

						+ ")"
					);
		}
	}

	@Override
	public void onServerConnect(CAI gameMode) {
		CAI.reset(gameMode);
	}

}
