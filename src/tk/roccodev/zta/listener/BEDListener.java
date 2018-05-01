package tk.roccodev.zta.listener;

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
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDMap;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiBED;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		IHive.genericJoin();
		
		new Thread(new Runnable(){
			
			@Override
			public void run(){
				try {
										
					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());
					BED.updateMode();
					if(sb != null && sb.getTitle().contains("BED")){
						BED.apiKills = sb.getLines().get(ChatColor.AQUA + "Kills");
						BED.apiDeaths = sb.getLines().get(ChatColor.AQUA + "Deaths");
					}else{
						String ign2 = The5zigAPI.getAPI().getGameProfile().getName();
						ApiBED api = new ApiBED(ign2);
						BED.apiDeaths = Math.toIntExact(api.getDeaths());
						BED.apiKills = Math.toIntExact(api.getKills());
					}

					String ign1 = The5zigAPI.getAPI().getGameProfile().getName();
					APIValues.BEDpoints = new ApiBED(ign1).getPoints();
					BED.updateRank();
					BED.updateKdr();
					The5zigAPI.getLogger().info(BED.apiDeaths + " / " + BED.apiKills + " / " + BED.apiKdr);
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
		if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map")){
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map")[1];
			BED.updateMode();
			String map = "";    
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		   
		    BED.activeMap = map;
		    

			DiscordUtils.updatePresence("Housekeeping in BedWars: " + BED.mode, "Playing on " + BED.activeMap, "game_bedwars");
		}

		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained") && message.contains("§a points for killing")){

			int i = Integer.parseInt(ChatColor.stripColor(message.split(" ")[5]));

			BED.kills++;
			BED.pointsCounter += i;
			APIValues.BEDpoints += i;
			BED.updateKdr();
			
		}

		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §aYou have gained") && message.contains("points for destroying")){

			int i = Integer.parseInt(ChatColor.stripColor(message.split(" ")[6]));
			
			BED.pointsCounter += i;
			BED.bedsDestroyed++;
			APIValues.BEDpoints += i;
			
		}
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §e✯ §6Notable Win! §eGold Medal Awarded!")){
			
			BED.pointsCounter += 100;
			APIValues.BEDpoints += 100;
			HiveAPI.medals++;
			HiveAPI.tokens += 100;
			BED.hasWon = true;
			BED.winstreak++;
			
		}
		else if(message.contains("§c has been ELIMINATED!")){
			BED.updateTeamsLeft();
		}
		else if(message.contains("§c bed has been DESTROYED!")){
			BED.updateTeamsLeft();
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
						
						ApiBED api = new ApiBED(BED.lastRecords);
							
						int kills = 0;
						int deaths = 0;
						int gamesPlayed = 0;
						int victories = 0;
						int bedsDestroyed = 0;
						int eliminations = 0;
						
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
						
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
						Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements() : null;

						//int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() && HiveAPI.getLeaderboardsPlacePoints(349, "BED") < HiveAPI.DRgetPoints(BED.lastRecords)) ? HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;
						List<String> messages = new ArrayList<String>();
						messages.addAll(BED.messagesToSend);
							for(String s : messages){
								
								
								 	if(s.trim().endsWith("'s Stats §6§m")){
								 	The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = api.getParentMode().getCorrectName();
									if(correctUser.contains("nicked player")) correctUser = "Nicked/Not found";
									sb.append("          §6§m                  §f ");
									The5zigAPI.getLogger().info("Added base...");
									if(rankColor != null) {
										sb.append(rankColor).append(correctUser);
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
										BED.lastRecordsPoints = points;
										sb.append(points);
										if(Setting.SHOW_RECORDS_RANK.getValue()){
											BEDRank rank = BEDRank.isNo1(BED.lastRecords) ? BEDRank.ZZZZZZ :BEDRank.getRank((int)points);
											if(rank != null){
												int level = rank.getLevel((int)points);
												String BEDrankColor = rank.getName().replaceAll(ChatColor.stripColor(rank.getName()), "");
												String rankString = BED.NUMBERS[level] + " " +rank.getName();
												sb.append(" (").append(BEDrankColor).append(rankString.trim());
												if(Setting.BED_SHOW_POINTS_TO_NEXT_RANK.getValue()){
													sb.append(" / ").append(rank.getPointsToNextRank((int) points));
												}
												sb.append("§b)");
											}
											
											
											
										}
										
										//if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

										The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
										continue;
									
									}
									else if(s.startsWith("§3 Kills: §b")){
										kills = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Kills: §b", "").trim()));
									}
									else if(s.startsWith("§3 Deaths: §b")){
										deaths = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Deaths: §b", "").trim()));
									}
									else if(s.startsWith("§3 Games Played: §b")){
										gamesPlayed = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Games Played: §b", "").trim()));
									}
									else if(s.startsWith("§3 Victories: §b")){
										victories = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Victories: §b", "").trim()));
									}
									else if(s.startsWith("§3 Beds Destroyed: §b")){
										bedsDestroyed = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Beds Destroyed: §b", "").trim()));
									}
									else if(s.startsWith("§3 Team Eliminated: §b")){
										eliminations = Integer.parseInt(ChatColor.stripColor(s.replaceAll("§3 Team Eliminated: §b", "").trim()));
									}
								 	
								The5zigAPI.getAPI().messagePlayer("§o " + s);
								
							}
							
						
							
						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "");
																											//^ API Achievements vs ingame - currently bad
						}
						// "§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained 10§a points for killing"
						
						if(Setting.BED_SHOW_ELIMINATIONS_PER_GAME.getValue()){
							double epg = eliminations / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Eliminations per Game: §b" + df1f.format(epg));
						}	
						if(Setting.BED_SHOW_BEDS_PER_GAME.getValue()){
							double bpg = bedsDestroyed / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Beds per Game: §b" + df1f.format(bpg));
						}
						if(Setting.BED_SHOW_DEATHS_PER_GAME.getValue()){
							double dpg = deaths / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Deaths per Game: §b" + df1f.format(dpg));
						}
						if(Setting.BED_SHOW_KILLS_PER_GAME.getValue()){
							double kpg = kills / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Kills per Game: §b" + df1f.format(kpg));
						}
						if(Setting.BED_SHOW_POINTS_PER_GAME.getValue()){
							double ppg = BED.lastRecordsPoints / (double)(gamesPlayed == 0 ? 1 : gamesPlayed);
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + df1f.format(ppg));
						}
						if(Setting.BED_SHOW_KD.getValue()){
							double kdr = kills / (double)(deaths == 0 ? 1 : deaths);
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 K/D: §b" + df.format(kdr));
						}
						if(Setting.BED_SHOW_WINRATE.getValue()){
							double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr) + "%");
						}
					/*	if(monthlyRank != 0){					
					 *		The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
					 *	}
					 */	
						
						if(lastGame != null){
							Calendar lastSeen = Calendar.getInstance();
							lastSeen.setTimeInMillis(lastGame.getTime());
							
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
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
						}
					}
				}).start();
				return true;
				
				
			}
			
		}
	
		
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §a§lVote received.") && Setting.AUTOVOTE.getValue()){
			BED.updateMode();
			BED.hasVoted = true;
		}
		
		else if(message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l4. ") && !BED.hasVoted && Setting.AUTOVOTE.getValue()){
			//Adding the 6th option, the normal method doesn't work
			BED.votesToParse.add(message);
			new Thread(new Runnable(){
				@Override
				public void run()	{
					try { TimeUnit.MILLISECONDS.sleep(200); } catch (Exception e) {}
				
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(BED.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("bed"));
					
					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();
					
					The5zigAPI.getLogger().info(votesCopy.size());
					for(String s : votesCopy){
						
						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l", "").replaceAll("▍ BedWars ▏", "").trim();
						String[] toConsider = ChatColor.stripColor(data[1]).split("\\[");
						String consider = ChatColor.stripColor(toConsider[0]).trim().replaceAll(" ", "_").toUpperCase();
						
						String votes = toConsider[1].split(" ")[0].trim();
						
						
						The5zigAPI.getLogger().info("trying to match " + consider);
						if(parsedMaps.contains(consider)){
							votesindex.add(votes + "-" + index);
							The5zigAPI.getLogger().info("Added " + consider + " Index #" + index + " with " + votes + " votes");	
						}else{
							The5zigAPI.getLogger().info(consider + " is not a favourite");
						}
						
						if(index.equals("6")){
							if(votesindex.size() != 0){
								for(String n : votesindex){
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);
								
								BED.votesToParse.clear();
								BED.hasVoted = true;
																										//we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer(Log.info + "Automatically voted for map §6#" + finalindex);
								return;
							}
							else{
								The5zigAPI.getLogger().info("Done, couldn't find matches");
								BED.votesToParse.clear();
								BED.hasVoted = true;
								//he hasn't but we don't want to check again and again
							return;
							}
						}
				
					}
			
				}
			}).start();
		}
		else if(message.equals("                 §e§lGold Ingot Summoner Activated!")) {
			BED.goldGen = 1;
		}
		else if(message.equals("                   §b§lDiamond Summoner Activated!")) {
			BED.diamondGen = 1;
		}
		else if(message.equals("                 §e§lGold Ingot Summoner Upgraded!")) {
			BED.goldGen++;
		}
		else if(message.equals("                 §f§lIron Ingot Summoner Upgraded!")) {
			BED.ironGen++;
		}
		else if(message.equals("                   §b§lDiamond Summoner Upgraded!")) {
			BED.diamondGen++;
		}
		else if(message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l") && !BED.hasVoted && Setting.AUTOVOTE.getValue()){		
			BED.votesToParse.add(message);
		}
		else if(message.startsWith("               §aYou levelled up to")){
			//Update the rank module when you uprank
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						String ign = The5zigAPI.getAPI().getGameProfile().getName();
						APIValues.BEDpoints = new ApiBED(ign).getPoints();
						Thread.sleep(200);
						BED.updateRank();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}}).start();
				
		}
		else if(message.contains("§lYou are on ")){
			//"                        §6§lYou are on Gold Team!"
            //§9§lYou are on Blue Team!

			String team = null;
			Pattern pattern = Pattern.compile(Pattern.quote("the ") + "(.*?)" + Pattern.quote(" Team!"));
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
		else if(message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §7You gained no points for killing")){
			BED.kills++;
			BED.updateKdr();
		}
		else if(message.trim().equals("§d§lNew Rank!")){
			new Thread(new Runnable(){
				@Override
				public void run(){
					
					try {
						String ign = The5zigAPI.getAPI().getGameProfile().getName();
						APIValues.BEDpoints = new ApiBED(ign).getPoints();
						Thread.sleep(200);
						BED.updateRank();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
		return false;
			
	}

	@Override
	public void onTitle(BED gameMode, String title, String subTitle) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("BedWars TitleColor Debug: (" + 
		
					title != null ? title : "ERR_TITLE_NULL"
						
						+ " *§* " +
						
						
					subTitle != null ? subTitle : "ERR_SUBTITLE_NULL"
					
						+ ")"
					);
		}
		if(subTitle != null && ChatColor.stripColor(subTitle).trim().equals("Respawning in 2 seconds")){
			BED.deaths++;
			BED.updateKdr();
		}
		else if(subTitle != null && subTitle.equals("§r§7Protect your bed, destroy others!§r")){
			gameMode.setState(GameState.GAME);
			BED.ironGen = 1;
			//As Hive sends this subtitle like 13 times, don't do anything here please :) mhm
		}
		else if(title != null && title.equals("§r§c§lFIGHT!§r")){	
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						TimeUnit.SECONDS.sleep(4);
						BED.updateTeamsLeft();
					} catch (Exception e) {}									
				}
				}).start();
		}
	}

	@Override
	public void onServerConnect(BED gameMode) {
		if(BED.hasWon == null || !BED.hasWon) BED.hasWon = false; 
		BED.reset(gameMode);
	}
	
	
	

	

}
