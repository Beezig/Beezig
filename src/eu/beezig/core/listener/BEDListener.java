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
import eu.beezig.core.games.BED;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
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
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bed.BedMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.BedStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BEDListener extends AbstractGameListener<BED> {

    @Override
    public Class<BED> getGameMode() {
        // TODO Auto-generated method stub
        return BED.class;
    }


    @Override
    public boolean matchLobby(String arg0) {
        if (arg0.contains("BED_")) {
            BED.activeMap = arg0.split("_")[1];
        }
        return arg0.startsWith("BED");
    }

    @Override
    public void onGameModeJoin(BED gameMode) {
        System.out.println("Join");
        gameMode.setState(GameState.STARTING);
        ActiveGame.set("BED");
        IHive.genericJoin();
        SendTutorial.send("bed_join");

        new Thread(() -> {
            try {
                try {
                    BED.initDailyPointsWriter();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                Thread.sleep(100);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();

                BedStats api = null;

                if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                    int points = ScoreboardUtils.getValue(sb, "Points");
                    BED.apiKills = ScoreboardUtils.getValue(sb, "Kills");
                    BED.apiDeaths = ScoreboardUtils.getValue(sb, "Deaths");
                } else {
                    String ign2 = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
                    api = new BedStats(ign2);
                    BED.apiDeaths = Math.toIntExact(api.getDeaths());
                    BED.apiKills = Math.toIntExact(api.getKills());
                }
                BED.updateMode();

                String ign1 = The5zigAPI.getAPI().getGameProfile().getName();
                if (api == null) api = new BedStats(ign1);
                APIValues.BEDpoints = api.getPoints();
                BED.updateRank();
                BED.updateKdr();

                try {
                    if (BED.attemptNew) {
                        BED.monthly = api.getMonthlyProfile();
                        BED.monthly.getPoints(); // Fetch (LazyObject)
                        BED.hasLoaded = true;
                    }
                } catch (Exception e) {
                    BED.attemptNew = false;
                }

                //Should've read the docs ¯\_(ツ)_/¯
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public boolean onServerChat(BED gameMode, String message) {

        //§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map §fEthereal§b has won!
        if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map")) {
            The5zigAPI.getLogger().info("Voting ended, parsing map");
            String afterMsg = message.split("§8▍ §3§lBed§b§lWars§8 ▏ §3Voting has ended! §bThe map")[1];
            BED.updateMode();
            String map = "";
            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }

            BED.activeMap = map;


            DiscordUtils.updatePresence("Housekeeping in BedWars: " + BED.mode, "Playing on " + BED.activeMap, "game_bedwars");
        } else if (message.equals("                     §6§lWelcome to Hive BedWars!")) { // Not sure about this one
            BED.inGame = true;
            The5zigAPI.getAPI().sendPlayerMessage("/gameid");
        } else if (message.equals("                        §6§lBedWars§7 - §a§lDouble Fun")) {
            BED.inGame = true;
            BED.mode = "Double Fun";

        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §2✚")) {
            int pts = Integer.parseInt(message.split("§a")[1].split(" Points")[0]);
            BED.pointsCounter += pts;
            APIValues.BEDpoints += pts;
            BED.dailyPoints += pts;

            if (message.endsWith("Generator]")) return false;

            switch (pts) {

                case 5:
                case 10:
                    BED.kills++;
                    BED.updateKdr();
                    break;
                case 50:
                case 65:
                case 80:
                    BED.bedsDestroyed++;
                    break;

            }
        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §7Gained no points")) {
            BED.kills++;
            BED.updateKdr();
        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §e✯ §6Notable Win! §eGold Medal Awarded!")) {

            BED.pointsCounter += 100;
            BED.dailyPoints += 100;
            APIValues.BEDpoints += 100;
            APIValues.medals++;
            APIValues.tokens += 100;
            BED.hasWon = true;
            BED.winstreak++;
            if (BED.winstreak >= BED.bestStreak)
                BED.bestStreak = BED.winstreak;
            StreakUtils.incrementWinstreakByOne("bed");

        } else if (message.contains("§c has been ELIMINATED!")) {
            BED.updateTeamsLeft();
        } else if (message.contains("§c bed has been DESTROYED!")) {
            BED.updateTeamsLeft();
        }

        //Advanced Rec  ords

        else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            BED.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            BED.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            BED.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ") && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            BED.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (BED.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                //Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        BedStats api = new BedStats(AdvancedRecords.player, true);
                        HivePlayer global = api.getPlayer();

                        int kills = 0;
                        int deaths = 0;
                        int gamesPlayed = 0;
                        int victories = 0;
                        int bedsDestroyed = 0;
                        int eliminations = 0;


                        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                        DecimalFormat df = (DecimalFormat) nf;
                        df.setMaximumFractionDigits(2);
                        df.setMinimumFractionDigits(2);

                        DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                        df1f.setMaximumFractionDigits(1);
                        df1f.setMinimumFractionDigits(1);


                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? global.getRank().getHumanName() : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {

                            rankColor = NetworkRank.fromDisplay(global.getRank().getHumanName()).getColor();

                        }
                        long points;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getUnlockedAchievements().size() : null;
                        Long streak = Setting.BED_SHOW_STREAK.getValue() ? api.getWinstreak() : null;


                        long monthlyRank = 0;
                        if (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()) {
                            try {
                                BedMonthlyProfile monthly = api.getMonthlyProfile();
                                if (monthly != null) {
                                    monthlyRank = monthly.getPlace();
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        List<String> messages = new ArrayList<>(BED.messagesToSend);
                        for (String s : messages) {


                            if (s.trim().endsWith("'s Stats §6§m")) {
                                The5zigAPI.getLogger().info("Editing Header...");
                                StringBuilder sb = new StringBuilder();
                                String correctUser = global.getUsername();
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
                                BED.lastRecordsPoints = points;
                                sb.append(newData[1]);
                                if (Setting.SHOW_RECORDS_RANK.getValue()) {
                                    BEDRank rank = BEDRank.newIsNo1(api) ? BEDRank.ZZZZZZ : BEDRank.getRank((int) points);
                                    if (rank != null) {
                                        int level = rank.getLevel((int) points);
                                        String BEDrankColor = rank.getName().replaceAll(ChatColor.stripColor(rank.getName()), "");
                                        String rankString = BED.NUMBERS[level] + " " + rank.getName();
                                        sb.append(" (").append(BEDrankColor).append(rankString.trim());
                                        if (Setting.SHOW_RECORDS_POINTSTONEXTRANK.getValue()) {
                                            sb.append(" / ").append(rank.getPointsToNextRank((int) points));
                                        }
                                        sb.append("§b)");
                                    }


                                }

                                //if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

                                The5zigAPI.getAPI().messagePlayer("§f " + sb.toString().trim());
                                continue;

                            } else if (s.startsWith("§3 Kills: §b")) {
                                kills = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Deaths: §b")) {
                                deaths = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Games Played: §b")) {
                                gamesPlayed = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Victories: §b")) {
                                victories = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Beds Destroyed: §b")) {
                                bedsDestroyed = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Team Eliminated: §b")) {
                                eliminations = Math.toIntExact(currentValue);
                            }

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }


                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Achievements: §b" + achievements + "/67");
                        }
                        // "§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained 10§a points for killing"

                        if (Setting.BED_SHOW_ELIMINATIONS_PER_GAME.getValue()) {
                            double epg = eliminations / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§f §3 Eliminations per Game: §b" + df1f.format(epg));
                        }
                        if (Setting.BED_SHOW_BEDS_PER_GAME.getValue()) {
                            double bpg = bedsDestroyed / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§f §3 Beds per Game: §b" + df1f.format(bpg));
                        }
                        if (Setting.SHOW_RECORDS_DPG.getValue()) {
                            double dpg = deaths / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§f §3 Deaths per Game: §b" + df1f.format(dpg));
                        }
                        if (Setting.SHOW_RECORDS_KPG.getValue()) {
                            double kpg = kills / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§f §3 Kills per Game: §b" + df1f.format(kpg));
                        }
                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = BED.lastRecordsPoints / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points per Game: §b" + df1f.format(ppg));
                        }
                        if (Setting.SHOW_RECORDS_KDR.getValue()) {
                            double kdr = kills / (double) (deaths == 0 ? 1 : deaths);
                            The5zigAPI.getAPI().messagePlayer("§f §3 K/D: §b" + df.format(kdr));
                        }
                        if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                            double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Winrate: §b" + df1f.format(wr) + "%");
                        }
                        if (monthlyRank != 0) {

                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Place: §b#" + monthlyRank);
                        }


                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());

                            The5zigAPI.getAPI().messagePlayer("§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }


                        for (String s : BED.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }


                        BED.messagesToSend.clear();
                        BED.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            BED.messagesToSend.clear();
                            BED.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : BED.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : BED.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer("§f " + "                      §6§m                  §6§m                  ");
                        BED.messagesToSend.clear();
                        BED.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;


            }

        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §a§lVote received.") && Setting.AUTOVOTE.getValue()) {
            BED.updateMode();
            BED.hasVoted = true;
        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §7§l5.") && !BED.hasVoted && Setting.AUTOVOTE.getValue()) {
            //Adding the 6th option, the normal method doesn't work
            BED.votesToParse.add(message);
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (Exception ignored) {
                }

                List<String> votesCopy = new ArrayList<>(BED.votesToParse);

                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("bed"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();
                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {
                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §3§lBed§b§lWars§8 ▏ §7§l", "").replaceAll("▍ BedWars ▏", "").trim();
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
                    The5zigAPI.getAPI().messagePlayer("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ " + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());

                } else if (Setting.AUTOVOTE_RANDOM.getValue()) {
                    The5zigAPI.getAPI().sendPlayerMessage("/v 6");
                    The5zigAPI.getAPI().messagePlayer("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ " + "§eAutomatically voted for§c Random map§e.");
                }
                BED.votesToParse.clear();
                BED.hasVoted = true;

            }).start();
        } else if (message.equals("                 §e§lGold Ingot Summoner Activated!")) {
            BED.goldGen = 1;
        } else if (message.equals("                   §b§lDiamond Summoner Activated!")) {
            BED.diamondGen = 1;
        } else if (message.equals("                 §e§lGold Ingot Summoner Upgraded!")) {
            BED.goldGen++;
        } else if (message.equals("                 §f§lIron Ingot Summoner Upgraded!")) {
            BED.ironGen++;
        } else if (message.equals("                   §b§lDiamond Summoner Upgraded!")) {
            BED.diamondGen++;
        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §7§l") && !BED.hasVoted && Setting.AUTOVOTE.getValue()) {
            BED.votesToParse.add(message);
        } else if (message.contains("§aYou levelled up to")) {
            //Update the rank module when you uprank
            new Thread(() -> {
                try {
                    String ign = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
                    APIValues.BEDpoints = new BedStats(ign).getPoints();
                    Thread.sleep(200);
                    BED.updateRank();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } else if (message.trim().equals("§d§lNew Rank!")) {
            new Thread(() -> {

                try {
                    String ign = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
                    APIValues.BEDpoints = new BedStats(ign).getPoints();
                    Thread.sleep(200);
                    BED.updateRank();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §bYou can find all §emessages and game events §bat §a")) {
            BED.gameId = message.replace("§8▍ §3§lBed§b§lWars§8 ▏ §bYou can find all §emessages and game events §bat §ahttps://hivemc.com/bedwars/game/", "");
        }
        return false;

    }

    @Override
    public void onTitle(BED gameMode, String title, String subTitle) {

        if (subTitle != null && ChatColor.stripColor(subTitle).trim().equals("Respawning in 2 seconds")) {
            BED.deaths++;
            BED.updateKdr();
        } else if (subTitle != null && subTitle.equals("§r§7Protect your bed, destroy others!§r")) {
            gameMode.setState(GameState.GAME);
            BED.ironGen = 1;
            //As Hive sends this subtitle like 13 times, don't do anything here please :) mhm
        } else if (title != null && title.equals("§r§c§lFIGHT!§r")) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(4);
                    BED.updateTeamsLeft();
                } catch (Exception ignored) {
                }
            }).start();
        }
    }

    @Override
    public void onServerConnect(BED gameMode) {
        BED.reset(gameMode);
    }


}
