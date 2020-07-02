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
import eu.beezig.core.games.TIMV;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.timv.TIMVRank;
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
import pw.roccodev.beezig.hiveapi.wrapper.monthly.timv.TimvMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.TimvStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TIMVListener extends AbstractGameListener<TIMV> {


    @Override
    public Class<TIMV> getGameMode() {
        // TODO Auto-generated method stub
        return TIMV.class;
    }

    @Override
    public boolean matchLobby(String lobby) {

        return lobby.equals("TIMV");

    }


    @Override
    public void onGameModeJoin(TIMV gameMode) {
        gameMode.setState(GameState.STARTING);
        ActiveGame.set("TIMV");
        IHive.genericJoin();
        SendTutorial.send("timv_join");

        //Should've read the docs ¯\_(ツ)_/¯
        new Thread(() -> {
            try {
                TIMV.initDailyKarmaWriter();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();

            if (sb != null && sb.getTitle().trim().equalsIgnoreCase("§6§lHive§e§lMC")) {

                int karma = ScoreboardUtils.getValue(sb, "Karma");
                if (karma != 0)
                    APIValues.TIMVkarma = (long) karma;


            } else {


                try {
                    String ign = The5zigAPI.getAPI().getGameProfile().getName();
                    APIValues.TIMVkarma = new TimvStats(ign).getKarma();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

            TimvStats api = new TimvStats(The5zigAPI.getAPI().getGameProfile().getName());
            TIMV.rankObject = TIMVRank.getFromDisplay(api.getTitle());
            TIMV.rank = TIMV.rankObject.getTotalDisplay(api.getPoints());

            TIMV.calculatedBeforeRoles = false;
            //safety


            try {
                if (TIMV.attemptNew) {
                    TIMV.monthly = api.getMonthlyProfile();
                    TIMV.monthly.getPoints(); // Fetch (LazyObject)
                    TIMV.hasLoaded = true;
                }
            } catch (Exception e) {
                TIMV.attemptNew = false;
            }

        }).start();
    }


    @Override
    public boolean onServerChat(TIMV gameMode, String message) {
        // Uncomment this to see the real messages with chatcolor. vv
        // The5zigAPI.getLogger().info("(" + message + ")");
        if (BeezigMain.isColorDebug) {
            The5zigAPI.getLogger().info("ColorDebug: " + "(" + message + ")");
        }
        if (message.equals(TIMV.joinMessage)) {
            TIMV.reset(gameMode);
        }
        if (message.startsWith("§8▍ §6TIMV§8 ▏ §c§l- 20 Karma") && gameMode != null) {
            TIMV.minus20();
            if (TIMV.role.equals("Detective")) {
                TIMV.applyPoints(-1, "i");
            } else {
                TIMV.applyPoints(-1);
            }

        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §c§l- 40 Karma") && gameMode != null) {
            TIMV.minus40();
            if (TIMV.role.equals("Traitor")) {
                TIMV.applyPoints(-2, "t");
            } else {
                TIMV.applyPoints(-2, "d");
            }
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §a§l+ 20 Karma") && gameMode != null) {
            TIMV.plus20();
            if (TIMV.role.equals("Traitor")) {
                TIMV.applyPoints(2);
            } else {
                TIMV.applyPoints(1);
            }
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §a§l+ 10 Karma") && gameMode != null) {
            TIMV.plus10();
            TIMV.applyPoints(1);
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §a§l+ 25 Karma") && gameMode != null) {
            TIMV.plus25();
            TIMV.applyPoints(2); //+1 Det point

        } else if (message.startsWith("             §c§lGame. OVER!") && gameMode != null) {
            if (!TIMV.dead) {
                TIMV.applyPoints(20);
            }
            TIMV.traitorTeam.addAll(Collections.nCopies(7, "fin"));
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §3Voting has ended! §bThe map") && gameMode != null) {
            String afterMsg = message.split("§8▍ §6TIMV§8 ▏ §3Voting has ended! §bThe map")[1];
            // §bSky Lands§6
            //
            String map = "";

            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }
            The5zigAPI.getLogger().info(map);
            TIMV.mapStr = map;
            DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "Playing on " + map, "game_timv");

            TIMV.activeMap = TIMV.mapsPool.get(map.toLowerCase().trim());


        } else if (message.contains("is known to have poisonous water...") && gameMode != null && TIMV.activeMap == null) {
            //(         §eFrozen Cargo is known to have poisonous water...)
            String mapmsg = ChatColor.stripColor(message.split(" is known to have poisonous water...")[0]).trim().trim();
            String map = "";
            Pattern pattern = Pattern.compile(mapmsg);
            Matcher matcher = pattern.matcher(mapmsg);
            while (matcher.find()) {
                map = matcher.group(0);
            }
            The5zigAPI.getLogger().info("FALLBACK: " + map);

            TIMV.activeMap = TIMV.mapsPool.get(map.toLowerCase());
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §1You will be a detective.")) {
            TIMV.currentPassStatus = 2;
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §4You will be a traitor.")) {
            TIMV.currentPassStatus = 1;
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            //"          §6§m                  §f ItsNiklass's Stats §6§m                  "
            //Advanced Records
            TIMV.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && !message.endsWith(" ") && Setting.ADVANCED_RECORDS.getValue()) {

            TIMV.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            //TODO Coloring
            TIMV.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ") && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            TIMV.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (TIMV.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                //Send AdvRec
                new Thread(() -> {
                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {
                        The5zigAPI.getLogger().info(AdvancedRecords.player);
                        TimvStats api = new TimvStats(AdvancedRecords.player, true);
                        HivePlayer global = api.getPlayer();
                        TIMVRank rank = null;
                        Long rolepoints = Setting.TIMV_SHOW_KRR.getValue() ? api.getRolePoints() : null;
                        if (rolepoints == null && Setting.TIMV_SHOW_TRAITORRATIO.getValue()) {
                            rolepoints = api.getRolePoints();
                        }
                        Long mostPoints = Setting.TIMV_SHOW_MOSTPOINTS.getValue() ? api.getMostPoints() : null;
                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? global.getRank().getHumanName() : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
                            rankColor = NetworkRank.fromDisplay(global.getRank().getHumanName()).getColor();
                        }
                        long karma = 0;
                        long traitorPoints;
                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.getLastLogin() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getUnlockedAchievements().size() : null;
                        String rankTitleTIMV = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        int monthlyRank = 0;
                        try {
                            TimvMonthlyProfile monthlyProfile = api.getMonthlyProfile();
                            monthlyRank = Math.toIntExact((Setting.SHOW_RECORDS_MONTHLYRANK.getValue() && monthlyProfile != null) ? api.getMonthlyProfile().getPlace() : 0);
                        } catch (Exception ignored) {
                        }
                        if (rankTitleTIMV != null) rank = TIMVRank.getFromDisplay(rankTitleTIMV);
                        List<String> messages = new ArrayList<>(TIMV.messagesToSend);
                        Iterator<String> it = messages.iterator();
                        for (String s : messages) {


                            if (s.trim().endsWith("'s Stats §6§m")) {
                                //"          §6§m                  §f ItsNiklass's Stats §6§m                  "
                                //"§6§m                  §f ItsNiklass's Stats §6§m"
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

                            if (s.startsWith("§3 Karma: §b")) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("§3 Karma: §b");
                                karma = currentValue;
                                sb.append(newData[1]);
                                if (rank != null) sb.append(" (").append(rank.getTotalDisplay(karma));
                                if (Setting.SHOW_RECORDS_POINTSTONEXTRANK.getValue() && rank != null) {
                                    sb.append(" / ").append(rank.getKarmaToNextRank((int) karma));
                                }
                                sb.append("§b)");
                                The5zigAPI.getAPI().messagePlayer("§f " + sb.toString().trim() + " ");
                                continue;
                            } else if (s.startsWith("§3 Traitor Points: §b") && karma > 1000 && Setting.TIMV_SHOW_TRAITORRATIO.getValue()) {
                                traitorPoints = Math.toIntExact(currentValue);
                                long rp = rolepoints;
                                double tratio = Math.round(((double) traitorPoints / (double) rp) * 1000d) / 10d;
                                ChatColor ratioColor = ChatColor.AQUA;
                                if (tratio >= TIMV.TRATIO_LIMIT) {
                                    ratioColor = ChatColor.RED;
                                }
                                The5zigAPI.getAPI().messagePlayer("§f §3 Traitor Points: §b" + ChatColor.AQUA + newData[1] + " (" + ratioColor + tratio + "%" + ChatColor.AQUA + ") ");
                                continue;
                            }

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }


                        Double krr = Setting.TIMV_SHOW_KRR.getValue() ? (double) Math.round((double) karma / (double) rolepoints * 100D) / 100D : null;


                        if (mostPoints != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Most Points: §b" + mostPoints + " ");
                        }
                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Achievements: §b" + achievements + "/64 ");
                        }
                        if (krr != null) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Karma/Rolepoints: §b" + krr + " ");
                        }
                        if (monthlyRank > 0) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Leaderboards: §b#" + monthlyRank + " ");
                        }
                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());

                            The5zigAPI.getAPI().messagePlayer("§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()) + " ");
                        }


                        for (String s : TIMV.footerToSend) {

                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }


                        TIMV.messagesToSend.clear();
                        TIMV.footerToSend.clear();
                        AdvancedRecords.isRunning = false;


                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            TIMV.messagesToSend.clear();
                            TIMV.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : TIMV.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : TIMV.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer("§f " + "                      §6§m                  §6§m                  ");
                        TIMV.messagesToSend.clear();
                        TIMV.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }


                }, "TIMV Advanced Records Fetcher").start(); // Labeling threads
                return true;
            }
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §a§lVote received. §3Your map now has") && Setting.AUTOVOTE.getValue()) {
            TIMV.hasVoted = true;
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §7§l6. §6§cRandom Map") && !TIMV.hasVoted && Setting.AUTOVOTE.getValue()) {
            /*
             *
             * Multi-threading to avoid lag on older machines
             *
             */

            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(TIMV.votesToParse);


                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("timv"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();
                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {
                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §6TIMV§8 ▏ §6§6§l", "").replaceAll("▍ TIMV ▏", "").trim();
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
                    The5zigAPI.getAPI().messagePlayer("§8▍ §6TIMV§8 ▏ " + "§eAutomatically voted for §cRandom map");
                } else {
                    System.out.println(votesindex.firstEntry().getKey());
                    The5zigAPI.getAPI().sendPlayerMessage("/v " + votesindex.firstEntry().getValue());
                    The5zigAPI.getAPI().messagePlayer("§8▍ §6TIMV§8 ▏ " + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());
                }
                TIMV.votesToParse.clear();
                TIMV.hasVoted = true;
            }).start();
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §7§l") && !TIMV.hasVoted && Setting.AUTOVOTE.getValue()) {
            TIMV.votesToParse.add(message);
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §6The body of §4")) {
            TIMV.traitorsDiscovered++;
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §6The body of §1")) {
            TIMV.detectivesDiscovered++;
        } else if (message.startsWith("§8▍ §6TIMV§8 ▏ §c§lMatch Recap")) {
            TIMV.dead = true;

        } else if (message.contains("§f§lYOU ARE ")) {
            gameMode.setState(GameState.GAME);
            TIMV.calculateTraitors(The5zigAPI.getAPI().getServerPlayers().size());
            TIMV.calculateDetectives(The5zigAPI.getAPI().getServerPlayers().size());
            The5zigAPI.getLogger().info("(" + message + ")");

            TIMV.calculatedBeforeRoles = true;

            String role = "";
            if (message.contains("§2INNOCENT")) {
                role = "Innocent";
            } else if (message.contains("§4TRAITOR")) {
                role = "Traitor";
            } else if (message.contains("§1DETECTIVE")) { // Assumption
                role = "Detective";
            }
            TIMV.role = role;

            Timer timer = new Timer();
            ScoreboardFetcherTask sft = new ScoreboardFetcherTask();
            timer.schedule(sft, 1500);

        } else if (message.equals("                        §4§m                                ") && TIMV.traitorTeam.size() < 7 && TIMV.traitorTeam.size() > 0 && TIMV.role.equals("Traitor")) {


            new Thread(() -> {

                ArrayList<Long> TraitorKarma = new ArrayList<>();
                for (String name : TIMV.traitorTeam) {
                    try {
                        TimvStats api = new TimvStats(name);
                        TraitorKarma.add(api.getKarma());
                    } catch(Exception ignored) {}
                }
                long avg = (long) APIUtils.average(TraitorKarma.toArray());
                The5zigAPI.getAPI().messagePlayer("                        §c§m                                ");
                The5zigAPI.getAPI().messagePlayer("                       §4Traitor Karma: " + avg);
                The5zigAPI.getAPI().messagePlayer("                        §c§m                                ");

            }).start();
        }
        else if (message.contains("   §4") && TIMV.traitorTeam.size() < 7 && TIMV.role.equals("Traitor")) {
            //§4jordix03, ItsNiklass, Vpnce, BatHex
            //The5zigAPI.getLogger().info(ChatColor.stripColor(message).split(", "));
            TIMV.traitorTeam.addAll(Arrays.asList(ChatColor.stripColor(message).replaceAll(" ", "").split(",")));
            The5zigAPI.getLogger().info(TIMV.traitorTeam.toString());
        }

        return false;
    }

    @Override
    public void onServerConnect(TIMV gameMode) {
        TIMV.reset(gameMode);

    }


    @Override
    public void onServerDisconnect(TIMV gameMode) {
        TIMV.reset(gameMode);
    }


    @Override
    public boolean onActionBar(TIMV gameMode, String message) {
        if (!TIMV.actionBarChecked) {
            if (message != null && message.contains("▏ §7")) {
                String[] data = message.split("▏ §7");
                TIMV.gameID = data[1];
                TIMV.actionBarChecked = true;
                Log.addToSendQueue(Log.info + "Game ID: §b" + TIMV.gameID + " > §a"
                        + "http://hivemc.com/trouble-in-mineville/game/" + TIMV.gameID);
            }
        }
        return false;
    }


    private class ScoreboardFetcherTask extends TimerTask {

        @Override
        public void run() {


        }

    }


}

