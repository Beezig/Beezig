package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.hiveapi.GiantMap;
import tk.roccodev.zta.hiveapi.GiantRank;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.settings.Setting;

public class GiantListener extends AbstractGameListener<Giant>{

	private Class<Giant> gameMode = Giant.class;
	private Giant instance = Giant.instance;
	public static GiantListener listener;
	private String lobby;
	
	public GiantListener(){
		listener = this;
	}
	
	
	@Override
	public Class<Giant> getGameMode() {
		// TODO Auto-generated method stub
		return Giant.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
	
		if(arg0.toUpperCase().startsWith("GNT")){ // Support for GNTM
			lobby = arg0;
			return true;
		} 
		else{
			return false;
		}
	}
	
	public void setGameMode(Class<? extends Giant> newMode, Giant instance){
		gameMode = (Class<Giant>) newMode;
		this.instance = instance;
		The5zigAPI.getLogger().info(instance.getClass());
		
	}

	@Override
	public void onGameModeJoin(Giant gameMode) {
		if(this.lobby.equalsIgnoreCase("GNT")) ActiveGame.set("GNT");
		if(this.lobby.equalsIgnoreCase("GNTM")) ActiveGame.set("GNTM");
		gameMode.setState(GameState.STARTING);
		IHive.genericJoin();
		new Thread(new Runnable(){
			@Override
			public void run(){
				String ign = The5zigAPI.getAPI().getGameProfile().getName();
				Giant.totalKills = (int) HiveAPI.getKills(ign, ActiveGame.current());
				Giant.totalDeaths = (int) HiveAPI.getDeaths(ign, ActiveGame.current());
				
				
				Giant.totalKdr = (double)Giant.totalKills / Giant.totalDeaths;
				Giant.gameKdr = new Double(Giant.totalKdr);
				The5zigAPI.getLogger().info(Giant.totalKdr);
				
			}
		}).start();
		
		The5zigAPI.getLogger().info(instance.getName());
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
					
						
						
							try {
								
								HiveAPI.GiantupdatePoints(instance.isMini());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
				
			
		}}).start();
	}

	
	
	

	@Override
	public boolean onServerChat(Giant gameMode, String message) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info(gameMode.getName() + " Color Debug: (" + message + ")");
		}
		
		if(message.startsWith(getPrefixWithBoldDivider(ActiveGame.current()) + "§a§lVote received. §3Your map now has") && Setting.AUTOVOTE.getValue()){
			Giant.hasVoted = true;
		}
		
		//§8▍ §aSky§b§b§lGiants§a§l§a§l:Mini§8§l ▏ §6§l§e§l§e§l1. §f§6Blossom §a[§f0§a Votes]
		else if(message.startsWith(getPrefixWithBoldDivider(ActiveGame.current()) + "§6§l§e§l§e§l6. ") && !Giant.hasVoted && Setting.AUTOVOTE.getValue()) {
				Giant.votesToParse.add(message);
			new Thread(new Runnable(){
				@Override
				public void run(){
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(Giant.votesToParse);
					List<GiantMap> parsedMaps = new ArrayList<GiantMap>();
					
					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();
					
					for(String s1 : AutovoteUtils.getMapsForMode("giant")){
						GiantMap map1 = GiantMap.valueOf(s1);	
						if(map1 == null) continue;
						parsedMaps.add(map1);
						The5zigAPI.getLogger().info("Parsed " + map1);
					}	
					
					for(String s : votesCopy){
						
						String[] data = s.split("\\.");						
						String index = ChatColor.stripColor(data[0]).replaceAll(getPrefixWithBoldDivider(ActiveGame.current()) + "§6§l§e§l§e§l", "").replaceAll(ChatColor.stripColor(getPrefixWithBoldDivider(ActiveGame.current())), "").trim();
						String[] toConsider = ChatColor.stripColor(data[1]).split("\\[");
						String consider = ChatColor.stripColor(toConsider[0]).trim();
						GiantMap map = GiantMap.get(consider, ActiveGame.current().equals("GNTM"));
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
						The5zigAPI.getLogger().info("Index Counter: " + index);
						if(index.equals("6")){
							if(votesindex.size() != 0){
								for(String n : votesindex){
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);
								
								Giant.votesToParse.clear();
								Giant.hasVoted = true;
																										//we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer(Log.info + "Automatically voted for map §6#" + finalindex);
								return;
							}
							else{
								The5zigAPI.getLogger().info("Done, couldn't find matches");
	
								Giant.votesToParse.clear();
								Giant.hasVoted = true;
								//he hasn't but we don't want to check again and again
							return;
							}
						}						
					}	
				}
			}).start();
			
			
		}
		else if(message.startsWith(getPrefixWithBoldDivider(ActiveGame.current()) + "§6§l§e§l§e§l") && !Giant.hasVoted && Setting.AUTOVOTE.getValue()){
			Giant.votesToParse.add(message);		
			The5zigAPI.getLogger().info("Added map");
		}
		else if(message.startsWith(getPrefix(ActiveGame.current()) + "§3You are now playing on the ")){
			String team = message.replaceAll(getPrefix(ActiveGame.current()) + "§3You are now playing on the ", "").replaceAll("Team!", "");
			Giant.team = team;
			gameMode.setState(GameState.GAME);
		}
		else if(message.startsWith(getPrefix(ActiveGame.current()) + "§3Voting has ended! §bThe map §f")){
			String data[] = message.replaceAll(getPrefix(ActiveGame.current()) + "§3Voting has ended! §bThe map §f", "").split("§b");
			String mapString = data[0].trim();
			The5zigAPI.getLogger().info(mapString.trim() + " / " + ActiveGame.is("GNTM"));
			GiantMap map = GiantMap.get(ChatColor.stripColor(mapString.trim()), ActiveGame.is("GNTM") ? true : false);
			Giant.activeMap = map;
			The5zigAPI.getLogger().info(map.getDisplay());
			
		}
		else if(message.startsWith(getPrefix(ActiveGame.current()) + "§a✚ §3You gained") && message.contains("for killing")){
			if(message.contains("as a team")){
				Giant.giantKills++; // Giant kill
			} else { 
			 Giant.gameKills++;
			 Giant.gameKdr = ((double)(Giant.totalKills + Giant.gameKills) / (double)(Giant.gameDeaths + Giant.totalDeaths == 0 ? 1 : Giant.gameDeaths + Giant.totalDeaths));
			}
		}
		
		//Advanced Records
		
		else if(message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")){
			Giant.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		}
		else if(message.startsWith("§3 ")){
				if(message.contains("User") || message.contains("Skill Rating")){
					return true;
				}

				Giant.messagesToSend.add(message);
				The5zigAPI.getLogger().info("found entry");
			
			return true;	
		}
		else if(message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")){
			Giant.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");
			
		return true;	
		}
		else if((message.equals("                      §6§m                  §6§m                  ")&& !message.startsWith("§o "))){
			The5zigAPI.getLogger().info("found footer");
			Giant.footerToSend.add(message);	
			The5zigAPI.getLogger().info("executed /records");
			if(Giant.footerToSend.contains("                      §6§m                  §6§m                  ")){
				//Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable(){
					@Override
					public void run(){
						Giant.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try{
						GiantRank rank = null;
						
						Integer wr = Setting.Giant_SHOW_WINRATE.getValue() ? (int) (Math.floor(((double)HiveAPI.GiantgetWins(Giant.lastRecords, lobby) / (double)HiveAPI.GiantgetGamesPlayed(Giant.lastRecords, lobby)) * 1000d) / 10d) : null;
						Double kd = Setting.Giant_SHOW_KD.getValue() ? (double) Math.floor(((double)HiveAPI.GiantgetKills(Giant.lastRecords, lobby) / (double)HiveAPI.GiantgetDeaths(Giant.lastRecords, lobby) * 100d)) / 100d : null;
						Double ppg = Setting.Giant_SHOW_PPG.getValue() ? Math.round(((double)HiveAPI.GiantgetPoints(Giant.lastRecords, lobby) / (double)HiveAPI.GiantgetGamesPlayed(Giant.lastRecords, lobby)) * 10d) / 10d : null;
						
						String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? HiveAPI.getNetworkRank(Giant.lastRecords) : "";
						ChatColor rankColor = null;
						if(Setting.SHOW_NETWORK_RANK_COLOR.getValue()){
							if(rankTitle.isEmpty()){
								rankColor = HiveAPI.getRankColorFromIgn(Giant.lastRecords);
							}
							else{
								rankColor = HiveAPI.getRankColor(rankTitle);
							}
						}
						long points = 0;
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? HiveAPI.lastGame(Giant.lastRecords, lobby) : null;
						String rankTitleGiant = Setting.SHOW_RECORDS_RANK.getValue() ? HiveAPI.GiantgetRank(Giant.lastRecords, lobby) : null;
						The5zigAPI.getLogger().info(rankTitleGiant);
						// int monthlyRank = (Setting.SHOW_RECORDS_MONTHLYRANK.getValue() && HiveAPI.getLeaderboardsPlacePoints(349, "DR") < HiveAPI.DRgetPoints(Giant.lastRecords)) ? HiveAPI.getMonthlyLeaderboardsRank(Giant.lastRecords, "DR") : 0;
						if(rankTitleGiant != null) rank = GiantRank.getFromDisplay(rankTitleGiant);
						List<String> messages = new ArrayList<String>();
						messages.addAll(Giant.messagesToSend);
							Iterator<String> it = messages.iterator();
							for(String s : messages){
								
														
								 	if(s.trim().endsWith("'s Stats §6§m")){
								 	The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = HiveAPI.getName(Giant.lastRecords);
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
									else if(s.startsWith("§3 Total Points: §b")){
										StringBuilder sb = new StringBuilder();
										sb.append("§3 Points: §b");
										points = Long.parseLong(s.replaceAll("§3 Total Points: §b", ""));
										sb.append(points);
										The5zigAPI.getLogger().info(rank);
										if(rank != null) sb.append(" (" + rank.getTotalDisplay());
										// if(Setting.Giant_SHOW_POINTS_TO_NEXT_RANK.getValue()) sb.append(" / " + rank.getPointsToNextRank((int)points));
										if(rank != null) sb.append("§b)");
										The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
										continue;
									
									}

								The5zigAPI.getAPI().messagePlayer("§o " + s);
								
							}
						
						if(ppg != null) The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + ppg);
						if(kd != null) The5zigAPI.getAPI().messagePlayer("§o " + "§3 K/D: §b" + kd);
						if(wr != null) The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + wr + "%");
						/*
						if(monthlyRank != 0){					
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
						} */
						if(lastGame != null){
								Calendar lastSeen = Calendar.getInstance();;
								lastSeen.setTimeInMillis(HiveAPI.lastGame(Giant.lastRecords, lobby).getTime());
							
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
						} 
						
							
							for(String s : Giant.footerToSend){								
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
						
						
						
						Giant.messagesToSend.clear();
						Giant.footerToSend.clear();
						Giant.isRecordsRunning = false;
						
						}catch(Exception e){
							e.printStackTrace();
							if(e.getCause() instanceof FileNotFoundException){
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								Giant.messagesToSend.clear();
								Giant.footerToSend.clear();
								Giant.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");
							
							for(String s : Giant.messagesToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for(String s : Giant.footerToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer("§o " + "                      §6§m                  §6§m                  ");
							Giant.messagesToSend.clear();
							Giant.footerToSend.clear();
							Giant.isRecordsRunning = false;
							return;
						}
					}
				}).start();
				return true;
				
				
				}
			
			}
		 
		 
		 
		
		return false;
	}


	@Override
	public void onServerConnect(Giant gameMode) {
		if(instance != null){
			Giant.reset(instance);
		}else{
		Giant.reset(gameMode);
		}
		
	}
	
	
	
	@Override
	public void onTitle(Giant gameMode, String title, String subTitle) {
		if(subTitle != null){
			
			if(ChatColor.stripColor(subTitle).equalsIgnoreCase("Respawning in 3 seconds")){
				Giant.gameDeaths++;
				Giant.gameKdr = ((double)((double)Giant.totalKills + (double)Giant.gameKills) / (double)((double)Giant.gameDeaths + (double)Giant.totalDeaths == 0 ? 1 :  (double)Giant.gameDeaths +  (double)Giant.totalDeaths));
			}
			
		}
	}


	private String getPrefix(String mode){
		// §8▍ §aSky§b§b§lGiants§8§l ▏ §6§l§e§l§e§l6. §f§6Lost §7[§f0§7 Votes]
		if(mode.equalsIgnoreCase("gnt")){
			return "§8▍ §aSky§b§lGiants§8 ▏ ";
		}
		else if(mode.equalsIgnoreCase("gntm")){
			return "§8▍ §aSky§b§lGiants§a§l:Mini§8 ▏ ";
		}
		
		return "";
	}
	
	private String getPrefixWithBoldDivider(String mode){
		// §8▍ §aSky§b§b§lGiants§8§l ▏ §6§l§e§l§e§l6. §f§6Lost §7[§f0§7 Votes]
		if(mode.equalsIgnoreCase("gnt")){
			return "§8▍ §aSky§b§b§lGiants§8§l ▏ ";
		}
		else if(mode.equalsIgnoreCase("gntm")){
			return "§8▍ §aSky§b§b§lGiants§a§l§a§l:Mini§8§l ▏ ";
		}
		
		return "";
	}
	
	
	
	
	

	
}
