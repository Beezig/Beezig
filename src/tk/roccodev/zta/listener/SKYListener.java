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
import tk.roccodev.zta.games.SKY;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.sky.SKYRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiSKY;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SKYListener extends AbstractGameListener<SKY> {

	@Override
	public Class<SKY> getGameMode() {
		return SKY.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("SKY");
	}

	@Override
	public void onGameModeJoin(SKY gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("SKY");
		IHive.genericJoin();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());

					if (sb != null && sb.getTitle().contains("Your SKY")) {
						if(sb.getTitle().contains("Your SKYT")) SKY.mode = "Teams";
						else if(sb.getTitle().contains("Your SKYD")) SKY.mode = "Duos";
						else SKY.mode = "Solo";
					
						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						SKY.apiKills = sb.getLines().get(ChatColor.AQUA + "Kills");
						SKY.apiDeaths = sb.getLines().get(ChatColor.AQUA + "Deaths");
						APIValues.SKYpoints = (long) points;
					}

					
					ApiSKY api = new ApiSKY(The5zigAPI.getAPI().getGameProfile().getName());
					SKY.totalKills = Math.toIntExact(api.getKills());
					SKY.rankObject = SKYRank
							.getFromDisplay(api.getTitle());
					SKY.rank = SKY.rankObject.getTotalDisplay();
					SKY.updateKdr();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	@Override
	public boolean onServerChat(SKY gameMode, String message) {

		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("SKY Color Debug: (" + message + ")");
		}

		if (message.startsWith("§8▍ §b§lSky§e§lWars§8 ▏ §3Voting has ended! §bThe map §f")) {
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §b§lSky§e§lWars§8 ▏ §3Voting has ended! §bThe map ")[1];
			String map = "";
			Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
				map = matcher.group(1);
			}

			SKY.map = map;

		}

		// Autovoting

		else if(message.endsWith("§c§lYou died. §6Better luck next time!")) {
			SKY.deaths++;
			SKY.updateKdr();
		}
		else if (message.startsWith("§8▍ §b§lSky§e§lWars§8 ▏ §a§lVote received. §3Your map now has")
				&& Setting.AUTOVOTE.getValue()) {
			SKY.hasVoted = true;
		} else if (message.startsWith("§8▍ §b§b§lSky§e§l§e§lWars§8§l ▏ §6§l§e§l§e§l6. §f§6") && !SKY.hasVoted
				&& Setting.AUTOVOTE.getValue()) {
			BED.votesToParse.add(message);
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(SKY.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("sky"));

					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					for (String s : votesCopy) {

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0])
								.replaceAll("§8▍ §b§b§lSky§e§l§e§lWars§8§l ▏ §6§l§e§l§e§l", "")
								.replaceAll("▍ SkyWars ▏", "").trim();
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

								SKY.votesToParse.clear();
								SKY.hasVoted = true;
								// we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer(
										"§8▍ §6SKY§8 ▏ " + "§eAutomatically voted for map §6#" + finalindex);
								return;
							} else {
								The5zigAPI.getLogger().info("Done, couldn't find matches");

								SKY.votesToParse.clear();
								SKY.hasVoted = true;
								// he hasn't but we don't want to check again and again
								return;
							}
						}
					}
				}
			}).start();
		} else if (message.startsWith("§8▍ §b§b§lSky§e§l§e§lWars§8§l ▏ §6§l§e§l§e§l") && !SKY.hasVoted
				&& Setting.AUTOVOTE.getValue()) {
			SKY.votesToParse.add(message);
		}
		else if(message.contains("§e, noble fighter for the ")) {
			String team = message.split("the")[1].replace("§eteam!", "").replaceAll("team!", "").trim();
			SKY.team = team;
			
			String teamSize = SKY.mode == null ? "0" : (SKY.mode.equals("Solo") ? "1" : (SKY.mode.equals("Duos") ? "2" : "4"));


			DiscordUtils.updatePresence("Crossing the skies in SkyWars: " + SKY.mode, "Playing on " + SKY.map, "game_skywars");
		}

		// Advanced Records

		else if(message.startsWith("§8▍ §eTokens§8 ▏ §7You earned §f15§7 tokens!")) {
			SKY.kills++;
			SKY.totalKills++;
			SKY.updateKdr();
		}
		
		else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
			SKY.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		} else if (message.startsWith("§3 ")) {

			SKY.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found entry");

			return true;
		} else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
			SKY.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");

			return true;
		} else if ((message.equals("                      §6§m                  §6§m                  ")
				&& !message.startsWith("§o "))) {
			The5zigAPI.getLogger().info("found footer");
			SKY.footerToSend.add(message);
			The5zigAPI.getLogger().info("executed /records");
			if (SKY.footerToSend.contains("                      §6§m                  §6§m                  ")) {
				// Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable() {
					@Override
					public void run() {
						SKY.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try {

							ApiSKY api = new ApiSKY(SKY.lastRecords);
							SKYRank rank = null;

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
							String rankTitleSKY = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
							if (rankTitleSKY != null)
								rank = SKYRank.getFromDisplay(rankTitleSKY);

							int kills = 0;
							long points = 0;
							int deaths = 0;
							int gamesPlayed = 0;
							int victories = 0;

							long timeAlive = 0;

							Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
							Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements()
									: null;
							;

							// int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() &&
							// HiveAPI.getLeaderboardsPlacePoints(349, "SKY") <
							// HiveAPI.DRgetPoints(SKY.lastRecords)) ?
							// HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

							List<String> messages = new ArrayList<String>();
							messages.addAll(SKY.messagesToSend);
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
									if (Setting.SKY_SHOW_POINTS_TO_NEXT_RANK.getValue())
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
								} else if (s.startsWith("§3 Kills: §b")) {
									kills = Integer
											.parseInt(ChatColor.stripColor(s.replaceAll("§3 Kills: §b", "").trim()));
								} else if (s.startsWith("§3 Deaths: §b")) {
									deaths = Integer
											.parseInt(ChatColor.stripColor(s.replaceAll("§3 Deaths: §b", "").trim()));
								}

								The5zigAPI.getAPI().messagePlayer("§o " + s);

							}

							if (achievements != null) {
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "");
							}

							if (Setting.SKY_SHOW_WINRATE.getValue()) {
								double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr) + "%");
							}
							if(Setting.SKY_SHOW_KD.getValue()) {
								double kd = (double) ((double)kills/(double)deaths);
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 K/D: §b" + df.format(kd));
							}

						

							if (lastGame != null) {
								Calendar lastSeen = Calendar.getInstance();
								lastSeen.setTimeInMillis(lastGame.getTime());
								The5zigAPI.getAPI().messagePlayer(
										"§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
							}

							for (String s : SKY.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}

							SKY.messagesToSend.clear();
							SKY.footerToSend.clear();
							SKY.isRecordsRunning = false;

						} catch (Exception e) {
							e.printStackTrace();
							if (e.getCause() instanceof FileNotFoundException) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								SKY.messagesToSend.clear();
								SKY.footerToSend.clear();
								SKY.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error
									+ "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

							for (String s : SKY.messagesToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for (String s : SKY.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer(
									"§o " + "                      §6§m                  §6§m                  ");
							SKY.messagesToSend.clear();
							SKY.footerToSend.clear();
							SKY.isRecordsRunning = false;
						}
					}
				}).start();
				return true;

			}

		}

		return false;

	}

	@Override
	public void onTitle(SKY gameMode, String title, String subTitle) {
		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("SKY TitleColor Debug: (" +

					title != null ? title
							: "ERR_TITLE_NULL"

									+ " *§* " +

									subTitle != null ? subTitle
											: "ERR_SUBTITLE_NULL"

													+ ")");
		}
	}

	@Override
	public void onServerConnect(SKY gameMode) {
		SKY.reset(gameMode);
	}

}
