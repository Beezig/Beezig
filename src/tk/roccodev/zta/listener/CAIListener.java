package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.BP;
import tk.roccodev.zta.games.CAI;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiCAI;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					try {
						CAI.initDailyPointsWriter();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());

					if (sb != null && sb.getTitle().contains("Your CAI Stats")) {
						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						APIValues.CAIpoints = (long) points;
					}

					CAI.rankObject = CAIRank
							.getFromDisplay(new ApiCAI(The5zigAPI.getAPI().getGameProfile().getName()).getTitle());
					CAI.rank = CAI.rankObject.getTotalDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	@Override
	public boolean onServerChat(CAI gameMode, String message) {

		if (message.startsWith("§8▍ §bCAI§8 ▏ §3Voting has ended! §bThe map §f")) {
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §bCAI§8 ▏ §3Voting has ended! §bThe map ")[1];
			String map = "";
			Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
				map = matcher.group(1);
			}
			CAI.activeMap = map;
		}

		// Autovoting

		else if (message.startsWith("§8▍ §bCAI§8 ▏ §a§lVote received. §3Your map now has")
				&& Setting.AUTOVOTE.getValue()) {
			CAI.hasVoted = true;
		} else if (message.startsWith("§8▍ §bCAI§8 ▏ §6§e§e§l6. §f§cRandom map") && !CAI.hasVoted
				&& Setting.AUTOVOTE.getValue()) {
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(CAI.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("cai"));

					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					for (String s : votesCopy) {

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §bCAI§8 ▏ §6§e§e§l", "")
								.replaceAll("▍ CAI ▏", "").trim();
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

								CAI.votesToParse.clear();
								CAI.hasVoted = true;
								// we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer(
										"§8▍ §6CaI§8 ▏ " + "§eAutomatically voted for map §6#" + finalindex);
								return;
							} else if(Setting.AUTOVOTE_RANDOM.getValue()) {
								The5zigAPI.getLogger().info("Done, couldn't find matches - Voting random");
								The5zigAPI.getAPI().sendPlayerMessage("/v 6");
								The5zigAPI.getAPI().messagePlayer("(§8▍ §bCAI§8 ▏ " + "§eAutomatically voted for §cRandom map");
							
								CAI.votesToParse.clear();
								CAI.hasVoted = true;
								// he hasn't but we don't want to check again and again
								return;
							}
						}
					}
				}
			}).start();
		} else if (message.startsWith("§8▍ §bCAI§8 ▏ §6§e§e§l") && !CAI.hasVoted && Setting.AUTOVOTE.getValue()) {
			CAI.votesToParse.add(message);
		} 
		else if(message.equals("§8▍ §bCAI§8 ▏ §aYou have captured the enemy's team leader!")) {
			CAI.gamePoints += 5;
			APIValues.CAIpoints += 5;
			CAI.dailyPoints += 5;
		}
		else if(message.endsWith("§cCowboys Leader§7.")) {
		
			CAI.team = "§eIndians";
			
			DiscordUtils.updatePresence("Battling in Cowboys and Indians", "Playing as I on " + CAI.activeMap, "game_cai");
			
		}
		else if(message.endsWith("§eIndians Leader§7.")) {
			CAI.team = "§cCowboys";
			DiscordUtils.updatePresence("Battling in Cowboys and Indians", "Playing as C on " + CAI.activeMap, "game_cai");
		}
		else if (message.equals("§8▍ §bCAI§8 ▏ §7You received §f10 points §7for your team's capture.")) {

			HiveAPI.tokens += 5;
			CAI.gamePoints += 10;
			APIValues.CAIpoints += 10;
			CAI.dailyPoints += 10;

		}
		else if(message.equals("                             §eIndians have won!") && CAI.team != null && CAI.team.equals("§eIndians")) {
			CAI.gamePoints += 50;
			APIValues.CAIpoints += 50;
			CAI.dailyPoints += 50;
		}
		else if(message.equals("                             §cCowboys have won!") && CAI.team != null && CAI.team.equals("§cCowboys")) {
			CAI.gamePoints += 50;
			APIValues.CAIpoints += 50;
			CAI.dailyPoints += 50;
		}
		else if (message.startsWith("§8▍ §bCAI§8 ▏ §7You gained §f5 points §7for killing")) {
			
			
			CAI.gamePoints += 5;
			APIValues.CAIpoints += 5;
			CAI.dailyPoints += 5;
			
		} else if (message.endsWith("§7[Leader Alive Bonus]")
				&& message.startsWith("§8▍ §bCAI§8 ▏ §2+")) {

			String points = message.replace(" Points §7[Leader Alive Bonus]", "")
					.replace("§8▍ §bCAI§8 ▏ §2+ §a", "");

			CAI.gamePoints += Long.parseLong(points.trim());
			APIValues.CAIpoints += Long.parseLong(points.trim());
			CAI.dailyPoints += Long.parseLong(points.trim());

		}

		// Advanced Records

		else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
			CAI.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		} else if (message.startsWith("§3 ")) {

			CAI.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found entry");

			return true;
		} else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
			CAI.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");

			return true;
		} else if ((message.equals("                      §6§m                  §6§m                  ")
				&& !message.startsWith("§o "))) {
			The5zigAPI.getLogger().info("found footer");
			CAI.footerToSend.add(message);
			The5zigAPI.getLogger().info("executed /records");
			if (CAI.footerToSend.contains("                      §6§m                  §6§m                  ")) {
				// Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable() {
					@Override
					public void run() {
						CAI.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try {

							ApiCAI api = new ApiCAI(CAI.lastRecords);
							CAIRank rank = null;

							NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
							DecimalFormat df = (DecimalFormat) nf;
							df.setMaximumFractionDigits(2);
							df.setMinimumFractionDigits(2);

							DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
							df1f.setMaximumFractionDigits(1);
							df1f.setMinimumFractionDigits(1);

							String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue()
									? api.getParentMode().getNetworkTitle()
									: "";
							ChatColor rankColor = null;
							if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
								rankColor = api.getParentMode().getNetworkRankColor();
							}
							String rankTitleCAI = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
							if (rankTitleCAI != null)
								rank = CAIRank.getFromDisplay(rankTitleCAI);

							int kills = 0;
							long points = 0;
							int deaths = 0;
							int gamesPlayed = 0;
							int victories = 0;

							long timeAlive = 0;

							long catches = 0, captured = 0, caught = 0, captures = 0;

							Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
							Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements()
									: null;
							;

							// int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() &&
							// HiveAPI.getLeaderboardsPlacePoints(349, "CAI") <
							// HiveAPI.DRgetPoints(CAI.lastRecords)) ?
							// HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

							List<String> messages = new ArrayList<String>();
							messages.addAll(CAI.messagesToSend);
							for (String s : messages) {

								if (s.trim().endsWith("'s Stats §6§m")) {
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
									if (Setting.CAI_SHOW_POINTS_TO_NEXT_RANK.getValue())
										sb.append(" / ").append(rank.getPointsToNextRank((int) points));
									if (rank != null)
										sb.append("§b)");

									// if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

									The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
									continue;
								} else if (s.startsWith("§3 Victories: §b")) {
									victories = Integer.parseInt(
											ChatColor.stripColor(s.replaceAll("§3 Victories: §b", "").trim()));
								} else if (s.startsWith("§3 Games Played: §b")) {
									gamesPlayed = Integer.parseInt(
											ChatColor.stripColor(s.replaceAll("§3 Games Played: §b", "").trim()));
								} else if (s.startsWith("§3 Total Kills: §b")) {
									kills = Integer.parseInt(
											ChatColor.stripColor(s.replaceAll("§3 Total Kills: §b", "").trim()));
								} else if (s.startsWith("§3 Total Deaths: §b")) {
									deaths = Integer.parseInt(
											ChatColor.stripColor(s.replaceAll("§3 Total Deaths: §b", "").trim()));
								}

								else if (s.startsWith("§3 Time Alive: §b")) {
									timeAlive = Long.parseLong(
											ChatColor.stripColor(s.replaceAll("§3 Time Alive: §b", "").trim()));
									s = s.replaceAll(Long.toString(timeAlive), APIUtils.getTimePassed(timeAlive));
								}

								The5zigAPI.getAPI().messagePlayer("§o " + s);

							}

							if (achievements != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/44");
							}
							if (Setting.CAI_SHOW_WINRATE.getValue()) {
								double wr = (double) victories / (double) gamesPlayed;
								The5zigAPI.getAPI()
										.messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr * 100) + "%");
							}
							if (Setting.CAI_SHOW_CATCHES_CAUGHT.getValue()) {
								if (catches == 0)
									catches = api.getCatches();
								if (caught == 0)
									caught = api.getCaught();
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Cc/Ct: §b"
										+ df.format((double) ((double) catches / (double) caught)) + "");
							}
							if (Setting.CAI_SHOW_POINTSPG.getValue()) {
								double ppg = (double) points / (double) gamesPlayed;
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + df1f.format(ppg));
							}

							/*
							 * if(Setting.CAI_SHOW_WINRATE.getValue()){ double wr = Math.floor(((double)
							 * victories / (double) gamesPlayed) * 1000d) / 10d;
							 * The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr)
							 * + "%"); }
							 * 
							 * if(monthlyRank != 0){ The5zigAPI.getAPI().messagePlayer("§o " +
							 * "§3 Monthly Leaderboards: §b#" + monthlyRank); }
							 */

							if (lastGame != null) {
								Calendar lastSeen = Calendar.getInstance();
								lastSeen.setTimeInMillis(lastGame.getTime());
								The5zigAPI.getAPI().messagePlayer(
										"§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
							}

							for (String s : CAI.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}

							CAI.messagesToSend.clear();
							CAI.footerToSend.clear();
							CAI.isRecordsRunning = false;

						} catch (Exception e) {
							e.printStackTrace();
							if (e.getCause() instanceof FileNotFoundException) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								CAI.messagesToSend.clear();
								CAI.footerToSend.clear();
								CAI.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error
									+ "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

							for (String s : CAI.messagesToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for (String s : CAI.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer(
									"§o " + "                      §6§m                  §6§m                  ");
							CAI.messagesToSend.clear();
							CAI.footerToSend.clear();
							CAI.isRecordsRunning = false;
						}
					}
				}).start();
				return true;

			}

		}

		return false;

	}

	@Override
	public void onServerConnect(CAI gameMode) {
		CAI.reset(gameMode);
	}

}
