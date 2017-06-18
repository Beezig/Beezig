package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import tk.roccodev.zta.hiveapi.BEDRank;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.settings.Setting;

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
					BED.rank = BEDRank.getRank(HiveAPI.BEDpoints).getName().replaceAll(ChatColor.stripColor(BEDRank.getRank(HiveAPI.BEDpoints).getName()), "") + BED.NUMBERS[BEDRank.getRank(HiveAPI.BEDpoints).getLevel((int)HiveAPI.BEDpoints)] + " " + BEDRank.getRank((int)HiveAPI.BEDpoints).getName();
					//Should've read the docs ¯\_(ツ)_/¯
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

		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained 10§a points for killing")){
			
			BED.kills++;
			BED.pointsCounter += 10;
			
		}
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained 5§a points for killing")){
			
			BED.kills++;
			BED.pointsCounter += 5;
			
		}
		
		//Advanced Records
		
		else if(message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")){
			BED.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		}
		else if(message.startsWith("§3 ")){
			
				BED.messagesToSend.add(message);
				The5zigAPI.getLogger().info("found entry");
			
			return true;	
		}
		else if(message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")){
			BED.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");
			
			return true;	
		}
		else if((message.equals("                      §6§m                  §6§m                  ")&& !message.startsWith("§o "))){
			The5zigAPI.getLogger().info("found footer");
			BED.footerToSend.add(message);	
			The5zigAPI.getLogger().info("executed /records");
			if(BED.footerToSend.contains("                      §6§m                  §6§m                  ")){
				//Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable(){
					@Override
					public void run(){
						BED.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try{
						Double epg = Setting.BED_SHOW_ELIMINATIONS_PER_GAME.getValue() ? (double) Math.floor(((double)HiveAPI.BEDgetTeamsEliminated(BED.lastRecords) / (double)HiveAPI.BEDgetGamesPlayed(BED.lastRecords) * 10d)) / 10d  : null;
						Double bpg = Setting.BED_SHOW_BEDS_PER_GAME.getValue() ? (double) Math.floor(((double)HiveAPI.BEDgetBedsDestroyed(BED.lastRecords) / (double)HiveAPI.BEDgetGamesPlayed(BED.lastRecords) * 10d)) / 10d  : null;
						Double dpg = Setting.BED_SHOW_DEATHS_PER_GAME.getValue() ? (double) Math.floor(((double)HiveAPI.BEDgetDeaths(BED.lastRecords) / (double)HiveAPI.BEDgetGamesPlayed(BED.lastRecords) * 10d)) / 10d  : null;
						Double kpg = Setting.BED_SHOW_KILLS_PER_GAME.getValue() ? (double) Math.floor(((double)HiveAPI.BEDgetKills(BED.lastRecords) / (double)HiveAPI.BEDgetGamesPlayed(BED.lastRecords) * 10d)) / 10d  : null;
						Double ppg = Setting.BED_SHOW_POINTS_PER_GAME.getValue() ? (double) Math.floor(((double)HiveAPI.BEDgetPoints(BED.lastRecords) / (double)HiveAPI.BEDgetGamesPlayed(BED.lastRecords) * 10d)) / 10d  : null;
						Double kd = Setting.BED_SHOW_KD.getValue() ? (double) Math.floor(((double)HiveAPI.BEDgetKills(BED.lastRecords) / (double)HiveAPI.BEDgetDeaths(BED.lastRecords) * 100d)) / 100d  : null;
						Integer winr = Setting.BED_SHOW_WINRATE.getValue() ? (int) (Math.floor(((double)HiveAPI.BEDgetVictories(BED.lastRecords) / (double)HiveAPI.BEDgetGamesPlayed(BED.lastRecords)) * 1000d) / 10d) : null;
						String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? HiveAPI.getNetworkRank(BED.lastRecords) : "";
						ChatColor rankColor = null;
						if(Setting.SHOW_NETWORK_RANK_COLOR.getValue()){
							if(rankTitle.isEmpty()){
								rankColor = HiveAPI.getRankColorFromIgn(BED.lastRecords);
							}
							else{
								rankColor = HiveAPI.getRankColor(rankTitle);
							}
						}
						long points = 0;
						
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? HiveAPI.lastGame(BED.lastRecords, "BED") : null;
						Integer achievements = Setting.BED_SHOW_ACHIEVEMENTS.getValue() ? HiveAPI.BEDgetAchievements(BED.lastRecords) : null;

						
						
						
						

						//int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() && HiveAPI.getLeaderboardsPlacePoints(349, "BED") < HiveAPI.DRgetPoints(BED.lastRecords)) ? HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;
						List<String> messages = new ArrayList<String>();
						messages.addAll(BED.messagesToSend);
							Iterator<String> it = messages.iterator();
							for(String s : messages){
								
								
								
								 	if(s.trim().endsWith("'s Stats §6§m")){
								 	The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = HiveAPI.getName(BED.lastRecords);
									if(correctUser.contains("nicked player")) correctUser = "Nicked/Not found";
									sb.append("          §6§m                  §f ");
									The5zigAPI.getLogger().info("Added base...");
									if(rankColor != null) {
										sb.append(rankColor + correctUser);
										The5zigAPI.getLogger().info("Added colored user...");
									}
									else{
										sb.append(correctUser);
										The5zigAPI.getLogger().info("Added white user...");
									}
									sb.append("§f's Stats §6§m                  ");
									The5zigAPI.getLogger().info("Added end...");
									The5zigAPI.getAPI().messagePlayer("§o " + sb.toString());
									
									if(rankTitle != null && rankTitle.contains("nicked player")) rankTitle = "Nicked/Not found";
									if(!rankTitle.equals("Nicked/Not found") && !rankTitle.isEmpty()){
											if(rankColor == null) rankColor = ChatColor.WHITE;
											The5zigAPI.getAPI().messagePlayer("§o           " + "§6§m       §6" + " (" + rankColor + rankTitle + "§6) " + "§m       ");
										}
									continue;
								 	}
									else if(s.startsWith("§3 Points: §b")){
										StringBuilder sb = new StringBuilder();
										sb.append("§3 Points: §b");
										points = Long.parseLong(s.replaceAll("§3 Points: §b", ""));
										sb.append(points);

										if(Setting.BED_SHOW_RANK.getValue()){
											BEDRank rank = BEDRank.getRank((int)points);
											if(rank != null){
												int level = rank.getLevel((int)points);
												String BEDrankColor = rank.getName().replaceAll(ChatColor.stripColor(rank.getName()), "");
												String rankString = BED.NUMBERS[level] + " " +rank.getName();
												sb.append(" (" + BEDrankColor + rankString);
												if(Setting.BED_SHOW_POINTS_TO_NEXT_RANK.getValue()){
													sb.append(" / " + rank.getPointsToNextRank((int)points));
												}
												sb.append("§b)");
											}
											
											
											
										}
										//if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

										The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
										continue;
									
									}

								The5zigAPI.getAPI().messagePlayer("§o " + s);
								
							}
						
						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "");
																											//^ API Achievements vs ingame - currently bad
						}
						// "§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained 10§a points for killing"
						
						if(epg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Eliminations per Game: §b" + epg);
						}	
						if(bpg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Beds per Game: §b" + bpg);
						}
						if(dpg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Deaths per Game: §b" + dpg);
						}
						if(kpg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Kills per Game: §b" + kpg);
						}
						if(ppg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + ppg);
						}
						if(kd != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 K/D: §b" + kd);
						}
						if(winr != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + winr + "%");
						}
					/*	if(monthlyRank != 0){					
					 *		The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
					 *	}
					 */		
						if(lastGame != null){
							Calendar lastSeen = Calendar.getInstance();
							lastSeen.setTimeInMillis(HiveAPI.lastGame(BED.lastRecords, "BED").getTime());
							
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Last Game: §b" + HiveAPI.getTimeAgo(lastSeen.getTimeInMillis()));
						}
						
							
							for(String s : BED.footerToSend){								
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
						
						
						
							BED.messagesToSend.clear();
							BED.footerToSend.clear();
							BED.isRecordsRunning = false;
						
						}catch(Exception e){
							e.printStackTrace();
							if(e.getCause() instanceof FileNotFoundException){
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								BED.messagesToSend.clear();
								BED.footerToSend.clear();
								BED.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");
							
							for(String s : BED.messagesToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for(String s : BED.footerToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer("§o " + "                      §6§m                  §6§m                  ");
							BED.messagesToSend.clear();
							BED.footerToSend.clear();
							BED.isRecordsRunning = false;
							return;
						}
					}
				}).start();
				return true;
				
				
			}
			
		}
	
		
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §a§lVote received.")){
			BED.hasVoted = true;
		}
		
		else if(message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l6. ") && !BED.hasVoted){
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

		else if(message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l") && !BED.hasVoted){		
			BED.votesToParse.add(message);
		}
		
		else if(message.contains("§lYou are on ")){
			//"                        §6§lYou are on Gold Team!"
            //§9§lYou are on Blue Team!
			String team = null;
			Pattern pattern = Pattern.compile(Pattern.quote("on ") + "(.*?)" + Pattern.quote(" Team!"));
			Matcher matcher = pattern.matcher(message.trim());
			while (matcher.find()) {
			        team = matcher.group(1);
			}
			try{
				String teamColor = team.replaceAll(" ", "_");
				switch(teamColor){
				//converting Hive-Team-Color-Names into actual color tag strings
					case "Magenta" : teamColor = "light_purple"; break;
					case "Purple" : teamColor = "dark_purple"; break;
					default : break;
				}
				BED.team = ChatColor.valueOf(teamColor.toUpperCase()) + team.replaceAll("_", " ");
			}
			catch(Exception e){
				e.printStackTrace();
				The5zigAPI.getAPI().messagePlayer(Log.error + "Couldn't find your team color <" + team + ">");
			}
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
