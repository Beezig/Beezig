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
import tk.roccodev.zta.games.MIMV;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.mimv.MIMVRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiMIMV;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MIMVListener extends AbstractGameListener<MIMV> {

	@Override
	public Class<MIMV> getGameMode() {
		return MIMV.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("MIMV");
	}

	@Override
	public void onGameModeJoin(MIMV gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("MIMV");
		IHive.genericJoin();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());

					ApiMIMV api = new ApiMIMV(The5zigAPI.getAPI().getGameProfile().getName());

					if (sb != null && sb.getTitle().contains("Your MIMV Stats")) {
						int points = sb.getLines().get(ChatColor.AQUA + "Karma");
						APIValues.MIMVpoints = (long) points;

						MIMV.rankObject = MIMVRank.getFromDisplay(api.getTitle());
						MIMV.rank = MIMV.rankObject.getTotalDisplay();

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	@Override
	public boolean onServerChat(MIMV gameMode, String message) {

		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("MIMV Color Debug: (" + message + ")");
		} 
		else if(message.startsWith("§8▍ §c§lMurder§8 ▏ §2§l+§a") && message.endsWith("karma")) {
			String k = message.split("§a")[1].replace(" karma", "").trim();
			int karma = Integer.parseInt(k);
			APIValues.MIMVpoints += karma;
			MIMV.gamePts += karma;
		
		}
		else if (message.startsWith("§8▍ §c§lMurder§8 ▏ §a§lVote received. §3Your map now")) {
			MIMV.hasVoted = true;
		} else if (message.startsWith("§8▍ §c§c§lMurder§8§l ▏ §6§l§e§l§e§l6. §f§6") && Setting.AUTOVOTE.getValue()
				&& !MIMV.hasVoted) {
			MIMV.votesToParse.add(message);
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(MIMV.votesToParse);
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("mimv"));

					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();

					for (String s : votesCopy) {

						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0])
								.replaceAll("§8▍ §c§c§lMurder§8§l ▏ §6§l§e§l§e§l", "").replaceAll("▍ Murder ▏", "")
								.trim();
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
						if (index.equals("6")) {
							if (votesindex.size() != 0) {
								for (String n : votesindex) {
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);

								MIMV.votesToParse.clear();
								MIMV.hasVoted = true;
								// we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer(
										"§8▍ §c§c§lMurder§8§l ▏ " + "§eAutomatically voted for map §6#" + finalindex);
								return;
							} else {
								The5zigAPI.getLogger().info("Done, couldn't find matches");

								MIMV.votesToParse.clear();
								MIMV.hasVoted = true;
								// he hasn't but we don't want to check again and again
								return;
							}
						}
					}
				}
			}).start();
		} else if (message.startsWith("§8▍ §c§c§lMurder§8§l ▏ §6§l§e§l§e§") && Setting.AUTOVOTE.getValue()
				&& !MIMV.hasVoted) {
			MIMV.votesToParse.add(message);
		} else if (message.startsWith("§8▍ §c§lMurder§8 ▏ §3Voting has ended! §bThe map §f")) {
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §c§lMurder§8 ▏ §3Voting has ended! §bThe map ")[1];
			String map = "";
			Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
				map = matcher.group(1);
			}

			MIMV.map = map;

		} else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
			MIMV.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		} else if (message.startsWith("§3 ")) {

			MIMV.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found entry");

			return true;
		} else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
			MIMV.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");

			return true;
		} else if ((message.equals("                      §6§m                  §6§m                  ")
				&& !message.startsWith("§o "))) {
			The5zigAPI.getLogger().info("found footer");
			MIMV.footerToSend.add(message);
			The5zigAPI.getLogger().info("executed /records");
			if (MIMV.footerToSend.contains("                      §6§m                  §6§m                  ")) {
				// Advanced Records - send
				The5zigAPI.getLogger().info("Sending adv rec");
				new Thread(new Runnable() {
					@Override
					public void run() {
						MIMV.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try {

							ApiMIMV api = new ApiMIMV(MIMV.lastRecords);
							MIMVRank rank = null;

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
							String rankTitleMIMV = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
							if (rankTitleMIMV != null)
								rank = MIMVRank.getFromDisplay(rankTitleMIMV);

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
							// HiveAPI.getLeaderboardsPlacePoints(349, "MIMV") <
							// HiveAPI.DRgetPoints(MIMV.lastRecords)) ?
							// HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

							List<String> messages = new ArrayList<String>();
							messages.addAll(MIMV.messagesToSend);
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
								} else if (s.startsWith("§3 Karma: §b")) {
									StringBuilder sb = new StringBuilder();
									sb.append("§3 Karma: §b");
									points = Long.parseLong(s.replaceAll("§3 Karma: §b", ""));
									sb.append(points);
									if (rank != null)
										sb.append(" (").append(rank.getTotalDisplay());
									if (Setting.MIMV_SHOW_POINTS_TO_NEXT_RANK.getValue())
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
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/37");
							}

							if (Setting.MIMV_SHOW_WINRATE.getValue()) {
								double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr) + "%");
							}
							if (Setting.MIMV_SHOW_KD.getValue()) {
								double kd = (double) ((double) kills / (double) deaths);
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 K/D: §b" + df.format(kd));
							}
							if (Setting.MIMV_SHOW_PPG.getValue()) {
								double ppg = (double) ((double) points / (double) gamesPlayed);
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Karma Per Game: §b" + df1f.format(ppg));
							}
							if (Setting.MIMV_SHOW_KPG.getValue()) {
								double kpg = (double) ((double) kills / (double) gamesPlayed);
								The5zigAPI.getAPI().messagePlayer("§o " + "§3 Kills Per Game: §b" + df.format(kpg));
							}

							if (lastGame != null) {
								Calendar lastSeen = Calendar.getInstance();
								lastSeen.setTimeInMillis(lastGame.getTime());
								The5zigAPI.getAPI().messagePlayer(
										"§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
							}

							for (String s : MIMV.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}

							MIMV.messagesToSend.clear();
							MIMV.footerToSend.clear();
							MIMV.isRecordsRunning = false;

						} catch (Exception e) {
							e.printStackTrace();
							if (e.getCause() instanceof FileNotFoundException) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								MIMV.messagesToSend.clear();
								MIMV.footerToSend.clear();
								MIMV.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error
									+ "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

							for (String s : MIMV.messagesToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for (String s : MIMV.footerToSend) {
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer(
									"§o " + "                      §6§m                  §6§m                  ");
							MIMV.messagesToSend.clear();
							MIMV.footerToSend.clear();
							MIMV.isRecordsRunning = false;
						}
					}
				}).start();
				return true;
			}
		}

		return false;

	}

	@Override
	public void onTitle(MIMV gameMode, String title, String subTitle) {
		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("MIMV TitleColor Debug: (" +

					title != null ? title
							: "ERR_TITLE_NULL"

									+ " *§* " +

									subTitle != null ? subTitle
											: "ERR_SUBTITLE_NULL"

													+ ")");
		}
		if (subTitle != null && subTitle.contains("You will be") && subTitle.contains("this round!")) {
			String role = APIUtils.capitalize(title.replace("§r", "").toLowerCase().trim());
			MIMV.role = role.startsWith("§a") ? "§aCitizen" : (role.startsWith("§c") ? "§cMurderer" : "§9Detective");
			DiscordUtils.updatePresence("Investigating in Murder in Mineville", "Playing on " + MIMV.map, "game_mimv");
		}
	}

	@Override
	public void onServerConnect(MIMV gameMode) {
		MIMV.reset(gameMode);
	}

}
