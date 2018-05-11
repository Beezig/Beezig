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
					// Scoreboard doesn't load otherwise ???
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
			new Thread(new Runnable() {
				@Override
				public void run() {
					String afterMsg = message.split("§8▍ §bGra§avi§ety§8 ▏ §3Voting has ended! §bThe maps ")[1]
							.replace("have won!", "").trim(); // No stripColor because we want difficulties
					String[] maps = afterMsg.split(", ");
					GRAV.maps.addAll(Arrays.asList(maps));

					HashMap<String, Double> pbs = new ApiGRAV(The5zigAPI.getAPI().getGameProfile().getName())
							.getMapTimes();
					int i = 0;
					for (String s : maps) {
						String apiMap = GRAV.mapsPool.get(ChatColor.stripColor(s));
						The5zigAPI.getLogger().info(apiMap);
						DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
						df.setMinimumFractionDigits(3);
						/*pbs.entrySet().forEach(e -> {
							The5zigAPI.getLogger().info(e.getKey() + " / " + e.getValue());
						});*/
						Double pb = pbs.get(apiMap);

						if (pb == null) {
							pb = 0D;
							GRAV.toDisplay.put(++i, s + " §f| §7No PB §f| §c{f}");
						} else GRAV.toDisplay.put(++i, s + " §f| " + df.format(pb) + "s §f| §c{f}");

						GRAV.mapPBs.put(ChatColor.stripColor(s), pb);
					}
					GRAV.toDisplayWithFails.putAll(GRAV.toDisplay);
					/*GRAV.toDisplay.entrySet().forEach(e -> {
						The5zigAPI.getAPI().messagePlayer(e.getValue());
					});*/
				}
			}).start();

		} else if (message.contains(The5zigAPI.getAPI().getGameProfile().getName() + " §afinished §bStage")) {
			String[] dataFirst = message.split("§d");
			String secs = "";
			if(dataFirst.length == 2) {
				secs = message.split("§d")[1].replaceAll("seconds", "").trim();
			}
			else if(dataFirst.length == 3) {
				secs = message.split("§d")[2].replaceAll("seconds", "").trim();
			}

			double d = 0D;
			if (secs.contains(":")) {
				String data[] = secs.split(":"); // E.g., 1:04.212
				d = Double.parseDouble(Integer.parseInt(data[0]) * 60 + ""); // e.g, 60
				d += Double.parseDouble(data[1]); // e.g, 60 + 4.212
			} else {
				d = Double.parseDouble(secs);
			}
			//The5zigAPI.getLogger().info("D="+d);
			String map = GRAV.maps.get(GRAV.currentMap);
			Double mapPb = GRAV.mapPBs.get(ChatColor.stripColor(map));
			//The5zigAPI.getLogger().info(mapPb.toString());
			if (mapPb == 0.0D) {
				DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
				df.setMinimumFractionDigits(3);
				GRAV.toDisplay.put(GRAV.currentMap + 1, map + " §f| §7" + df.format(d) + "s §f| §c{f}");
				GRAV.toDisplayWithFails.put(GRAV.currentMap + 1, map + " §f| §7" + df.format(d) + "s §f| §c" + GRAV.fails);

			} else {
				double diff = mapPb == 0 ? mapPb - d : d - mapPb;
				DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
				df.setMinimumFractionDigits(3);
				GRAV.toDisplay.put(GRAV.currentMap + 1, map + " §f| "
																+ (diff < 0 ? "§a-" + df.format(Math.abs(diff)) : "§c+" + df.format(diff)) + "s §f| §c{f}");
				GRAV.toDisplayWithFails.put(GRAV.currentMap + 1,
						map + " §f| " + (diff < 0 ? "§a-" + df.format(Math.abs(diff)) : "§c+" + df.format(diff))
								+ "s §f| §c" + GRAV.fails);
			}
		} else if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §a§lVote received. §3Your map")) {
			GRAV.hasVoted = true;
		}

		else if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §6Map §7» §b")) {
			DiscordUtils.updatePresence("Freefalling in Gravity",
					"Falling on " + message.replace("§8▍ §bGra§avi§ety§8 ▏ §6Map §7» §b", ""), "game_grav");
			GRAV.currentMap++;
			GRAV.fails = 0;
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
							// Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ?
							// api.getAchievements() : null;

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

							/*
							 * if (achievements != null) { The5zigAPI.getAPI().messagePlayer("§o " +
							 * "§3 Achievements: §b" + achievements + ""); }
							 */

							if (Setting.GRAV_SHOW_PPG.getValue()) {
								double ppg = (double) ((double) points / (double) gamesPlayed);
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points Per Game: §b" + df1f.format(ppg));
							}
							if (Setting.GRAV_SHOW_FINISHRATE.getValue()) {
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
	public boolean onActionBar(GRAV gameMode, String message) {
		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("GRAV ActionBar Debug: (" + message + ")");
		}
		if (message.contains("Fails§8 » §c")) {
			String fails = message.split("Fails§8 » §c")[1];
			int f = Integer.parseInt(fails);
			if (f != GRAV.failsCache) {
				GRAV.toDisplayWithFails.put(GRAV.currentMap + 1,
						GRAV.toDisplay.get(GRAV.currentMap + 1).replace("{f}", ++GRAV.fails + ""));
				GRAV.failsCache = f;
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
