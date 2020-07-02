/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.listener;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.AdvancedRecords;
import eu.beezig.core.games.GRAV;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.grav.GRAVRank;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.ScoreboardUtils;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.GravStats;

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
        SendTutorial.send("grav_join");

        new Thread(() -> {
            try {

                Thread.sleep(1000);
                // Scoreboard doesn't load otherwise ???
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();


                if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                    int points = ScoreboardUtils.getValue(sb, "Points");
                    GRAV.apiGamesPlayed = ScoreboardUtils.getValue(sb, "Games Played");
                    GRAV.apiVictories = ScoreboardUtils.getValue(sb, "Victories");

                    APIValues.GRAVpoints = (long) points;
                }

                GravStats api = new GravStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));
                GRAV.rankObject = GRAVRank.getFromDisplay(api.getTitle());
                GRAV.rank = GRAV.rankObject.getTotalDisplay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public boolean onServerChat(GRAV gameMode, String message) {

        if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §3Voting has ended! §bThe maps")) {
            The5zigAPI.getLogger().info("Voting ended, parsing maps");
            new Thread(() -> {
                String afterMsg = message.split("§8▍ §bGra§avi§ety§8 ▏ §3Voting has ended! §bThe maps ")[1]
                        .replace("have won!", "").trim(); // No stripColor because we want difficulties
                String[] maps = afterMsg.split(", ");
                GRAV.maps.addAll(Arrays.asList(maps));

                Map<String, Long> pbs = new GravStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""))
                        .getMapRecords();
                int i = 0;
                for (String s : maps) {
                    String apiMap = GRAV.mapsPool.get(ChatColor.stripColor(s));
                    The5zigAPI.getLogger().info(apiMap);
                    DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                    df.setMinimumFractionDigits(3);
					/*pbs.entrySet().forEach(e -> {
						The5zigAPI.getLogger().info(e.getKey() + " / " + e.getValue());
					});*/
                    Long rawPb = pbs.get(apiMap);
                    Double pb;
                    if (rawPb == null) {
                        pb = 0D;
                        GRAV.toDisplay.put(++i, s + " §f| §7No PB §f| §c{f}");
                    } else {
                        pb = rawPb / 1000D;
                        GRAV.toDisplay.put(++i, s + " §f| " + df.format(pb) + "s §f| §c{f}");
                    }

                    GRAV.mapPBs.put(ChatColor.stripColor(s), pb);
                }
                GRAV.toDisplayWithFails.putAll(GRAV.toDisplay);
				/*GRAV.toDisplay.entrySet().forEach(e -> {
					The5zigAPI.getAPI().messagePlayer(e.getValue());
				});*/
            }).start();

        } else if (message.contains(The5zigAPI.getAPI().getGameProfile().getName() + " §afinished §bStage")) {
            String[] dataFirst = message.split("§d");
            String secs = "";
            if (dataFirst.length == 2) {
                secs = message.split("§d")[1].replaceAll("seconds", "").trim();
            } else if (dataFirst.length == 3) {
                secs = message.split("§d")[2].replaceAll("seconds", "").trim();
            }

            double d;
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
        } else if (message.startsWith("§8▍ §bGra§avi§ety§8 ▏ §6Map §7» §b")) {
            DiscordUtils.updatePresence("Freefalling in Gravity",
                    "Falling on " + message.replace("§8▍ §bGra§avi§ety§8 ▏ §6Map §7» §b", ""), "game_grav");
            GRAV.currentMap++;
            GRAV.fails = 0;
        }

        // Advanced Records

        else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            GRAV.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            GRAV.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            GRAV.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            GRAV.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (GRAV.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        GravStats api = new GravStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();
                        GRAVRank rank = null;

                        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                        DecimalFormat df = (DecimalFormat) nf;
                        df.setMaximumFractionDigits(2);
                        df.setMinimumFractionDigits(2);

                        DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                        df1f.setMaximumFractionDigits(1);
                        df1f.setMinimumFractionDigits(1);

                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue()
                                ? parent.getRank().getHumanName()
                                : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
                            rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                        }
                        String rankTitleGRAV = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        if (rankTitleGRAV != null)
                            rank = GRAVRank.getFromDisplay(rankTitleGRAV);

                        long points = 0;
                        int gamesPlayed = 0;
                        int victories = 0;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;
                        // Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ?
                        // api.getAchievements() : null;

                        // int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() &&
                        // HiveAPI.getLeaderboardsPlacePoints(349, "GRAV") <
                        // HiveAPI.DRgetPoints(AdvancedRecords.player)) ?
                        // HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

                        List<String> messages = new ArrayList<>(GRAV.messagesToSend);
                        for (String s : messages) {

                            if (s.trim().endsWith("'s Stats §6§m")) {
                                The5zigAPI.getLogger().info("Editing Header...");
                                StringBuilder sb = new StringBuilder();
                                String correctUser = parent.getUsername();
                                if (correctUser.contains("nicked player"))
                                    correctUser = "Nicked/Not found";
                                sb.append("§f          §6§m                  §f ");
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
                                The5zigAPI.getAPI().messagePlayer("§f " + sb.toString());

                                if (rankTitle != null && rankTitle.contains("nicked player"))
                                    rankTitle = "Nicked/Not found";
                                if (!rankTitle.equals("Nicked/Not found") && !rankTitle.isEmpty()) {
                                    if (rankColor == null)
                                        rankColor = ChatColor.WHITE;
                                    The5zigAPI.getAPI().messagePlayer("§f           " + "§6§m       §6" + " ("
                                            + rankColor + rankTitle + "§6) " + "§m       ");
                                }
                                continue;
                            }


                            String[] newData = s.split("\\: §b");
                            long currentValue = 0;
                            try {
                                currentValue = Long.parseLong(newData[1]);
                                newData[1] = Log.df(currentValue);
                                s = newData[0] + ": §b" + newData[1];
                            } catch (NumberFormatException ignored) {
                                s = newData[0] + ": §b" + newData[1];
                            }

                            if (s.startsWith("§3 Points: §b")) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("§3 Points: §b");
                                points = currentValue;
                                sb.append(newData[1]);
                                if (rank != null)
                                    sb.append(" (").append(rank.getTotalDisplay());
                                if (Setting.SHOW_RECORDS_POINTSTONEXTRANK.getValue())
                                    sb.append(" / ").append(rank.getPointsToNextRank((int) points));
                                if (rank != null)
                                    sb.append("§b)");

                                // if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

                                The5zigAPI.getAPI().messagePlayer("§f " + sb.toString().trim());
                                continue;
                            } else if (s.startsWith("§3 Victories: §b")) {
                                victories = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Games Played: §b")) {
                                gamesPlayed = Math.toIntExact(currentValue);
                            }

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }

                        /*
                         * if (achievements != null) { The5zigAPI.getAPI().messagePlayer("§f " +
                         * "§3 Achievements: §b" + achievements + ""); }
                         */

                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = (double) points / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points Per Game: §b" + df1f.format(ppg));
                        }
                        if (Setting.GRAV_SHOW_FINISHRATE.getValue()) {
                            double fr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Finish-Rate: §b" + df1f.format(fr) + "%");
                        }

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer(
                                    "§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : GRAV.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }

                        GRAV.messagesToSend.clear();
                        GRAV.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            GRAV.messagesToSend.clear();
                            GRAV.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : GRAV.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : GRAV.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§f " + "                      §6§m                  §6§m                  ");
                        GRAV.messagesToSend.clear();
                        GRAV.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;

            }

        }

        return false;

    }

    @Override
    public boolean onActionBar(GRAV gameMode, String message) {
        if (BeezigMain.isColorDebug) {
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
    public void onServerConnect(GRAV gameMode) {
        GRAV.reset(gameMode);
    }

}
