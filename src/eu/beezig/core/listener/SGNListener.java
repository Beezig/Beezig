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
import eu.beezig.core.games.SGN;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.sgn.SGNRank;
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
import pw.roccodev.beezig.hiveapi.wrapper.player.games.SgnStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SGNListener extends AbstractGameListener<SGN> {

    private boolean duos;

    @Override
    public Class<SGN> getGameMode() {
        return SGN.class;
    }

    @Override
    public boolean matchLobby(String arg0) {
        if("SGN2".equals(arg0)) {
            duos = true;
            return true;
        }
        duos = false;
        return "SGN".equals(arg0);
    }

    @Override
    public void onGameModeJoin(SGN gameMode) {

        gameMode.setState(GameState.STARTING);
        ActiveGame.set("SGN");
        IHive.genericJoin();
        SendTutorial.send("sgn_join");

        new Thread(() -> {
            try {
                try {
                    SGN.initDailyPointsWriter();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                Thread.sleep(500);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();


                SgnStats api = new SgnStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));

                if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                    int points = ScoreboardUtils.getValue(sb, "Points");
                    APIValues.SGNpoints = (long) points;

                    SGN.rankObject = SGNRank.getRank(api.getPoints());
                    SGN.rank = SGN.rankObject.getTotalDisplay();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public boolean onServerChat(SGN gameMode, String message) {
        // (§8▍ §e§e§lHive§3§l§3§lSG§b§l§b§l2§8§l ▏ §6§l§e§l§e§l1. §f§6SG3: Mini
        // §a[§f0§a Votes])
        String prefix = duos ? "§8▍ §e§lHive§3§lSG §b§lDuos§8 ▏ " : "§8▍ §e§lHive§3§lSG§8 ▏ ";

        if (message.startsWith(prefix + "§3Voting has ended! §bThe map §f")) {
            The5zigAPI.getLogger().info("Voting ended, parsing map");
            String afterMsg = message.split(prefix + "§3Voting has ended! §bThe map")[1];
            String map = "";
            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }

            SGN.activeMap = map;

            DiscordUtils.updatePresence("Battling in SG2", "Playing on " + SGN.activeMap, "game_sgn");
        } else if (message.startsWith(prefix + "§a§lVote received.")
                && Setting.AUTOVOTE.getValue()) {
            SGN.hasVoted = true;
        } else if (message.startsWith(prefix + "§7§l6. §6§cRandom Map") && !SGN.hasVoted
                && Setting.AUTOVOTE.getValue()) {
            /*
             *
             * Multi-threading to avoid lag on older machines
             *
             */

            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(SGN.votesToParse);

                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("sgn"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();
                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {
                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0])
                            .replaceAll(prefix + "§7§l", "")
                            .replaceAll(ChatColor.stripColor(prefix + "§7§l"),
                                    "")
                            .trim();
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
                    The5zigAPI.getAPI().messagePlayer("§8▍ §e§e§lHive§3§l§3§lSG§b§l§b§l2§8§l ▏ "
                            + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());
                }
                else if(Setting.AUTOVOTE_RANDOM.getValue()) {
                    The5zigAPI.getAPI().sendPlayerMessage("/v 6");
                    The5zigAPI.getAPI().messagePlayer("§8▍ §e§e§lHive§3§l§3§lSG§b§l§b§l2§8§l ▏ "
                            + "§eAutomatically voted for Random Map.");
                }
                SGN.votesToParse.clear();
                SGN.hasVoted = true;
            }).start();
        } else if (message.startsWith(prefix + "§7§l") && !SGN.hasVoted
                && Setting.AUTOVOTE.getValue()) {
            SGN.votesToParse.add(message);
        } else if (message.startsWith(prefix + "§6Global Points Lost: §e")) {
            int pts = Integer.parseInt(message.replace(prefix + "§6Global Points Lost: §e", ""));
            if (!SGN.custom)
                APIValues.SGNpoints -= pts;
            SGN.gamePts -= pts;
            if (!SGN.custom)
                SGN.dailyPoints -= pts;
        } else if (message.endsWith("§3§lPoints gained.")) {
            int pts = Integer.parseInt(message.replace("§3§lPoints gained.", "").replace(prefix + "§b§l", "").trim());
            if (!SGN.custom)
                APIValues.SGNpoints += pts;
            SGN.gamePts += pts;
            if (!SGN.custom)
                SGN.dailyPoints += pts;
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            SGN.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            SGN.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            SGN.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if (message.equals("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚§b§l 1 point")) {
            APIValues.SGNpoints++;
            SGN.gamePts++;
            SGN.dailyPoints++;
        } else if (message.equals("§8▍ §d§lDeathmatch§8 ▏ §c§lDeathmatch in §e§l1.")) {
            DiscordUtils.updatePresence("Battling in SG2", "Deathmatch" + (SGN.custom ? " [CS]" : ""), "game_sgn");
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            SGN.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (SGN.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        SgnStats api = new SgnStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();
                        SGNRank rank = null;

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
                        String rankTitleSGN = Setting.SHOW_RECORDS_RANK.getValue() ? "" : null;
                        if (rankTitleSGN != null)
                            rank = SGNRank.getRank(api.getPoints());

                        int kills = 0;
                        long points = 0;
                        int deaths = 0;
                        int gamesPlayed = 0;
                        int victories = 0;

                        long realKills = 0;
                        long realDeaths = 0;
                        long realVictories = 0;
                        long realPlayed = 0;
                        long realPoints = AdvancedRecords.player.equalsIgnoreCase(The5zigAPI.getAPI().getGameProfile().getName()) ? api.getPoints() : 0;


                        long timeAlive = 0;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;


                        List<String> messages = new ArrayList<>(SGN.messagesToSend);
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
                                if (realPoints != 0) {
                                    sb.append(" [").append(Log.df(realPoints)).append("]");
                                } else {
                                    realPoints = points;
                                }
                                if (rank != null)
                                    sb.append(" (").append(rank.getTotalDisplay());
                                if (Setting.SHOW_RECORDS_POINTSTONEXTRANK.getValue())
                                    sb.append(" / ").append(rank.getPointsToNextRank((int) realPoints));
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
                            if (Setting.SHOW_RECORDS_KDR.getValue()) {
                                if (AdvancedRecords.player.equalsIgnoreCase(The5zigAPI.getAPI().getGameProfile().getName())) {
                                    realKills = api.getKills();
                                    realDeaths = api.getDeaths();
                                } else {
                                    realKills = kills;
                                    realDeaths = deaths;
                                }
                            }
                            if (Setting.SHOW_RECORDS_PPG.getValue()) {
                                if (AdvancedRecords.player.equalsIgnoreCase(The5zigAPI.getAPI().getGameProfile().getName())) {
                                    realPoints = realPoints == 0 ? api.getPoints() : realPoints;
                                    realPlayed = api.getGamesPlayed();
                                } else {
                                    realPoints = points;
                                    realPlayed = gamesPlayed;
                                }
                            }
                            if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                                if (AdvancedRecords.player.equalsIgnoreCase(The5zigAPI.getAPI().getGameProfile().getName())) {
                                    realVictories = api.getVictories();
                                    realPlayed = realPlayed == 0 ? api.getGamesPlayed() : realPlayed;
                                } else {
                                    realVictories = victories;
                                    realPlayed = gamesPlayed;
                                }
                            }

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }

                        if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                            double wr = Math.floor(((double) realVictories / (double) realPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§f" + (realVictories == victories ? "§3 Winrate: §b" : "§3 Real Winrate: §b") + df1f.format(wr) + "%");
                        }
                        if (Setting.SHOW_RECORDS_KDR.getValue()) {
                            double kd = (double) realKills / (double) realDeaths;

                            The5zigAPI.getAPI().messagePlayer("§f" + (realKills == kills ? "§3 K/D: §b" : "§3 Real K/D: §b") + df.format(kd));
                        }
                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = (double) realPoints / (double) realPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f" + (realPoints == points ? "§3 Points Per Game: §b" : "§3 Real Points Per Game: §b") + df1f.format(ppg));
                        }

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer(
                                    "§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : SGN.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }

                        SGN.messagesToSend.clear();
                        SGN.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            SGN.messagesToSend.clear();
                            SGN.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : SGN.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : SGN.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§f " + "                      §6§m                  §6§m                  ");
                        SGN.messagesToSend.clear();
                        SGN.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;
            }
        }

        return false;

    }

    @Override
    public boolean onActionBar(SGN gameMode, String message) {
        if (message.contains("CS_") && !SGN.custom) {
            SGN.custom = true;
            DiscordUtils.updatePresence("Battling in SG2", "Playing on " + SGN.activeMap + " [CS]", "game_sgn");
        }
        return false;
    }

    @Override
    public void onServerConnect(SGN gameMode) {
        SGN.reset(gameMode);
    }

}
