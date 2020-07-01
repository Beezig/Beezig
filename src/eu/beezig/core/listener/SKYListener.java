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
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.AdvancedRecords;
import eu.beezig.core.autovote.AutovoteUtils;
import eu.beezig.core.games.SKY;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.sky.SKYRank;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.ScoreboardUtils;
import eu.beezig.core.utils.StreakUtils;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.sky.SkyMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.SkyStats;

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
        SendTutorial.send("sky_join");

        new Thread(() -> {
            try {
                try {
                    SKY.initDailyPointsWriter();
                    Thread.sleep(500L);
                } catch (Exception e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();


                SkyStats api = new SkyStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));


                if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                    int points = ScoreboardUtils.getValue(sb, "Points");
                    SKY.apiKills = ScoreboardUtils.getValue(sb, "Kills");
                    SKY.apiDeaths = ScoreboardUtils.getValue(sb, "Deaths");
                    APIValues.SKYpoints = (long) points;
                } else {
                    SKY.apiKills = Math.toIntExact(api.getKills());
                    SKY.apiDeaths = Math.toIntExact(api.getDeaths());
                    APIValues.SKYpoints = api.getPoints();
                }

                SKY.totalKills = Math.toIntExact(api.getKills());
                SKY.rankObject = SKYRank.getFromDisplay(api.getTitle());
                SKY.rank = SKY.rankObject.getTotalDisplay();
                SKY.updateKdr();

                try {
                    if (SKY.attemptNew) {
                        SKY.monthly = api.getMonthlyProfile();
                        SKY.monthly.getPoints(); // Fetch (LazyObject)
                        SKY.hasLoaded = true;
                    }
                } catch (Exception e) {
                    SKY.attemptNew = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public boolean onServerChat(SKY gameMode, String message) {

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

        else if (message.endsWith("§c§lYou died. §6Better luck next time!")) {
            SKY.deaths++;
            SKY.updateKdr();
        } else if (message.startsWith("§8▍ §b§lSky§e§lWars§8 ▏ §a§lVote received. §3Your map now has")
                && Setting.AUTOVOTE.getValue()) {
            SKY.hasVoted = true;
        } else if (message.startsWith("§8▍ §b§lSky§e§lWars§8 ▏ §7§l5.") && !SKY.hasVoted
                && Setting.AUTOVOTE.getValue()) {
            SKY.votesToParse.add(message);
            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(SKY.votesToParse);

                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("sky"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();
                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {
                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0])
                            .replaceAll("§8▍ §b§lSky§e§lWars§8 ▏ §7§l", "")
                            .replaceAll("▍ SkyWars ▏", "").trim();
                    String[] toConsider = ChatColor.stripColor(data[1]).split("\\[");
                    String consider = ChatColor.stripColor(toConsider[0]).trim().replaceAll(" ", "_").toUpperCase();

                    System.out.println("VoteCopy: " + consider);

                    finalvoting.put(consider, Integer.parseInt(index));
                }


                for (String s : parsedMaps) {
                    if (finalvoting.containsKey(s)) {
                        votesindex.put(s, finalvoting.get(s));
                        break;
                    }
                }

                if (votesindex.size() != 0) {
                    System.out.println(votesindex.firstEntry().getKey());
                    The5zigAPI.getAPI().sendPlayerMessage("/v " + votesindex.firstEntry().getValue());
                    The5zigAPI.getAPI().messagePlayer(
                            "§8▍ §b§b§lSky§e§l§e§lWars§8§l ▏ " + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());

                } else if (Setting.AUTOVOTE_RANDOM.getValue()) {
                    The5zigAPI.getAPI().sendPlayerMessage("/v 6");
                    The5zigAPI.getAPI().messagePlayer(
                            "§8▍ §b§b§lSky§e§l§e§lWars§8§l ▏ §eAutomatically voted for §cRandom map§e.");
                }
                SKY.votesToParse.clear();
                SKY.hasVoted = true;


            }).start();
        } else if (message.startsWith("§8▍ §b§lSky§e§lWars§8 ▏ §7§l") && !SKY.hasVoted
                && Setting.AUTOVOTE.getValue()) {
            SKY.votesToParse.add(message);
        } else if (message.contains("§e§lWelcome to SkyWars")) {
            SKY.inGame = true;
            The5zigAPI.getAPI().sendPlayerMessage("/gameid");
            SKY.team = message.split("the")[1].replace("§eteam!", "").replaceAll("team!", "").trim();

            String teamSize = SKY.mode == null ? "0"
                    : (SKY.mode.equals("Solo") ? "1" : (SKY.mode.equals("Duos") ? "2" : "4"));

            DiscordUtils.updatePresence("Fighting in SkyWars: " + SKY.mode, "Playing on " + SKY.map, "game_skywars");
        }

        // Advanced Records

        else if (message.startsWith("§8▍ §eTokens§8 ▏ §7You earned §f15§7 tokens!")) {
            SKY.kills++;
            SKY.totalKills++;
            APIValues.SKYpoints += 5;
            SKY.gamePoints += 5;
            SKY.dailyPoints += 5;
            SKY.updateKdr();
        } else if (message.startsWith("§8▍ §eTokens§8 ▏ §7You earned §f50§7 tokens!")) {
            APIValues.SKYpoints += 20;
            SKY.dailyPoints += 20;
            SKY.gamePoints += 20;
            SKY.hasWon = true;
            SKY.winstreak++;
            if (SKY.winstreak >= SKY.bestStreak)
                SKY.bestStreak = SKY.winstreak;
            StreakUtils.incrementWinstreakByOne("sky");
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            SKY.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            SKY.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            SKY.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            SKY.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (SKY.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        SkyStats api = new SkyStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();
                        SKYRank rank = null;

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
                        String rankTitleSKY = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        if (rankTitleSKY != null)
                            rank = SKYRank.getFromDisplay(rankTitleSKY);

                        int kills = 0;
                        long points = 0;
                        int deaths = 0;
                        int gamesPlayed = 0;
                        int victories = 0;

                        long timeAlive = 0;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getUnlockedAchievements().size()
                                : null;

                        long monthlyRank = 0;
                        if (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()) {
                            try {
                                SkyMonthlyProfile monthly = api.getMonthlyProfile();
                                if (monthly != null) {
                                    monthlyRank = monthly.getPlace();
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        List<String> messages = new ArrayList<>(SKY.messagesToSend);
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
                            } else if (s.startsWith("§3 Kills: §b")) {
                                kills = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Deaths: §b")) {
                                deaths = Math.toIntExact(currentValue);
                            }

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }

                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Achievements: §b" + achievements + "");
                        }

                        if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                            double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Winrate: §b" + df1f.format(wr) + "%");
                        }
                        if (monthlyRank != 0) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Place: §b#" + monthlyRank);
                        }
                        if (Setting.SHOW_RECORDS_KDR.getValue()) {
                            double kd = (double) kills / (double) deaths;
                            The5zigAPI.getAPI().messagePlayer("§f §3 K/D: §b" + df.format(kd));
                        }
                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = (double) points / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points Per Game: §b" + df1f.format(ppg));
                        }
                        if (Setting.SHOW_RECORDS_KPG.getValue()) {
                            double kpg = (double) kills / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Kills Per Game: §b" + df1f.format(kpg));
                        }

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer(
                                    "§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : SKY.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }

                        SKY.messagesToSend.clear();
                        SKY.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            SKY.messagesToSend.clear();
                            SKY.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : SKY.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : SKY.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§f " + "                      §6§m                  §6§m                  ");
                        SKY.messagesToSend.clear();
                        SKY.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;

            }

        } else if (message.startsWith("§8▍ §b§lSky§e§lWars§8 ▏ §bYou can find all §emessages and game events §bat §a")) {
            SKY.gameId = message.replace("§8▍ §b§lSky§e§lWars§8 ▏ §bYou can find all §emessages and game events §bat §ahttp://hivemc.com/skywars/game/", "");
        }

        return false;

    }

    @Override
    public void onServerConnect(SKY gameMode) {
        SKY.reset(gameMode);
    }

}
