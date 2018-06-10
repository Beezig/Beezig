package tk.roccodev.beezig.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BP;
import tk.roccodev.beezig.hiveapi.APIValues;
import tk.roccodev.beezig.hiveapi.stuff.bp.BPRank;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiBP;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
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
                The5zigAPI.getLogger().info(sb.getTitle());

                ApiBP api = new ApiBP(The5zigAPI.getAPI().getGameProfile().getName());

                if (sb != null && sb.getTitle().contains("Your BP Stats")) {
                    int points = sb.getLines().get(ChatColor.AQUA + "Points");
                    APIValues.BPpoints = (long) points;

                    BP.rankObject = BPRank.getFromDisplay(api.getTitle());
                    BP.rank = BP.rankObject.getTotalDisplay();

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

        } else if (message.startsWith("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚§b§l 10 points")) {

            APIValues.BPpoints += 10;
            BP.gamePts += 10;
            BP.dailyPoints += 10;
        } else if (message.startsWith("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚§b§l 20 points")) {

            APIValues.BPpoints += 20;
            BP.gamePts += 20;
            BP.dailyPoints += 20;
        } else if (message.contains("§b§l") && message.startsWith("   ") && BP.song == null && !message.contains("hivemc.com")) {
            BP.song = ChatColor.stripColor(message).trim();
            gameMode.setState(GameState.GAME);
            DiscordUtils.updatePresence("Dancing in BlockParty", "In rhythm with \"" + BP.song + "\"", "game_bp");
        } else if (message.contains(" §7") && message.startsWith("   ") && BP.artist == null && !message.startsWith("    §7§m")) {
            BP.artist = ChatColor.stripColor(message).trim();
        } else if (message.equals("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚§b§l 5 points§7")) {
            APIValues.BPpoints += 5;
            BP.gamePts += 5;
            BP.dailyPoints += 5;
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
            BP.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ")) {

            BP.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
            BP.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if (message.equals("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏ §a✚§b§l 1 point")) {
            APIValues.BPpoints++;
            BP.gamePts++;
            BP.dailyPoints++;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§o "))) {
            The5zigAPI.getLogger().info("found footer");
            BP.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (BP.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    BP.isRecordsRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        ApiBP api = new ApiBP(BP.lastRecords);
                        BPRank rank = null;

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
                        String rankTitleBP = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        if (rankTitleBP != null)
                            rank = BPRank.getFromDisplay(rankTitleBP);

                        int kills = 0;
                        long points = 0;
                        int deaths = 0;
                        int gamesPlayed = 0;
                        int victories = 0;

                        long timeAlive = 0;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements()
                                : null;

                        // int monthlyRank = (Setting.DR_SHOW_MONTHLYRANK.getValue() &&
                        // HiveAPI.getLeaderboardsPlacePoints(349, "BP") <
                        // HiveAPI.DRgetPoints(BP.lastRecords)) ?
                        // HiveAPI.getMonthlyLeaderboardsRank(DR.lastRecords, "DR") : 0;

                        List<String> messages = new ArrayList<>(BP.messagesToSend);
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
                                if (Setting.BP_SHOW_POINTS_TO_NEXT_RANK.getValue())
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
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/27");
                        }

                        if (Setting.BP_SHOW_WINRATE.getValue()) {
                            double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Winrate: §b" + df1f.format(wr) + "%");
                        }
                        if (Setting.BP_SHOW_PPG.getValue()) {
                            double ppg = (double) points / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points Per Game: §b" + df1f.format(ppg));
                        }

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer(
                                    "§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : BP.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }

                        BP.messagesToSend.clear();
                        BP.footerToSend.clear();
                        BP.isRecordsRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            BP.messagesToSend.clear();
                            BP.footerToSend.clear();
                            BP.isRecordsRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : BP.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        for (String s : BP.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§o " + "                      §6§m                  §6§m                  ");
                        BP.messagesToSend.clear();
                        BP.footerToSend.clear();
                        BP.isRecordsRunning = false;
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
