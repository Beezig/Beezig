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
import eu.beezig.core.autovote.AutovoteUtils;
import eu.beezig.core.command.PBCommand;
import eu.beezig.core.command.WRCommand;
import eu.beezig.core.games.DR;
import eu.beezig.core.games.TIMV;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.dr.DRRank;
import eu.beezig.core.hiveapi.stuff.dr.TotalPB;
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
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;
import pw.roccodev.beezig.hiveapi.wrapper.speedrun.WorldRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DRListener extends AbstractGameListener<DR> {

    @Override
    public Class<DR> getGameMode() {
        // TODO Auto-generated method stub
        return DR.class;
    }

    @Override
    public boolean matchLobby(String lobby) {
        return lobby.equals("DR");
    }

    @Override
    public void onGameModeJoin(DR gameMode) {
        gameMode.setState(GameState.STARTING);
        ActiveGame.set("DR");
        IHive.genericJoin();
        SendTutorial.send("dr_join");
        new Thread(() -> {
            try {
                gameMode.initWriter();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
            DrStats api = new DrStats(The5zigAPI.getAPI().getGameProfile()
                    .getId().toString().replace("-", ""));
            gameMode.rankObject = DRRank.getFromDisplay(api.getTitle());
            gameMode.rank = gameMode.rankObject.getTotalDisplay();
            // Should've read the docs ¯\_(ツ)_/¯
            if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                int points = ScoreboardUtils.getValue(sb, "Points");
                APIValues.DRpoints = points;

            }

            try {
                if (gameMode.attemptNew) {
                    gameMode.monthly = api.getMonthlyProfile();
                    gameMode.monthly.getPoints(); // Fetch (LazyObject)
                    gameMode.hasLoaded = true;
                }
            } catch (Exception e) {
                gameMode.attemptNew = false;
            }
        }).start();

    }

    @Override
    public boolean onServerChat(DR gameMode, String message) {
        if (BeezigMain.isColorDebug) {
            The5zigAPI.getLogger().info("ColorDebug: " + "(" + message + ")");
        }
        if (message.startsWith("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map") && gameMode != null) {
            String afterMsg = message.split("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map")[1];
            The5zigAPI.getLogger().info(afterMsg);
            String map = "";

            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }
            gameMode.activeMap = DR.mapsPool.get(map.toLowerCase().trim());

        } else if (message.contains("§lYou are a ") && gameMode != null) {
            String afterMsg = message.split(ChatColor.stripColor("You are a "))[1];
            switch (afterMsg) {
                case "DEATH!":
                    gameMode.role = "Death";
                    break;
                case "RUNNER!":
                    gameMode.role = "Runner";
                    new Thread(() -> {
                        if (gameMode.activeMap != null) {
                            The5zigAPI.getLogger().info("Loading PB...");

                            DrStats api = new DrStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));

                            try {
                                gameMode.currentMapPB = PBCommand.parseTime(api.getMapRecords().get(gameMode.activeMap.getHiveAPIName()));
                            } catch (Exception e) {
                                gameMode.currentMapPB = "No Personal Best";
                            }
                            The5zigAPI.getLogger().info("Loading WR...");

                            WorldRecord wr = DrStats.getWorldRecord(gameMode.activeMap.getSpeedrunID());

                            gameMode.currentMapWR = WRCommand.getWorldRecord(wr.getTime());
                            gameMode.currentMapWRHolder = wr.getHolderName();
                            if (gameMode.currentMapWR == null)
                                gameMode.currentMapWR = "No Record";
                            if (gameMode.currentMapWRHolder == null)
                                gameMode.currentMapWRHolder = "Unknown";
                        }
                    }).start();

                    break;
            }
            DiscordUtils.updatePresence("Parkouring in DeathRun", (gameMode.role.equals("Runner") ? "Running" : "Killing") + " on " + gameMode.activeMap.getDisplayName(), "game_dr");
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §aCheckpoint Reached! §7") && ActiveGame.is("dr")
                && gameMode.role.equals("Runner")) {
            // No more double tokens weekends Niklas :>)
            if (!(gameMode.checkpoints == gameMode.activeMap.getCheckpoints())) {
                gameMode.checkpoints++;

            }

            String data[] = ChatColor.stripColor(message).trim().split("\\+");
            int tokens = Integer.parseInt(data[1].trim().replaceAll("Tokens", "").trim());
            APIValues.tokens += tokens;
        } else if (message.equals("§8▍ §cDeathRun§8 ▏ §cYou have been returned to your last checkpoint!")
                && ActiveGame.is("dr") && gameMode.role.equals("Runner")) {
            gameMode.deaths++;
        } else if (message.contains("§6 (") && message.contains("§6)")
                && message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && ActiveGame.is("dr")
                && gameMode.role.equals("Death")) {
            gameMode.kills++;
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §a§lVote received.") && Setting.AUTOVOTE.getValue()) {
            gameMode.hasVoted = true;
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §7§l6. §6§cRandom Map") && !gameMode.hasVoted
                && Setting.AUTOVOTE.getValue()) {
            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(gameMode.votesToParse);
                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("dr"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();

                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {
                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §cDeathRun§8 ▏ §6§e§e§l", "")
                            .replaceAll("▍ DeathRun ▏", "").trim();
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
                    The5zigAPI.getAPI().messagePlayer("§8▍ §cDeathRun§8 ▏ §eAutomatically voted for §cRandom map");
                } else {
                    System.out.println(votesindex.firstEntry().getKey());
                    The5zigAPI.getAPI().sendPlayerMessage("/v " + votesindex.firstEntry().getValue());
                    The5zigAPI.getAPI().messagePlayer("§8▍ §cDeathRun§8 ▏ " + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());
                }
                gameMode.votesToParse.clear();
                gameMode.hasVoted = true;


            }).start();
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §7§l") && !gameMode.hasVoted && Setting.AUTOVOTE.getValue()) {
            gameMode.votesToParse.add(message);
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            // " §6§m §f ItsNiklass's Stats §6§m "
            // Advanced Records
            gameMode.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            gameMode.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            // TODO Coloring
            gameMode.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            gameMode.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (gameMode.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {
                        DRRank rank = null;
                        DrStats api = new DrStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();

                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue()
                                ? parent.getRank().getHumanName()
                                : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
                            rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                        }
                        long points = 0;
                        int kills = 0;
                        int deaths = 0;
                        int played = 0;
                        int victories = 0;
                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;

                        String rankTitleDR = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;


                        int monthlyRank = -1;
                        if (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()) {
                            try {
                                monthlyRank = (int) api.getMonthlyProfile().getPlace();
                            } catch (Exception ignored) {
                            }
                        }

                        if (rankTitleDR != null)
                            rank = DRRank.getFromDisplay(rankTitleDR);
                        List<String> messages = new ArrayList<>(gameMode.messagesToSend);
                        Iterator<String> it = messages.iterator();
                        for (String s : messages) {

                            if (s.trim().endsWith("'s Stats §6§m")) {
                                // " §6§m §f ItsNiklass's Stats §6§m "
                                // "§6§m §f ItsNiklass's Stats §6§m"
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
                                The5zigAPI.getAPI().messagePlayer("§f " + sb.toString().trim());
                                continue;

                            } else if (s.startsWith("§3 Kills: §b")) kills = Math.toIntExact(currentValue);
                            else if (s.startsWith("§3 Deaths: §b")) deaths = Math.toIntExact(currentValue);
                            else if (s.startsWith("§3 Victories: §b")) victories = Math.toIntExact(currentValue);
                            else if (s.startsWith("§3 Games Played: §b")) played = Math.toIntExact(currentValue);

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }


                        double ppg = Setting.SHOW_RECORDS_PPG.getValue() ? Math.round(((double) points / (double) played) * 10d) / 10d : -1;

                        int ach = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getUnlockedAchievements().size() : -1;

                        double rwr = Setting.SHOW_RECORDS_WINRATE.getValue() ? (Math
                                .floor(((double) victories / (double) played)
                                        * 1000d)
                                / 10d) : -1;

                        double dpg = Setting.SHOW_RECORDS_DPG.getValue() ? Math.floor((double) deaths / (double) played * 10d)
                                / 10d : -1;

                        double kpg = Setting.SHOW_RECORDS_KPG.getValue() ? Math.round((double) kills / (double) played * 10d) / 10d : -1;


                        String tpb = Setting.DR_SHOW_TOTALPB.getValue() ? TotalPB.getTotalPB(api.getMapRecords()) : null;


                        if (ppg != -1)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points per Game: §b" + ppg);

                        if (ach != -1)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Achievements: §b" + ach + "/68");

                        if (rwr != -1)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Winrate: §b" + rwr + "%");

                        if (dpg != -1)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Deaths per Game: §b" + dpg);

                        if (kpg != -1)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Kills per Game: §b" + kpg);

                        if (tpb != null)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Total Personal Best: §b" + tpb);

                        if (monthlyRank != -1)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Place: §b#" + monthlyRank);

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());

                            The5zigAPI.getAPI().messagePlayer(
                                    "§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : gameMode.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }

                        gameMode.messagesToSend.clear();
                        gameMode.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            gameMode.messagesToSend.clear();
                            gameMode.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : gameMode.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : gameMode.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§f " + "                      §6§m                  §6§m                  ");
                        gameMode.messagesToSend.clear();
                        gameMode.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;

            }

        } else if (message.contains("§lYou are a ")) {
            gameMode.setState(GameState.GAME);
        } else if (message.equals("§8▍ §cDeathRun§8 ▏ §6The round has started!")) {
            Timer timer = new Timer();
            ScoreboardFetcherTask sft = new ScoreboardFetcherTask();
            timer.schedule(sft, 1500);
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §3NEW PERSONAL BEST!§b") && message.contains("it took you")) {
            gameMode.mapTime = message.split("it took you §e")[1].replace("§b!", "");
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §bYou finished your run in ")) {
            // §8▍ §cDeathRun§8 ▏ §bYou finished your run in 03:07.479§b!
            String time = (message.split("in "))[1].replace("§b!", "").trim();

            gameMode.mapTime = time;

            String[] data = time.split(":");
            int minutes = Integer.parseInt(data[0]);
            // data[1 ] is seconds.milliseconds
            double secondsMillis = Double.parseDouble(data[1]);
            double finalTime = 60 * minutes + secondsMillis; // e.g, You finished in 01:51.321 = 01*60 + 51.321 =
            // 111.321

            new Thread(() -> {

                double wr = DrStats.getWorldRecord(gameMode.activeMap.getSpeedrunID()).getTime();
                double diff = (Math.round((finalTime - wr) * 1000d)) / 1000d;
                int finalPb;

                String pb = gameMode.currentMapPB;
                String[] pbData = pb.split(":");
                try {
                    finalPb = Integer.parseInt(pbData[0]) * 60 + Integer.parseInt(pbData[1]);
                } catch (Exception e) {
                    finalPb = -1;
                }

                int pbDiff = ((int) Math.floor(finalTime)) - finalPb;

                if (diff == 0) {
                    // Lets make this more important lmao
                    // All this has a reason, I may overdid it but I learned smth about tags
                    The5zigAPI.getAPI().messagePlayer(
                            "§c    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer(
                            Log.info + "   §c§lCongratulations! You §4§ltied §c§lthe §4§lWorld Record§c§l!");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer(
                            "§c    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer(message);
                } else if (diff < 0) {
                    The5zigAPI.getAPI().messagePlayer(
                            "§c    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI()
                            .messagePlayer(Log.info + "   §c§lCongratulations! §4§lYou beat the World Record!!!");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer(
                            "§c    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer(message);
                } else if (finalPb == -1) {
                    The5zigAPI.getAPI()
                            .messagePlayer(message + " §3The World Record is §b" + diff + "§3 seconds away!");
                } else if (pbDiff > 0) {
                    The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff
                            + "§3 seconds away! Your Personal Best is §b" + pbDiff + " §3seconds away!");
                } else if (pbDiff == 0) {
                    The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff
                            + "§3 seconds away! You tied your Personal Best!");
                } else {
                    The5zigAPI.getAPI().messagePlayer(message + " §3The World Record is §b" + diff
                            + "§3 seconds away! You beat your Personal Best by §b" + -pbDiff + " §3seconds!");
                }

                gameMode.mapTime = time + " (WR: " + (diff >= 0 ? "+" + diff : diff) + " PB: " + (pbDiff >= 0 ? "+" + pbDiff
                        : pbDiff) + ")";
            }).start();

            return true;
        }
        return false;
    }

    @Override
    public void onServerConnect(DR gameMode) {
        The5zigAPI.getLogger().info("Resetting! (DR)");
        gameMode.reset();
    }

    @Override
    public void onTick(DR gameMode) {
        int i = 5;
        if (The5zigAPI.getAPI().getSideScoreboard() == null) return;
        HashMap<String, Integer> lines = The5zigAPI.getAPI().getSideScoreboard().getLines();
        for (Map.Entry<String, Integer> e : lines.entrySet()) {
            if (e.getValue() == i && e.getKey().contains("§7Points: ")) {
                int pts = Integer.parseInt(ChatColor.stripColor(e.getKey().replace("§7Points: ", "").replace("§9", "").replaceAll("§f", "")));
                if (pts != gameMode.lastPts) {
                    gameMode.dailyPoints += (pts - gameMode.lastPts);
                    APIValues.DRpoints += (pts - gameMode.lastPts);
                    gameMode.lastPts = pts;
                }
            } else if (e.getKey().contains("N/A / §7Kills: ")) {
                gameMode.mapTime = e.getKey();
            }
            if (gameMode.gameId == null && e.getKey().contains("§7GID: §f")) {
                gameMode.gameId = e.getKey().replace("§7GID: §f", "").trim();
                Log.addToSendQueue(Log.info + "Game ID: §b" + gameMode.gameId + " > §a" + "http://hivemc.com/deathrun/game/" + gameMode.gameId);

            }
        }
    }

    private class ScoreboardFetcherTask extends TimerTask {

        @Override
        public void run() {
            for (Map.Entry<String, Integer> e : The5zigAPI.getAPI().getSideScoreboard().getLines().entrySet()) {
                if (e.getValue() == 3) {
                    TIMV.gameID = ChatColor.stripColor(e.getKey().trim());
                }
            }

        }

    }
}
