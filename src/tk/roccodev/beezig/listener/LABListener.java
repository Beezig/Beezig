package tk.roccodev.beezig.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.games.LAB;
import tk.roccodev.beezig.hiveapi.APIValues;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.stuff.lab.LABRank;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiLAB;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class LABListener extends AbstractGameListener<LAB> {
    @Override
    public Class<LAB> getGameMode() {
        return LAB.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return s.equals("LAB");
    }

    @Override
    public void onGameModeJoin(LAB gameMode) {
        gameMode.setState(GameState.STARTING);
        ActiveGame.set("LAB");
        IHive.genericJoin();

        new Thread(() -> {
            try {
                try {
                    LAB.initDailyPointsWriter();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                Thread.sleep(500);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();

                ApiLAB api = new ApiLAB(The5zigAPI.getAPI().getGameProfile().getName());

                if (sb != null && sb.getTitle().contains("Your LAB Stats")) {
                    int points = sb.getLines().get(ChatColor.AQUA + "Total Atoms");
                    APIValues.LABpoints = (long) points;

                    LAB.rankObject = LABRank.getFromDisplay(api.getTitle());
                    LAB.rank = LAB.rankObject.getTotalDisplay();

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean onServerChat(LAB gameMode, String message) {
        if(message.equals("§8▍ §3The§bLab§8 ▏ §aYou were awarded §b§l10 atoms and 20 tokens§a for being in the top 3!")) {
            LAB.dailyPoints += 10;
            APIValues.LABpoints += 10;
            HiveAPI.tokens += 20;
        }
        else if(message.startsWith("§8▍ §3The§bLab§8 ▏ §a§lExperiment ")) {
            LAB.experiments.add(ChatColor.stripColor(message.split(":")[1].trim()));
        }
        else if(message.equals("§8▍ §3The§bLab§8 ▏ §aYou were awarded §b§l2 atoms and 10 tokens§a for participating!")) {
            LAB.dailyPoints += 2;
            APIValues.LABpoints += 2;
            HiveAPI.tokens += 10;
        }
        else if(message.endsWith("[+ 3 Atoms]") && message.startsWith(" §e§lFirst:")) {
            String name = ChatColor.stripColor(message.split("\\[")[0].trim().replace("§e§lFirst: ", ""));
            if(name.equals(The5zigAPI.getAPI().getGameProfile().getName())) {
                LAB.dailyPoints += 3;
                APIValues.LABpoints += 3;
            }
            LAB.leaderboard.put(name, LAB.leaderboard.get(name) + 3);
            LAB.leaderboard = LAB.sortByValue(LAB.leaderboard);
        }
        else if(message.endsWith("[+ 2 Atoms]") && message.startsWith(" §b§lSecond:")) {
            String name = ChatColor.stripColor(message.split("\\[")[0].trim().replace("§b§lSecond: ", ""));
            if(name.equals(The5zigAPI.getAPI().getGameProfile().getName())) {
                LAB.dailyPoints += 2;
                APIValues.LABpoints += 2;
            }
            LAB.leaderboard.put(name, LAB.leaderboard.get(name) + 2);
            LAB.leaderboard = LAB.sortByValue(LAB.leaderboard);
        }
        else if(message.endsWith("[+ 1 Atom]") && message.startsWith(" §6§lThird:")) {
            String name = ChatColor.stripColor(message.split("\\[")[0].trim().replace("§6§lThird: ", ""));
            if(name.equals(The5zigAPI.getAPI().getGameProfile().getName())) {
                LAB.dailyPoints += 1;
                APIValues.LABpoints += 1;
            }
            LAB.leaderboard.put(name, LAB.leaderboard.get(name) + 1);
            LAB.leaderboard = LAB.sortByValue(LAB.leaderboard);
        }
        else if(message.contains(The5zigAPI.getAPI().getGameProfile().getName() + "§7 [+ ")) {
            int atoms = Integer.parseInt(message.split("\\[\\+")[1].replace(" Atom", "").replace("s", "").trim());
            LAB.dailyPoints += atoms;
            APIValues.LABpoints += atoms;
        }
        if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
            LAB.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ")) {

            LAB.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
            LAB.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§o "))) {
            The5zigAPI.getLogger().info("found footer");
            LAB.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (LAB.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    LAB.isRecordsRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {

                        ApiLAB api = new ApiLAB(LAB.lastRecords);
                        LABRank rank = null;

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
                        String rankTitleLAB = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
                        if (rankTitleLAB != null)
                            rank = LABRank.getFromDisplay(rankTitleLAB);


                        long points = 0;
                        int gamesPlayed = 0;
                        int victories = 0;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements()
                                : null;

                        List<String> messages = new ArrayList<>(LAB.messagesToSend);
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
                            } 
                            
                            
                            String[] newData = s.split("\\: §b");
                            long currentValue = 0;
                            try {
                            	currentValue = Long.parseLong(newData[1]);
                            	newData[1] = Log.df(currentValue);
                            	s = newData[0] + ": §b" + newData[1];
                            }
                            catch(NumberFormatException ignored) {
                            	s = newData[0] + ": §b" + newData[1];
                            }
                            
                            if (s.startsWith("§3 Atoms: §b")) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("§3 Atoms: §b");
                                points = currentValue;
                                sb.append(newData[1]);
                                if (rank != null)
                                    sb.append(" (").append(rank.getTotalDisplay());
                                if (Setting.LAB_SHOW_POINTS_TO_NEXT_RANK.getValue())
                                    sb.append(" / ").append(rank.getPointsToNextRank((int) points));
                                if (rank != null)
                                    sb.append("§b)");

                                // if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§b)");

                                The5zigAPI.getAPI().messagePlayer("§o" + sb.toString().trim());
                                continue;
                            } else if (s.startsWith("§3 Victories: §b")) {
                                victories = Math.toIntExact(currentValue);
                            } else if (s.startsWith("§3 Played: §b")) {
                                gamesPlayed = Math.toIntExact(currentValue);
                            }

                            The5zigAPI.getAPI().messagePlayer("§o" + s);

                        }

                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§o§3 Achievements: §b" + achievements);
                        }

                        if (Setting.LAB_SHOW_WINRATE.getValue()) {
                            double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§o§3 Winrate: §b" + df1f.format(wr) + "%");
                        }
                        if (Setting.LAB_SHOW_PPG.getValue()) {
                            double ppg = (double) points / (double) gamesPlayed;
                            The5zigAPI.getAPI().messagePlayer("§o§3 Atoms Per Game: §b" + df1f.format(ppg));
                        }

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());
                            The5zigAPI.getAPI().messagePlayer(
                                    "§o§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : LAB.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o" + s);
                        }

                        LAB.messagesToSend.clear();
                        LAB.footerToSend.clear();
                        LAB.isRecordsRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            LAB.messagesToSend.clear();
                            LAB.footerToSend.clear();
                            LAB.isRecordsRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : LAB.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        for (String s : LAB.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§o " + "                      §6§m                  §6§m                  ");
                        LAB.messagesToSend.clear();
                        LAB.footerToSend.clear();
                        LAB.isRecordsRunning = false;
                    }
                }).start();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onServerConnect(LAB gameMode) {
        LAB.reset(gameMode);
    }

    @Override
    public void onTitle(LAB gameMode, String title, String subTitle) {
        if(subTitle != null && subTitle.equals("§r§3Experiment §r§b§l1§r§3 of §r§a§l3§r")) {
            for(NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                LAB.leaderboard.put(npi.getGameProfile().getName(), 0);
            }
        }
        if(subTitle != null && subTitle.startsWith("§r§3Experiment §r§b§l") && title != null) {
            LAB.experiment = ChatColor.stripColor(title.trim());
            DiscordUtils.updatePresence("Experimenting in TheLab", "Playing " + LAB.experiment, "game_lab");
        }
    }
}
