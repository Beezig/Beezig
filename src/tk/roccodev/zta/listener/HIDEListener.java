package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import tk.roccodev.zta.games.HIDE;
import tk.roccodev.zta.hiveapi.HIDEMap;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHIDE;
import tk.roccodev.zta.settings.Setting;

public class HIDEListener extends AbstractGameListener<HIDE> {

	@Override
	public Class<HIDE> getGameMode() {
		return HIDE.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("HIDE");
	}
	
	@Override
	public void onGameModeJoin(HIDE gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("HIDE");
		IHive.genericJoin();
		
		new Thread(new Runnable(){
			
			@Override
			public void run(){
				try {
										
					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());
					if(sb != null && sb.getTitle().contains("HIDE")){
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();		
	}
	
	

	@Override
	public boolean onServerChat(HIDE gameMode, String message) {
		
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("HIDE Color Debug: (" + message + ")");
		}

		if(message.startsWith("§8▍ §3§lHIDE§b§lWars§8 ▏ §3Voting has ended! §bThe map")){
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §3§lHIDE§b§lWars§8 ▏ §3Voting has ended! §bThe map")[1];
			String map = "";    
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    HIDEMap map1 = HIDEMap.getFromDisplay(map);
		    HIDE.activeMap = map1;
		}

		
		//Advanced Records
		
		else if(message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")){
			HIDE.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		}
		else if(message.startsWith("§3 ")){
			
				HIDE.messagesToSend.add(message);
				The5zigAPI.getLogger().info("found entry");
			
			return true;	
		}
		else if(message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")){
			HIDE.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");
			
			return true;	
		}
		else if((message.equals("                      §6§m                  §6§m                  ")&& !message.startsWith("§o "))){
			The5zigAPI.getLogger().info("found footer");
			HIDE.footerToSend.add(message);	
			The5zigAPI.getLogger().info("executed /records");
			if(HIDE.footerToSend.contains("                      §6§m                  §6§m                  ")){
				//Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable(){
					@Override
					public void run(){
						HIDE.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try{
						
						ApiHIDE api = new ApiHIDE(HIDE.lastRecords);
							
						int kills = 0;

						
						NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
						DecimalFormat df = (DecimalFormat) nf;
						df.setMaximumFractionDigits(2);
						df.setMinimumFractionDigits(2);
						
						DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
						df1f.setMaximumFractionDigits(1);
						df1f.setMinimumFractionDigits(1);
						
						
						String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? api.getParentMode().getNetworkTitle() : "";
						ChatColor rankColor = null;
						if(Setting.SHOW_NETWORK_RANK_COLOR.getValue()){
							
							rankColor = api.getParentMode().getNetworkRankColor();
							
						}
						
						long points = 0;
						int deaths = 0;
						int gamesPlayed = 0;
						int victories = 0;
						int killsSeeker = 0;
						int killsHider = 0;
						long timeAlive = 0;
						
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
						Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements() : null;
						

						//int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() && HiveAPI.getLeaderboardsPlacePoints(349, "HIDE") < HiveAPI.DRgetPoints(HIDE.lastRecords)) ? HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;
						
						List<String> messages = new ArrayList<String>();
						messages.addAll(HIDE.messagesToSend);
							Iterator<String> it = messages.iterator();
							for(String s : messages){
								
								
								
								 	if(s.trim().endsWith("'s Stats §6§m")){
								 	The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = api.getParentMode().getCorrectName();
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
										if(Setting.SHOW_RECORDS_RANK.getValue()){
														
										}
										
										//if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

										The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
										continue;
									}
									else if(s.startsWith("§3 Victories: §b")){
										victories = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Victories: §b", "").trim()));
									}
									else if(s.startsWith("§3 Games Played: §b")){
										gamesPlayed = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Games Played: §b", "").trim()));
									}
									else if(s.startsWith("§3 Total Kills: §b")){
										kills = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Total Kills: §b", "").trim()));
									}
									else if(s.startsWith("§3 Total Deaths: §b")){
										deaths = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Total Deaths: §b", "").trim()));
									}
									else if(s.startsWith("§3 Kills as Seeker: §b")){
										killsSeeker = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Kills as Seeker: §b", "").trim()));
									}
									else if(s.startsWith("§3 Kills as Hider: §b")){
										killsHider = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Kills as Hider: §b", "").trim()));
									}
									else if(s.startsWith("§3 Time Alive: §b")){
										timeAlive = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Time Alive: §b", "").trim()));
									}
								
									
								 	
								The5zigAPI.getAPI().messagePlayer("§o " + s);
								
							}
							
												
						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "");
																											
						}

						
					/*	if(Setting.HIDE_SHOW_WINRATE.getValue()){				
							double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr) + "%");
						}
						
					*	if(monthlyRank != 0){					
					 *		The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
					 *	}
					 */		
						if(lastGame != null){
							Calendar lastSeen = Calendar.getInstance();
							lastSeen.setTimeInMillis(lastGame.getTime());
							
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Last Game: §b" + HiveAPI.getTimeAgo(lastSeen.getTimeInMillis()));
						}
						
							
							for(String s : HIDE.footerToSend){								
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
						
						
						
							HIDE.messagesToSend.clear();
							HIDE.footerToSend.clear();
							HIDE.isRecordsRunning = false;
						
						}catch(Exception e){
							e.printStackTrace();
							if(e.getCause() instanceof FileNotFoundException){
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								HIDE.messagesToSend.clear();
								HIDE.footerToSend.clear();
								HIDE.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");
							
							for(String s : HIDE.messagesToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for(String s : HIDE.footerToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer("§o " + "                      §6§m                  §6§m                  ");
							HIDE.messagesToSend.clear();
							HIDE.footerToSend.clear();
							HIDE.isRecordsRunning = false;
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
	public void onTitle(HIDE gameMode, String title, String subTitle) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("HIDE TitleColor Debug: (" + 
		
					title != null ? title : "ERR_TITLE_NULL"
						
						+ " *§* " +
						
						
					subTitle != null ? subTitle : "ERR_SUBTITLE_NULL"
					
						+ ")"
					);
		}	
	}

	@Override
	public void onServerConnect(HIDE gameMode) {
		HIDE.reset(gameMode);
	}
		
}
