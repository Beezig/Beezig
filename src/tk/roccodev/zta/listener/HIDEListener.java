package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.HIDE;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.hide.HIDERank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHIDE;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

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
					try {
						HIDE.initDailyPointsWriter();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());
					
					ApiHIDE api = new ApiHIDE(The5zigAPI.getAPI().getGameProfile().getName());
					
					if(sb != null && sb.getTitle().contains("Your HIDE Stats")){
						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						APIValues.HIDEpoints = (long) points;
					}
					else {
						APIValues.HIDEpoints = api.getPoints();
					}

					HIDE.rankObject = HIDERank.getFromDisplay(api.getTitle());
					HIDE.rank = HIDE.rankObject.getTotalDisplay();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}



	@Override
	public boolean onServerChat(HIDE gameMode, String message) {

		if(message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §3Voting has ended! §bThe map §f")){
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §bHide§aAnd§eSeek§8 ▏ §3Voting has ended! §bThe map ")[1];
			String map = "";
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
			HIDE.activeMap = map;
			DiscordUtils.updatePresence("Playing Hide & Seek", "Hiding on " + HIDE.activeMap, "game_hide");
		}

		//Autovoting

		else if(message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §a§lVote received. §3Your map now has ") && Setting.AUTOVOTE.getValue()){
			HIDE.hasVoted = true;
		}
		else if(message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6§e§e§l6. §f§cRandom map") && !HIDE.hasVoted && Setting.AUTOVOTE.getValue()){

			new Thread(new Runnable(){
				@Override
				public void run(){
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(HIDE.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("hide"));

					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					
					for(String s : votesCopy){

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §bHide§aAnd§eSeek§8 ▏ §6§e§e§l", "").replaceAll("▍ HideAndSeek ▏", "").trim();
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
						if(index.equals("5")){
							if(votesindex.size() != 0){
								for(String n : votesindex){
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);

								HIDE.votesToParse.clear();
								HIDE.hasVoted = true;
								//we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer("§8▍ §bHide§aAnd§eSeek§8 ▏ " + "§eAutomatically voted for map §6#" + finalindex);
								return;
							}
							else if(Setting.AUTOVOTE_RANDOM.getValue()){
								The5zigAPI.getLogger().info("Done, couldn't find matches - Voting Random");
								The5zigAPI.getAPI().sendPlayerMessage("/v 6");
								The5zigAPI.getAPI().messagePlayer("§8▍ §bHide§aAnd§eSeek§8 ▏ " + "§eAutomatically voted for §cRandom map");
								HIDE.votesToParse.clear();
								HIDE.hasVoted = true;
								//he hasn't but we don't want to check again and again
								return;
							}
						}
					}
				}
			}).start();
		}
		else if(message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6§e§e§l") && !HIDE.hasVoted && Setting.AUTOVOTE.getValue()){
			HIDE.votesToParse.add(message);
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
		else if(message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6You have gained §e200 points§6")) {
			APIValues.HIDEpoints += 200;
			HIDE.dailyPoints += 200;
		}
		else if(message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6You have gained §e50 points§6")) {
			APIValues.HIDEpoints += 50;
			HIDE.dailyPoints += 50;
		}
		else if(message.equals("                          §6§lYou are a §c§lSEEKER!")) {
			HIDE.seeking = true;
			DiscordUtils.updatePresence("Playing Hide & Seek", "Seeking on " + HIDE.activeMap, "game_hide");
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
						HIDERank rank = null;




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
						String rankTitleHIDE = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
						if(rankTitleHIDE != null) rank = HIDERank.getFromDisplay(rankTitleHIDE);

						int kills = 0;
						long points = 0;
						int deaths = 0;
						int gamesPlayed = 0;
						int victories = 0;
						int killsSeeker = 0;
						int killsHider = 0;
						long timeAlive = 0;

						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
						Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements() : null;
						Integer playedBlocks = Setting.HIDE_SHOW_AMOUNT_UNLOCKED.getValue() ? api.getBlockExperience().size() : null;



						//int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() && HiveAPI.getLeaderboardsPlacePoints(349, "HIDE") < HiveAPI.DRgetPoints(HIDE.lastRecords)) ? HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

						List<String> messages = new ArrayList<String>();
						messages.addAll(HIDE.messagesToSend);
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
										sb.append(points);
										if(rank != null) sb.append(" (").append(rank.getTotalDisplay());
										if(Setting.HIDE_SHOW_POINTS_TO_NEXT_RANK.getValue()) sb.append(" / ").append(rank.getPointsToNextRank((int) points));
										if(rank != null) sb.append("§b)");

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
										timeAlive = Long.parseLong(ChatColor.stripColor(s.replaceAll("§3 Time Alive: §b", "").trim()));
										s = s.replaceAll(Long.toString(timeAlive), APIUtils.getTimePassed(timeAlive));
									}



								The5zigAPI.getAPI().messagePlayer("§o " + s);

							}


						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/57");
						}
						if(Setting.HIDE_SHOW_WINRATE.getValue()){
							double wr = (double) victories / (double) gamesPlayed;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr*100) + "%");
						}
						if(Setting.HIDE_SHOW_SEEKER_KPG.getValue()){
							double skpg = (double) killsSeeker / (double) gamesPlayed;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Seeker: Kills per Game: §b" + df.format(skpg));
						}
						if(Setting.HIDE_SHOW_HIDER_KPG.getValue()){
							double hkpg = (double) killsHider / (double) gamesPlayed;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Hider: Kills per Game: §b" + df.format(hkpg));
						}
						if(Setting.HIDE_SHOW_POINTSPG.getValue()){
							double ppg = (double) points / (double) gamesPlayed;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + df1f.format(ppg));
						}

					/*	if(Setting.HIDE_SHOW_WINRATE.getValue()){				
							double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr) + "%");
						}
						
					*	if(monthlyRank != 0){					
					 *		The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
					 *	}
					 */
						if(playedBlocks != null){
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Played Blocks: §b" + playedBlocks);
						}
						if(lastGame != null){
							Calendar lastSeen = Calendar.getInstance();
							lastSeen.setTimeInMillis(lastGame.getTime());
							The5zigAPI.getAPI().messagePlayer("§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
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
						}
					}
				}).start();
				return true;


			}

		}


		return false;

	}
	
	

	@Override
	public void onTick(HIDE gameMode) {
		if(The5zigAPI.getAPI().getSideScoreboard() == null) return;
		int i = HIDE.seeking ? 4 : 5;
		HashMap<String, Integer> lines = The5zigAPI.getAPI().getSideScoreboard().getLines();
		for(Map.Entry<String, Integer> e : lines.entrySet()) {
			if(e.getValue() == i && e.getKey().contains("§7 Points§6")) {
				int pts = Integer.parseInt(e.getKey().replace("§7 Points§6", "").replace("§f", ""));
				if(pts != HIDE.lastPts) {
					HIDE.dailyPoints += (pts - HIDE.lastPts);
					APIValues.HIDEpoints += (pts - HIDE.lastPts);
					HIDE.lastPts = pts;
				}
			}
		}
	}

	@Override
	public void onServerConnect(HIDE gameMode) {
		HIDE.reset(gameMode);
	}

}
