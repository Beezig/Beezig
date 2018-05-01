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
import tk.roccodev.zta.games.GRAV;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.grav.GRAVRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiGRAV;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class GRAVListener extends AbstractGameListener<GRAV> {

	@Override
	public Class<GRAV> getGameMode() {
		return GRAV.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("GRAV");
	}

	@Override
	public void onGameModeJoin(GRAV gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("GRAV");
		IHive.genericJoin();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(1000);
					//Scoreboard doesn't load otherwise ???
					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());

					if (sb != null && sb.getTitle().contains("Your GRAV")) {

						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						GRAV.apiGamesPlayed = sb.getLines().get(ChatColor.AQUA + "Games Played");
						GRAV.apiVictories = sb.getLines().get(ChatColor.AQUA + "Victories");

						APIValues.GRAVpoints = (long) points;
					}

					ApiGRAV api = new ApiGRAV(The5zigAPI.getAPI().getGameProfile().getName());
					GRAV.rankObject = GRAVRank.getFromDisplay(api.getTitle());
					GRAV.rank = GRAV.rankObject.getTotalDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	@Override
	public boolean onServerChat(GRAV gameMode, String message) {

		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("GRAV Color Debug: (" + message + ")");
		}

		if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §3Voting has ended! §bThe maps")) {
			The5zigAPI.getLogger().info("Voting ended, parsing maps");
			String afterMsg = message.split("§8▍ §bGra§avi§ety§8 ▏ §3Voting has ended! §bThe maps ")[1];
			/*
			String map = "";
			Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
				map = matcher.group(1);
			}
			*/

			//GRAV.map = map;

		}

		// Autovoting

		else if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §a§lVote received. §3Your map now has")
						 && Setting.AUTOVOTE.getValue()) {
			GRAV.hasVoted = true;
		} else if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §6§e§e§l5.") && !GRAV.hasVoted
						   && Setting.AUTOVOTE.getValue()) {
			/*GRAV.votesToParse.add(message);
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(GRAV.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("grav"));

					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					for (String s : votesCopy) {

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0])
											   .replaceAll("§8▍ §b§b§lGrav§e§l§e§lWars§8§l ▏ §6§l§e§l§e§l", "")
											   .replaceAll("▍ GravWars ▏", "").trim();
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

								GRAV.votesToParse.clear();
								GRAV.hasVoted = true;
								// we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer(
										"§8▍ §6GRAV§8 ▏ " + "§eAutomatically voted for map §6#" + finalindex);
								return;
							} else {
								The5zigAPI.getLogger().info("Done, couldn't find matches");

								GRAV.votesToParse.clear();
								GRAV.hasVoted = true;
								// he hasn't but we don't want to check again and again
								return;
							}
						}
					}
				}
			}).start();
			*/

		} else if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §6§e§e§l") && !GRAV.hasVoted
						   && Setting.AUTOVOTE.getValue()) {
			GRAV.votesToParse.add(message);
		}

		else if(message.contains("§e, noble fighter for the ")) {
			DiscordUtils.updatePresence("Freefalling in Gravity" , "Falling on " + GRAV.maps[GRAV.currentMap], "game_grav");
		}

		// Advanced Records

		else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
			GRAV.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		} else if (message.startsWith("§3 ")) {

			GRAV.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found entry");

			return true;
		} else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
			GRAV.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");

			return true;
		} else if ((message.equals("                      §6§m                  §6§m                  ")
							&& !message.startsWith("§o "))) {
			The5zigAPI.getLogger().info("found footer");
			GRAV.footerToSend.add(message);
			The5zigAPI.getLogger().info("executed /records");
			if (GRAV.footerToSend.contains("                      §6§m                  §6§m                  ")) {
				// Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable() {
					@Override
					public void run() {
						GRAV.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try {

							ApiGRAV api = new ApiGRAV(GRAV.lastRecords);
							GRAVRank rank = null;

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
							String rankTitleGRAV = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
							if (rankTitleGRAV != null)
								rank = GRAVRank.getFromDisplay(rankTitleGRAV);

							long points = 0;
							int gamesPlayed = 0;
							int victories = 0;

							Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
							//Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements() : null;

							// int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() &&
							// HiveAPI.getLeaderboardsPlacePoints(349, "GRAV") <
							// HiveAPI.DRgetPoints(GRAV.lastRecords)) ?
							// HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

							List<String> messages = new ArrayList<String>();
							messages.addAll(GRAV.messagesToSend);
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
									if (Setting.GRAV_SHOW_POINTS_TO_NEXT_RANK.getValue())
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
								}

								The5zigAPI.getAPI().messagePlayer("§o " + s);

							}

							/*if (achievements != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "");
							}*/

							if(Setting.GRAV_SHOW_PPG.getValue()) {
								double ppg = (double) ((double)points / (double)gamesPlayed);
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points Per Game: §b" + df.format(ppg));
							}
							if(Setting.GRAV_SHOW_FINISHRATE.getValue()) {
								double fr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Finish-Rate: §b" + df1f.format(fr) + "%");
							}

							if (lastGame != null) {
								Calendar lastSeen = Calendar.getInstance();
								lastSeen.setTimeInMillis(lastGame.getTime());
								The5zigAPI.getAPI().messagePlayer(
										"§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
							}

							for (String s : GRAV.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}

							GRAV.messagesToSend.clear();
							GRAV.footerToSend.clear();
							GRAV.isRecordsRunning = false;

						} catch (Exception e) {
							e.printStackTrace();
							if (e.getCause() instanceof FileNotFoundException) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								GRAV.messagesToSend.clear();
								GRAV.footerToSend.clear();
								GRAV.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error
																	  + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

							for (String s : GRAV.messagesToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for (String s : GRAV.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer(
									"§o " + "                      §6§m                  §6§m                  ");
							GRAV.messagesToSend.clear();
							GRAV.footerToSend.clear();
							GRAV.isRecordsRunning = false;
						}
					}
				}).start();
				return true;

			}

		}

		return false;

	}

	@Override
	public void onTitle(GRAV gameMode, String title, String subTitle) {
		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("GRAV TitleColor Debug: (" +

												title != null ? title
												: "ERR_TITLE_NULL"

														  + " *§* " +

														  subTitle != null ? subTitle
														  : "ERR_SUBTITLE_NULL"

																	+ ")");
		}
	}

	@Override
	public void onServerConnect(GRAV gameMode) {
		GRAV.reset(gameMode);
	}

}
