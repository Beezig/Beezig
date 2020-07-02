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
import eu.beezig.core.games.BP;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.bp.BPRank;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.ScoreboardUtils;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.beezig.core.utils.ws.Connector;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONObject;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bp.BpMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.BpStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class BPListener extends AbstractGameListener<BP> {

    @Override
    public Class<BP> getGameMode() {
        return BP.class;
    }

    @Override
    public boolean matchLobby(String arg0) {
        return arg0.equals("BP");
    }

    @Override
    public void onGameModeJoin(BP gameMode) {

        gameMode.setState(GameState.STARTING);
        ActiveGame.set("BP");
        IHive.genericJoin();
        SendTutorial.send("bp_join");

        new Thread(() -> {
            try {
                try {
                    BP.initDailyPointsWriter();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                Thread.sleep(500);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();


                BpStats api = new BpStats(The5zigAPI.getAPI().getGameProfile().getName());

                if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {
                    int points = ScoreboardUtils.getValue(sb, "Points");
                    APIValues.BPpoints = (long) points;

                    BP.rankObject = BPRank.getFromDisplay(api.getTitle());
                    BP.rank = BP.rankObject.getTotalDisplay();

                }

                // Custom jukebox
                if (Setting.BP_JUKEBOX.getValue()) {
                    JSONObject obj = APIUtils.getObject(APIUtils.readURL(new URL("https://hivemc.com/ajax/getblockpartyserver/" + The5zigAPI.getAPI().getGameProfile().getName())));
                    String server = (String) obj.get("server"); // Either "NONE" or the server the player is in
                    if (server.equals("NONE")) {
                        System.out.println("No server, skipping.");
                        return;
                    }
                    Connector.connectBP(server);
                }

                try {
                    if (BP.attemptNew) {
                        BP.monthly = api.getMonthlyProfile();
                        BP.monthly.getPoints(); // Fetch (LazyObject)
                        BP.hasLoaded = true;
                    }
                } catch (Exception e) {
                    BP.attemptNew = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public boolean onServerChat(BP gameMode, String message) {

        if (message.startsWith("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §bCongrats! You earned §a")) {

            APIValues.BPpoints += 10;
            BP.gamePts += 10;
            BP.dailyPoints += 10;

        } else if (message.startsWith("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚ §b§l10 points")) {

            APIValues.BPpoints += 10;
            BP.gamePts += 10;
            BP.dailyPoints += 10;
        } else if (message.startsWith("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚ §b§l20 points")) {

            APIValues.BPpoints += 20;
            BP.gamePts += 20;
            BP.dailyPoints += 20;
        } else if (message.contains("§b§l") && message.startsWith("   ") && BP.song == null && !message.contains("hivemc.com")) {
            BP.song = ChatColor.stripColor(message).trim();
            gameMode.setState(GameState.GAME);
            DiscordUtils.updatePresence("Dancing in BlockParty", "In rhythm with \"" + BP.song + "\"", "game_bp");
        } else if (message.contains(" §7") && message.startsWith("   ") && !message.startsWith("    §7§m")) {
            BP.artist = ChatColor.stripColor(message).trim();
        } else if (message.startsWith("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚ §b§l5 points")) {
            APIValues.BPpoints += 5;
            BP.gamePts += 5;
            BP.dailyPoints += 5;
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            BP.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            BP.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            BP.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if (message.equals("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚ §b§l1 point")) {
            APIValues.BPpoints++;
            BP.gamePts++;
            BP.dailyPoints++;
        } else if (message.equals("        §a§m                      §f§l NOW PLAYING §a§m                      ")) {
            gameMode.setState(GameState.GAME);
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            BP.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (BP.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        BpStats api = new BpStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();
                        BPRank rank = null;

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
                        String rankTitleBP = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        if (rankTitleBP != null)
                            rank = BPRank.getFromDisplay(rankTitleBP);

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
                                BpMonthlyProfile monthly = api.getMonthlyProfile();
                                if (monthly != null) {
                                    monthlyRank = monthly.getPlace();
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        List<String> messages = new ArrayList<>(BP.messagesToSend);
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

                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Achievements: §b" + achievements + "/27");
                        }

                        if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                            double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Winrate: §b" + df1f.format(wr) + "%");
                        }
                        if (monthlyRank != 0) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Place: §b#" + monthlyRank);
                        }
                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = (double) points / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points Per Game: §b" + df1f.format(ppg));
                        }

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer(
                                    "§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : BP.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }

                        BP.messagesToSend.clear();
                        BP.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            BP.messagesToSend.clear();
                            BP.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : BP.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : BP.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§f " + "                      §6§m                  §6§m                  ");
                        BP.messagesToSend.clear();
                        BP.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;
            }
        }

        return false;

    }

    @Override
    public void onServerConnect(BP gameMode) {
        BP.reset(gameMode);
    }

}
