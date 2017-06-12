package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.DRRank;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.TIMVRank;
import tk.roccodev.zta.settings.Setting;

public class DRListener extends AbstractGameListener<DR>{

	@Override
	public Class<DR> getGameMode() {
		// TODO Auto-generated method stub
		return DR.class;
	}

	@Override
	public boolean matchLobby(String lobby) {		
		return lobby.equals("DR");		
	}

	@Override
	public void onGameModeJoin(DR gameMode){
		gameMode.setState(GameState.STARTING);
		ZTAMain.isDR = true;
		Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
		if(sb != null) The5zigAPI.getLogger().info(sb.getTitle());
		if(sb != null && sb.getTitle().contains("Your DR Stats")){
			int points = sb.getLines().get(ChatColor.AQUA + "Points");	
			HiveAPI.DRpoints = (long) points;		
		}else{
		try {
			//HiveAPI.updateKarma();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
	}
	
	@Override
	public boolean onServerChat(DR gameMode, String message) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("ColorDebug: " + "(" + message + ")");
		}
		if(message.startsWith("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map") && gameMode != null){
			String afterMsg = message.split("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map")[1];
			The5zigAPI.getLogger().info(afterMsg);
			String map = "";
		    
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    DRMap map1 = DRMap.getFromDisplay(map);	    
		    DR.activeMap = map1;			
		}
		
		else if(message.contains("§lYou are a ") && gameMode != null){
			String afterMsg = message.split(ChatColor.stripColor("You are a "))[1];	
			switch(afterMsg){
				case "DEATH!": DR.role = "Death";
					break;
				case "RUNNER!": DR.role = "Runner";
				new Thread(new Runnable(){
				
					@Override
					public void run(){
						if(DR.activeMap != null){
							The5zigAPI.getLogger().info("Loading PB...");
							DR.currentMapPB = HiveAPI.DRgetPB(The5zigAPI.getAPI().getGameProfile().getName(), DR.activeMap);
							if(DR.currentMapPB == null) DR.currentMapPB = "No Personal Best";
							The5zigAPI.getLogger().info("Loading WR...");
							DR.currentMapWR = HiveAPI.DRgetWR(DR.activeMap);
							DR.currentMapWRHolder = HiveAPI.DRgetWRHolder(DR.activeMap);
							if(DR.currentMapWR == null) DR.currentMapWR = "No Record";
							if(DR.currentMapWRHolder == null) DR.currentMapWRHolder = "Unknown";
						}
					}
				}).start();
				
						
					break;
			}
		}
		else if(message.startsWith("§8▍ §eTokens§8 ▏ §7You earned §f") && ZTAMain.isDR && DR.role == "Runner") {
			// I don't care about double tokens weekends Rocco :^)
			if(!(DR.checkpoints == DR.activeMap.getCheckpoints())){
					DR.checkpoints++;
				}
			}
		else if(message.equals("§8▍ §cDeathRun§8 ▏ §cYou have been returned to your last checkpoint!") && ZTAMain.isDR && DR.role == "Runner") {
				DR.deaths++;	
			}
		else if(message.contains("§6 (") && message.contains("§6)") && message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && ZTAMain.isDR && DR.role == "Death") {
				DR.kills++;	
			}
		
		
		
		
		
		else if(message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")){
			//"          §6§m                  §f ItsNiklass's Stats §6§m                  "
			//Advanced Records
			DR.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		}
		else if(message.startsWith("§3 ")){
			
				DR.messagesToSend.add(message);
				The5zigAPI.getLogger().info("found entry");
			
			return true;	
		}
		else if(message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")){
			//TODO Coloring
			DR.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");
			
		return true;	
		}
		else if((message.equals("                      §6§m                  §6§m                  ")&& !message.startsWith("§o "))){
			The5zigAPI.getLogger().info("found footer");
			DR.footerToSend.add(message);	
			The5zigAPI.getLogger().info("executed /records");
			if(DR.footerToSend.contains("                      §6§m                  §6§m                  ")){
				//Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable(){
					@Override
					public void run(){
						DR.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try{
						DRRank rank = null;
						Double ppg = Setting.DR_SHOW_POINTSPERGAME.getValue() ? Math.round(((double)HiveAPI.DRgetPoints(DR.lastRecords) / (double)HiveAPI.DRgetGames(DR.lastRecords)) * 10d) / 10d : null;
						Integer rwr = Setting.DR_SHOW_RUNNERWINRATE.getValue() ? (int) (Math.floor(((double)HiveAPI.DRgetRunnerWins(DR.lastRecords) / (double)HiveAPI.DRgetRunnerGamesPlayed(DR.lastRecords)) * 1000d) / 10d) : null;
						Double dpg = Setting.DR_SHOW_DEATHSPERGAME.getValue() ? (double) (Math.floor(((double)HiveAPI.DRgetDeaths(DR.lastRecords) / (double)HiveAPI.DRgetRunnerGamesPlayed(DR.lastRecords)) * 10d) / 10d) : null;
						String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? HiveAPI.getNetworkRank(DR.lastRecords) : "";
						ChatColor rankColor = null;
						if(Setting.SHOW_NETWORK_RANK_COLOR.getValue()){
							if(rankTitle.isEmpty()){
								rankColor = HiveAPI.getRankColorFromIgn(DR.lastRecords);
								The5zigAPI.getLogger().info("Executed RankColorFromIgn > " + rankColor);
							}
							else{
								rankColor = HiveAPI.getRankColor(rankTitle);
							}
						}
						long points = 0;
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? HiveAPI.lastGame(DR.lastRecords, "DR") : null;
						Integer achievements = Setting.DR_SHOW_ACHIEVEMENTS.getValue() ? HiveAPI.DRgetAchievements(DR.lastRecords) : null;
						String rankTitleDR = Setting.DR_SHOW_RANK.getValue() ? HiveAPI.DRgetRank(DR.lastRecords) : null;
						The5zigAPI.getLogger().info(HiveAPI.getLeaderboardsPlacePoints(349, "DR"));
						int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() && HiveAPI.getLeaderboardsPlacePoints(349, "DR") < HiveAPI.DRgetPoints(DR.lastRecords)) ? HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;
						if(rankTitleDR != null) rank = DRRank.getFromDisplay(rankTitleDR);
						List<String> messages = new ArrayList<String>();
						messages.addAll(DR.messagesToSend);
							Iterator<String> it = messages.iterator();
							for(String s : messages){
								
								
								
								 	if(s.trim().endsWith("'s Stats §6§m")){
								 	//"          §6§m                  §f ItsNiklass's Stats §6§m                  "
								 	//"§6§m                  §f ItsNiklass's Stats §6§m"
								 	The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = HiveAPI.getName(DR.lastRecords);
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
										if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");
										The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
										continue;
									
									}

								The5zigAPI.getAPI().messagePlayer("§o " + s);
								
							}
						
							
						if(ppg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + ppg);
						}
						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/47");
						}
						if(rwr != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Runner-Winrate: §b" + rwr + "%");
						}
						if(dpg != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Deaths per Game: §b" + dpg);
						}
						if(monthlyRank != 0){					
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
						}
						if(lastGame != null){
							Date now = new Date();
							long diff = now.getTime() - lastGame.getTime();
							
							
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Last Game: §b" + lastGame + " (" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days ago)");
						}
						
							
							for(String s : DR.footerToSend){								
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
						
						
						
						DR.messagesToSend.clear();
						DR.footerToSend.clear();
						DR.isRecordsRunning = false;
						
						}catch(Exception e){
							e.printStackTrace();
							if(e.getCause() instanceof FileNotFoundException){
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								DR.messagesToSend.clear();
								DR.footerToSend.clear();
								DR.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");
							
							for(String s : DR.messagesToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for(String s : DR.footerToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer("§o " + "                      §6§m                  §6§m                  ");
							DR.messagesToSend.clear();
							DR.footerToSend.clear();
							DR.isRecordsRunning = false;
							return;
						}
					}
				}).start();
				return true;
				
				
				}
			
			}
	
			else if(message.contains("§lYou are a ")){
				gameMode.setState(GameState.GAME);
			}
			
			else if(message.startsWith("§8▍ §cDeathRun§8 ▏") && message.contains("§3 finished §b") && message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && !message.endsWith(" ")){
				//"§8▍ §cDeathRun§8 ▏ §b §aItsNiklass§3 finished §b1st§3. §7(01:10.574)"
				String time = (message.split("§7\\("))[1].replaceAll("\\)", "");
				String[] data = time.split(":");
				int minutes = Integer.parseInt(data[0]);
				//data[1	] is seconds.milliseconds
				double secondsMillis = Double.parseDouble(data[1]);
				double finalTime = 60 * minutes + secondsMillis; //e.g, You finished in 01:51.321 = 01*60 + 51.321 = 111.321
				
				new Thread(new Runnable(){
					@Override
					public void run(){
						
					double wr = HiveAPI.DRgetWR_raw(DR.activeMap);
					double diff = (Math.round((finalTime - wr) * 1000d)) / 1000d;
					
					String pb = DR.currentMapPB;
					String[] pbData = pb.split(":");
					int finalPb = Integer.parseInt(pbData[0]) * 60 + Integer.parseInt(pbData[1]); 		
					int pbDiff = ((int) Math.floor(finalTime)) - finalPb;


					if(diff == 0){
					//Lets make this more important lmao
					//All this has a reason, I may overdid it but I learned smth about tags
						The5zigAPI.getAPI().messagePlayer("    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer(Log.info + "   §c§lCongratulations! You §4§ltied §c§lthe §4§lWorld Record§c§l!");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer("    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer(message);
					}
					else if(diff < 0){
						The5zigAPI.getAPI().messagePlayer("    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer(Log.info + "   §c§lCongratulations! §4§lYou beat the World Record!!!");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer("    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer(message);
					}
					
					
					else if(pbDiff > 0){
						The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff + "§3 seconds away! Your Personal Best is §b" + pbDiff + " §3seconds away!");
					}
					else if(pbDiff == 0){
						The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff + "§3 seconds away! You tied your Personal Best!");
					}
					else{
						The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff + "§3 seconds away! You beat your Personal Best by §b" + -pbDiff + " §3seconds!");
					}
					}
					
				}).start();
				
			return true;
			}
		return false;
	}

	@Override
	public void onServerConnect(DR gameMode) {
		The5zigAPI.getLogger().info("Resetting! (DR)");
		DR.reset(gameMode);
	}

}
