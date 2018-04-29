package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.stuff.dr.DRMap;
import tk.roccodev.zta.hiveapi.stuff.dr.DRRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiDR;
import tk.roccodev.zta.settings.Setting;

public class DRListener extends AbstractGameListener<DR> {

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
	public void onGameModeJoin(DR gameMode) {
		gameMode.setState(GameState.STARTING);
		ActiveGame.set("DR");
		IHive.genericJoin();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
				DR.rank = DRRank.getFromDisplay(new ApiDR(The5zigAPI.getAPI().getGameProfile().getName()).getTitle())
						.getTotalDisplay();
				// Should've read the docs ¯\_(ツ)_/¯
				if (sb != null)
					The5zigAPI.getLogger().info(sb.getTitle());
				if (sb != null && sb.getTitle().contains("Your DR Stats")) {
					int points = sb.getLines().get(ChatColor.AQUA + "Points");
					APIValues.DRpoints = (long) points;

				}
			}
		}).start();

	}

	@Override
	public boolean onServerChat(DR gameMode, String message) {
		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("ColorDebug: " + "(" + message + ")");
		}
		if (message.startsWith("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map") && gameMode != null) {
			String afterMsg = message.split("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map")[1];
			The5zigAPI.getLogger().info(afterMsg);
			String map = "";

			Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
				map = matcher.group(1);
			}
			DR.activeMap = DR.mapsPool.get(map.toLowerCase());
		}

		else if (message.contains("§lYou are a ") && gameMode != null) {
			String afterMsg = message.split(ChatColor.stripColor("You are a "))[1];
			switch (afterMsg) {
			case "DEATH!":
				DR.role = "Death";
				break;
			case "RUNNER!":
				DR.role = "Runner";
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (DR.activeMap != null) {
							The5zigAPI.getLogger().info("Loading PB...");

							ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());

							DR.currentMapPB = api.getPersonalBest(DR.activeMap);
							if (DR.currentMapPB == null)
								DR.currentMapPB = "No Personal Best";
							The5zigAPI.getLogger().info("Loading WR...");
							DR.currentMapWR = api.getWorldRecord(DR.activeMap);
							DR.currentMapWRHolder = api.getWorldRecordHolder(DR.activeMap);
							if (DR.currentMapWR == null)
								DR.currentMapWR = "No Record";
							if (DR.currentMapWRHolder == null)
								DR.currentMapWRHolder = "Unknown";
						}
					}
				}).start();

				break;
			}
		} else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §aCheckpoint Reached! §7") && ActiveGame.is("dr")
				&& DR.role == "Runner") {
			// No more double tokens weekends Niklas :>)
			if (!(DR.checkpoints == DR.activeMap.getCheckpoints())) {
				DR.checkpoints++;

			}

			String data[] = ChatColor.stripColor(message).trim().split("\\+");
			int tokens = Integer.parseInt(data[1].trim().replaceAll("Tokens", "").trim());
			HiveAPI.tokens += tokens;
		} else if (message.equals("§8▍ §cDeathRun§8 ▏ §cYou have been returned to your last checkpoint!")
				&& ActiveGame.is("dr") && DR.role == "Runner") {
			DR.deaths++;
		} else if (message.contains("§6 (") && message.contains("§6)")
				&& message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && ActiveGame.is("dr")
				&& DR.role == "Death") {
			DR.kills++;
		}

		else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §a§lVote received.") && Setting.AUTOVOTE.getValue()) {
			DR.hasVoted = true;
		} else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §6§e§e§l6. §f§cRandom map ") && !DR.hasVoted
				&& Setting.AUTOVOTE.getValue()) {
			/*
			 * 
			 * Multi-threading to avoid lag on older machines
			 * 
			 */

			new Thread(new Runnable() {
				@Override
				public void run() {
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(DR.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("dr"));
					
					
					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					
					for (String s : votesCopy) {

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §cDeathRun§8 ▏ §6§e§e§l", "")
								.replaceAll("▍ DeathRun ▏", "").trim();
						String[] toConsider = ChatColor.stripColor(data[1]).split("\\[");
						String consider = ChatColor.stripColor(toConsider[0]).trim().replaceAll(" ", "_").toUpperCase();

						String votes = toConsider[1].split(" ")[0].trim();

						The5zigAPI.getLogger().info("trying to match " + consider);
						if (parsedMaps.contains(consider)) {
							votesindex.add(votes + "-" + index);
							The5zigAPI.getLogger()
									.info("Added " + consider + " Index #" + index + " with " + votes + " votes");
						} else {
							The5zigAPI.getLogger().info(consider + " is not a favourite");
						}

						if (index.equals("5")) {
							if (votesindex.size() != 0) {
								for (String n : votesindex) {
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);

								DR.votesToParse.clear();
								DR.hasVoted = true;
								// we can't really get the map name at this point
								The5zigAPI.getAPI()
										.messagePlayer(Log.info + "Automatically voted for map §6#" + finalindex);
								return;
							} else if(Setting.AUTOVOTE_RANDOM.getValue()){
								The5zigAPI.getLogger().info("Done, couldn't find matches - Voting Random");
								The5zigAPI.getAPI().sendPlayerMessage("/v 6");
								The5zigAPI.getAPI().messagePlayer(Log.info + "§eAutomatically voted for §cRandom map");
								DR.votesToParse.clear();
								DR.hasVoted = true;
								// he hasn't but we don't want to check again and again
								return;
							}
						}

					}

				}
			}).start();
		} else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §6§e§e§l") && !DR.hasVoted && Setting.AUTOVOTE.getValue()) {
			DR.votesToParse.add(message);
		}

		else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
			// " §6§m §f ItsNiklass's Stats §6§m "
			// Advanced Records
			DR.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		} else if (message.startsWith("§3 ")) {

			DR.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found entry");

			return true;
		} else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
			// TODO Coloring
			DR.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");

			return true;
		} else if ((message.equals("                      §6§m                  §6§m                  ")
				&& !message.startsWith("§o "))) {
			The5zigAPI.getLogger().info("found footer");
			DR.footerToSend.add(message);
			The5zigAPI.getLogger().info("executed /records");
			if (DR.footerToSend.contains("                      §6§m                  §6§m                  ")) {
				// Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable() {
					@Override
					public void run() {
						DR.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try {
							DRRank rank = null;
							ApiDR api = new ApiDR(DR.lastRecords);

							Double ppg = Setting.DR_SHOW_POINTSPERGAME.getValue()
									? Math.round(((double) api.getPoints() / (double) api.getGamesPlayed()) * 10d) / 10d
									: null;
							Integer rwr = Setting.DR_SHOW_RUNNERWINRATE.getValue() ? (int) (Math
									.floor(((double) api.getVictoriesAsRunner() / (double) api.getGamesPlayedAsRunner())
											* 1000d)
									/ 10d) : null;
							Double dpg = Setting.DR_SHOW_DEATHSPERGAME.getValue()
									? (double) (Math.floor(
											((double) api.getDeaths() / (double) api.getGamesPlayedAsRunner()) * 10d)
											/ 10d)
									: null;
							String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue()
									? api.getParentMode().getNetworkTitle()
									: "";
							ChatColor rankColor = null;
							if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
								rankColor = api.getParentMode().getNetworkRankColor();
							}
							long points = 0;
							Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
							Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements()
									: null;
							String rankTitleDR = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;

							int monthlyRank = (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()
									&& api.getLeaderboardsPlacePoints(349) < api.getPoints()) ? api.getMonthlyRank()
											: 0;
							if (rankTitleDR != null)
								rank = DRRank.getFromDisplay(rankTitleDR);
							List<String> messages = new ArrayList<String>();
							messages.addAll(DR.messagesToSend);
							Iterator<String> it = messages.iterator();
							for (String s : messages) {

								if (s.trim().endsWith("'s Stats §6§m")) {
									// " §6§m §f ItsNiklass's Stats §6§m "
									// "§6§m §f ItsNiklass's Stats §6§m"
									The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = api.getParentMode().getCorrectName();
									if (correctUser.contains("nicked player"))
										correctUser = "Nicked/Not found";
									sb.append("          §6§m                  §f ");
									The5zigAPI.getLogger().info("Added base...");
									if (rankColor != null) {
										sb.append(rankColor).append(correctUser);
										The5zigAPI.getLogger().info("Added colored user...");
									} else {
										sb.append(correctUser);
										The5zigAPI.getLogger().info("Added white user...");
									}
									sb.append("§f's Stats §6§m                  ");
									The5zigAPI.getLogger().info("Added end...");
									The5zigAPI.getAPI().messagePlayer("§o " + sb.toString());

									if (rankTitle != null && rankTitle.contains("nicked player"))
										rankTitle = "Nicked/Not found";
									if (!rankTitle.equals("Nicked/Not found") && !rankTitle.isEmpty()) {
										if (rankColor == null)
											rankColor = ChatColor.WHITE;
										The5zigAPI.getAPI().messagePlayer("§o           " + "§6§m       §6" + " ("
												+ rankColor + rankTitle + "§6) " + "§m       ");
									}
									continue;
								} else if (s.startsWith("§3 Points: §b")) {
									StringBuilder sb = new StringBuilder();
									sb.append("§3 Points: §b");
									points = Long.parseLong(s.replaceAll("§3 Points: §b", ""));
									sb.append(points);
									if (rank != null)
										sb.append(" (").append(rank.getTotalDisplay());
									if (Setting.DR_SHOW_POINTS_TO_NEXT_RANK.getValue())
										sb.append(" / ").append(rank.getPointsToNextRank((int) points));
									if (rank != null)
										sb.append("§b)");
									The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
									continue;

								}

								The5zigAPI.getAPI().messagePlayer("§o " + s);

							}

							if (ppg != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + ppg);
							}
							if (achievements != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/65");
							}
							if (rwr != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Runner-Winrate: §b" + rwr + "%");
							}
							if (dpg != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Deaths per Game: §b" + dpg);
							}
							if (monthlyRank != 0) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
							}
							if (lastGame != null) {
								Calendar lastSeen = Calendar.getInstance();
								lastSeen.setTimeInMillis(lastGame.getTime());

								The5zigAPI.getAPI().messagePlayer(
										"§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
							}

							for (String s : DR.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}

							DR.messagesToSend.clear();
							DR.footerToSend.clear();
							DR.isRecordsRunning = false;

						} catch (Exception e) {
							e.printStackTrace();
							if (e.getCause() instanceof FileNotFoundException) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								DR.messagesToSend.clear();
								DR.footerToSend.clear();
								DR.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error
									+ "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

							for (String s : DR.messagesToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for (String s : DR.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer(
									"§o " + "                      §6§m                  §6§m                  ");
							DR.messagesToSend.clear();
							DR.footerToSend.clear();
							DR.isRecordsRunning = false;
						}
					}
				}).start();
				return true;

			}

		}

		else if (message.contains("§lYou are a ")) {
			gameMode.setState(GameState.GAME);
		}

		else if (message.equals("§8▍ §cDeathRun§8 ▏ §6The round has started!")) {
			Timer timer = new Timer();
			ScoreboardFetcherTask sft = new ScoreboardFetcherTask();
			timer.schedule(sft, 1500);
		}

		else if (message.startsWith("§8▍ §cDeathRun§8 ▏") && message.contains("§3 finished §b")
				&& message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && !message.endsWith(" ")) {
			// "§8▍ §cDeathRun§8 ▏ §b §aItsNiklass§3 finished §b1st§3. §7(01:10.574)"
			String time = (message.split("§7\\("))[1].replaceAll("\\)", "");
			String[] data = time.split(":");
			int minutes = Integer.parseInt(data[0]);
			// data[1 ] is seconds.milliseconds
			double secondsMillis = Double.parseDouble(data[1]);
			double finalTime = 60 * minutes + secondsMillis; // e.g, You finished in 01:51.321 = 01*60 + 51.321 =
																// 111.321

			new Thread(new Runnable() {
				@Override
				public void run() {
					ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
					double wr = api.getRawWorldRecord(DR.activeMap);
					double diff = (Math.round((finalTime - wr) * 1000d)) / 1000d;
					int finalPb = 0;

					String pb = DR.currentMapPB;
					String[] pbData = pb.split(":");
					try {
						finalPb = Integer.parseInt(pbData[0]) * 60 + Integer.parseInt(pbData[1]);
					} catch (Exception e) {
						finalPb = -1;
					}

					int pbDiff = ((int) Math.floor(finalTime)) - finalPb;

					if (diff == 0) {
						// Lets make this more important lmao
						// All this has a reason, I may overdid it but I learned smth about tags
						The5zigAPI.getAPI().messagePlayer(
								"    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer(
								Log.info + "   §c§lCongratulations! You §4§ltied §c§lthe §4§lWorld Record§c§l!");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer(
								"    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer(message);
					} else if (diff < 0) {
						The5zigAPI.getAPI().messagePlayer(
								"    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI()
								.messagePlayer(Log.info + "   §c§lCongratulations! §4§lYou beat the World Record!!!");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer("§c  ");
						The5zigAPI.getAPI().messagePlayer(
								"    §c§m                                                                                    ");
						The5zigAPI.getAPI().messagePlayer(message);
					} else if (finalPb == -1) {
						The5zigAPI.getAPI()
								.messagePlayer(message + " §3The World Record is §b" + diff + "§3 seconds away!");
					} else if (pbDiff > 0) {
						The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff
								+ "§3 seconds away! Your Personal Best is §b" + pbDiff + " §3seconds away!");
					} else if (pbDiff == 0) {
						The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff
								+ "§3 seconds away! You tied your Personal Best!");
					} else {
						The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff
								+ "§3 seconds away! You beat your Personal Best by §b" + -pbDiff + " §3seconds!");
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

	private class ScoreboardFetcherTask extends TimerTask {

		@Override
		public void run() {
			for (Map.Entry<String, Integer> e : The5zigAPI.getAPI().getSideScoreboard().getLines().entrySet()) {
				if (e.getValue() == 3) {
					TIMV.gameID = ChatColor.stripColor(e.getKey().trim());
				}
			}

		}

	}
}
