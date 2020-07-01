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
import eu.beezig.core.games.HIDE;
import eu.beezig.core.games.logging.hide.HideMapRecords;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.hide.HIDERank;
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
import pw.roccodev.beezig.hiveapi.wrapper.monthly.hide.HideMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.HideStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        SendTutorial.send("hide_join");

        new Thread(() -> {
            try {
                try {
                    HIDE.initDailyPointsWriter();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                Thread.sleep(1000);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();


                HideStats api = new HideStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));

                if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                    int points = ScoreboardUtils.getValue(sb, "Points");
                    APIValues.HIDEpoints = (long) points;
                } else {
                    APIValues.HIDEpoints = api.getPoints();
                }

                HIDE.rankObject = HIDERank.getFromDisplay(api.getTitle());
                HIDE.rank = HIDE.rankObject.getTotalDisplay();

                try {
                    if (HIDE.attemptNew) {
                        HIDE.monthly = api.getMonthlyProfile();
                        HIDE.monthly.getPoints(); // Fetch (LazyObject)
                        HIDE.hasLoaded = true;
                    }
                } catch (Exception e) {
                    HIDE.attemptNew = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public boolean onServerChat(HIDE gameMode, String message) {

        if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §3Voting has ended! §bThe map §f")) {
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
            HIDE.mostKills = HideMapRecords.getForMap(map);
            Log.addToSendQueue(Log.info + "Your current kills record on §b" + map + "§3 is §b" + HIDE.mostKills + "§3.");
        } else if (message.contains("for surviving §e")) {
            HIDE.timeAlive = message.split("for surviving §e")[1];
        }

        //Autovoting

        else if (message.contains("§b§lYou are a §f§lHIDER!")) {
            HIDE.inGame = true;
            The5zigAPI.getAPI().sendPlayerMessage("/gameid");
        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §a§lVote received. §3Your map now has ") && Setting.AUTOVOTE.getValue()) {
            HIDE.hasVoted = true;
        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l6. §6§cRandom Map") && !HIDE.hasVoted && Setting.AUTOVOTE.getValue()) {

            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(HIDE.votesToParse);

                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("hide"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();
                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {
                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §bHide§aAnd§eSeek§8 ▏ §6§e§e§l", "").replaceAll("▍ HideAndSeek ▏", "").trim();
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

                if (votesindex.size() == 0 && Setting.AUTOVOTE_RANDOM.getValue()) {
                    The5zigAPI.getAPI().sendPlayerMessage("/v 6");
                    The5zigAPI.getAPI().messagePlayer("§8▍ §bHide§aAnd§eSeek§8 ▏ " + "§eAutomatically voted for §cRandom map");

                } else {
                    System.out.println(votesindex.firstEntry().getKey());
                    The5zigAPI.getAPI().sendPlayerMessage("/v " + votesindex.firstEntry().getValue());
                    The5zigAPI.getAPI().messagePlayer("§8▍ §bHide§aAnd§eSeek§8 ▏ " + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());
                }
                HIDE.votesToParse.clear();
                HIDE.hasVoted = true;

            }).start();
        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l") && !HIDE.hasVoted && Setting.AUTOVOTE.getValue()) {
            HIDE.votesToParse.add(message);
        }

        //Advanced Records

        else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            HIDE.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            HIDE.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            HIDE.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6You have gained §e30 points§6")) {
            HIDE.kills++;
        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6You have gained §e200 points§6")) {
            APIValues.HIDEpoints += 200;
            HIDE.dailyPoints += 200;
            HIDE.hasWon = true;
            HIDE.winstreak++;
            if (HIDE.winstreak >= HIDE.bestStreak) HIDE.bestStreak = HIDE.winstreak;
            StreakUtils.incrementWinstreakByOne("hide");
        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §6You have gained §e50 points§6")) {
            APIValues.HIDEpoints += 50;
            HIDE.dailyPoints += 50;
            HIDE.hasWon = true;
            HIDE.winstreak++;
            if (HIDE.winstreak >= HIDE.bestStreak) HIDE.bestStreak = HIDE.winstreak;
            StreakUtils.incrementWinstreakByOne("hide");
        } else if (message.equals("                          §6§lYou are a §c§lSEEKER!")) {
            HIDE.inGame = true;
            HIDE.seeking = true;
            The5zigAPI.getAPI().sendPlayerMessage("/gameid");
            DiscordUtils.updatePresence("Playing Hide & Seek", "Seeking on " + HIDE.activeMap, "game_hide");
        } else if ((message.equals("                      §6§m                  §6§m                  ") && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            HIDE.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (HIDE.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                //Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        HideStats api = new HideStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();
                        HIDERank rank = null;


                        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                        DecimalFormat df = (DecimalFormat) nf;
                        df.setMaximumFractionDigits(2);
                        df.setMinimumFractionDigits(2);

                        DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                        df1f.setMaximumFractionDigits(1);
                        df1f.setMinimumFractionDigits(1);


                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? parent.getRank().getHumanName() : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
                            rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                        }
                        String rankTitleHIDE = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        if (rankTitleHIDE != null) rank = HIDERank.getFromDisplay(rankTitleHIDE);

                        int kills = 0;
                        long points = 0;
                        int deaths = 0;
                        int gamesPlayed = 0;
                        int victories = 0;
                        int killsSeeker = 0;
                        int killsHider = 0;
                        long timeAlive;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getUnlockedAchievements().size() : null;
                        Integer playedBlocks = Setting.HIDE_SHOW_AMOUNT_UNLOCKED.getValue() ? api.getBlockExperience().size() : null;


                        long monthlyRank = 0;
                        if (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()) {
                            try {
                                HideMonthlyProfile monthly = api.getMonthlyProfile();
                                if (monthly != null) {
                                    monthlyRank = monthly.getPlace();
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        List<String> messages = new ArrayList<>(HIDE.messagesToSend);
                        for (String s : messages) {

                            if (s.trim().endsWith("'s Stats §6§m")) {
                                The5zigAPI.getLogger().info("Editing Header...");
                                StringBuilder sb = new StringBuilder();
                                String correctUser = parent.getUsername();
                                if (correctUser.contains("nicked player")) correctUser = "Nicked/Not found";
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
                                    if (rankColor == null) rankColor = ChatColor.WHITE;
                                    The5zigAPI.getAPI().messagePlayer("§f           " + "§6§m       §6" + " (" + rankColor + rankTitle + "§6) " + "§m       ");
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
                                if (rank != null) sb.append(" (").append(rank.getTotalDisplay());
                                if (Setting.SHOW_RECORDS_POINTSTONEXTRANK.getValue())
                                    sb.append(" / ").append(rank.getPointsToNextRank((int) points));
                                if (rank != null) sb.append("§b)");

                                //if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

                                The5zigAPI.getAPI().messagePlayer("§f " + sb.toString().trim());
                                continue;
                            } else if (s.startsWith("§3 Victories: §b")) {
                                victories = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Games Played: §b")) {
                                gamesPlayed = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Kills as Seeker: §b")) {
                                killsSeeker = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Kills as Hider: §b")) {
                                killsHider = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Time Alive: §b")) {
                                timeAlive = currentValue;
                                s = s.replaceAll(Long.toString(timeAlive), APIUtils.getTimePassed(timeAlive));
                            }


                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }


                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Achievements: §b" + achievements + "/57");
                        }
                        if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                            double wr = (double) victories / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Winrate: §b" + df1f.format(wr * 100) + "%");
                        }
                        if (monthlyRank != 0) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Place: §b#" + monthlyRank);
                        }
                        if (Setting.HIDE_SHOW_SEEKER_KPG.getValue()) {
                            double skpg = (double) killsSeeker / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Seeker: Kills per Game: §b" + df.format(skpg));
                        }
                        if (Setting.HIDE_SHOW_HIDER_KPG.getValue()) {
                            double hkpg = (double) killsHider / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Hider: Kills per Game: §b" + df.format(hkpg));
                        }
                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = (double) points / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points per Game: §b" + df1f.format(ppg));
                        }

                        if (playedBlocks != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Played Blocks: §b" + playedBlocks);
                        }
                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer("§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }


                        for (String s : HIDE.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }


                        HIDE.messagesToSend.clear();
                        HIDE.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            HIDE.messagesToSend.clear();
                            HIDE.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : HIDE.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : HIDE.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer("§f " + "                      §6§m                  §6§m                  ");
                        HIDE.messagesToSend.clear();
                        HIDE.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;


            }

        } else if (message.startsWith("§8▍ §bHide§aAnd§eSeek§8 ▏ §bYou can find all §emessages and game events §bat §a")) {
            HIDE.gameId = message.replace("§8▍ §bHide§aAnd§eSeek§8 ▏ §bYou can find all §emessages and game events §bat §ahttps://hivemc.com/hide-and-seek/game/", "");
        }


        return false;

    }


    @Override
    public void onTick(HIDE gameMode) {
        if (The5zigAPI.getAPI().getSideScoreboard() == null) return;
        int i = HIDE.seeking ? 4 : 5;
        HashMap<String, Integer> lines = The5zigAPI.getAPI().getSideScoreboard().getLines();
        for (Map.Entry<String, Integer> e : lines.entrySet()) {
            if (e.getValue() == i && e.getKey().contains("§7 Points§6")) {
                int pts = Integer.parseInt(e.getKey().replace("§7 Points§6", "").replace("§f", ""));
                if (pts != HIDE.lastPts) {
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
