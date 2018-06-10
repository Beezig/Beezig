package tk.roccodev.beezig.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.autovote.AutovoteUtils;
import tk.roccodev.beezig.games.DR;
import tk.roccodev.beezig.games.HIDE;
import tk.roccodev.beezig.games.TIMV;
import tk.roccodev.beezig.hiveapi.APIValues;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRRank;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiDR;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

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
        new Thread(() -> {
            try {
                DR.initDailyPointsWriter();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
            DR.rankObject = DRRank.getFromDisplay(new ApiDR(The5zigAPI.getAPI().getGameProfile().getName()).getTitle());
            DR.rank = DR.rankObject.getTotalDisplay();
            // Should've read the docs ¯\_(ツ)_/¯
            if (sb != null)
                The5zigAPI.getLogger().info(sb.getTitle());
            if (sb != null && sb.getTitle().contains("Your DR Stats")) {
                int points = sb.getLines().get(ChatColor.AQUA + "Points");
                APIValues.DRpoints = (long) points;

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
            DR.activeMap = DR.mapsPool.get(map.toLowerCase());

        } else if (message.contains("§lYou are a ") && gameMode != null) {
            String afterMsg = message.split(ChatColor.stripColor("You are a "))[1];
            switch (afterMsg) {
                case "DEATH!":
                    DR.role = "Death";
                    break;
                case "RUNNER!":
                    DR.role = "Runner";
                    new Thread(() -> {
                        if (DR.activeMap != null) {
                            The5zigAPI.getLogger().info("Loading PB...");

                            ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());

                            DR.currentMapPB = api.getPersonalBest(DR.activeMap);
                            if (DR.currentMapPB == null)
                                DR.currentMapPB = "No Personal Best";
                            The5zigAPI.getLogger().info("Loading WR...");
                            DR.currentMapWR = api.getWorldRecord(DR.activeMap);
                            DR.currentMapWRHolder = api.getWorldRecordHolder(DR.activeMap);
                            if (DR.currentMapWR == null)
                                DR.currentMapWR = "No Record";
                            if (DR.currentMapWRHolder == null)
                                DR.currentMapWRHolder = "Unknown";
                        }
                    }).start();

                    break;
            }
            DiscordUtils.updatePresence("Parkouring in DeathRun", (DR.role.equals("Runner") ? "Running" : "Killing") + " on " + DR.activeMap.getDisplayName(), "game_dr");
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §aCheckpoint Reached! §7") && ActiveGame.is("dr")
                && DR.role.equals("Runner")) {
            // No more double tokens weekends Niklas :>)
            if (!(DR.checkpoints == DR.activeMap.getCheckpoints())) {
                DR.checkpoints++;

            }

            String data[] = ChatColor.stripColor(message).trim().split("\\+");
            int tokens = Integer.parseInt(data[1].trim().replaceAll("Tokens", "").trim());
            HiveAPI.tokens += tokens;
        } else if (message.equals("§8▍ §cDeathRun§8 ▏ §cYou have been returned to your last checkpoint!")
                && ActiveGame.is("dr") && DR.role.equals("Runner")) {
            DR.deaths++;
        } else if (message.contains("§6 (") && message.contains("§6)")
                && message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && ActiveGame.is("dr")
                && DR.role.equals("Death")) {
            DR.kills++;
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §a§lVote received.") && Setting.AUTOVOTE.getValue()) {
            DR.hasVoted = true;
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §6§e§e§l6. §f§cRandom map ") && !DR.hasVoted
                && Setting.AUTOVOTE.getValue()) {
            /*
             *
             * Multi-threading to avoid lag on older machines
             *
             */

            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(DR.votesToParse);
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
                DR.votesToParse.clear();
                DR.hasVoted = true;


            }).start();
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §6§e§e§l") && !DR.hasVoted && Setting.AUTOVOTE.getValue()) {
            DR.votesToParse.add(message);
        } else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")) {
            // " §6§m §f ItsNiklass's Stats §6§m "
            // Advanced Records
            DR.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ")) {

            DR.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")) {
            // TODO Coloring
            DR.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§o "))) {
            The5zigAPI.getLogger().info("found footer");
            DR.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (DR.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {
                    DR.isRecordsRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {
                        DRRank rank = null;
                        ApiDR api = new ApiDR(DR.lastRecords);

                        Double ppg = Setting.DR_SHOW_POINTSPERGAME.getValue()
                                ? Math.round(((double) api.getPoints() / (double) api.getGamesPlayed()) * 10d) / 10d
                                : null;
                        Integer rwr = Setting.DR_SHOW_RUNNERWINRATE.getValue() ? (int) (Math
                                .floor(((double) api.getVictoriesAsRunner() / (double) api.getGamesPlayedAsRunner())
                                        * 1000d)
                                / 10d) : null;
                        Double dpg = Setting.DR_SHOW_DEATHSPERGAME.getValue()
                                ? Math.floor(
                                ((double) api.getDeaths() / (double) api.getGamesPlayedAsRunner()) * 10d)
                                / 10d
                                : null;
                        Double kpg = Setting.DR_SHOW_KILLSPERGAME.getValue()
                                ? Math.round(((double) api.getKills() / (double) api.getGamesPlayedAsDeath()) * 10d) / 10d
                                : null;
                        String tpb = Setting.DR_SHOW_TOTALPB.getValue()
                                ? api.getTotalPB()
                                : null;
                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue()
                                ? api.getParentMode().getNetworkTitle()
                                : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
                            rankColor = api.getParentMode().getNetworkRankColor();
                        }
                        long points;
                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements()
                                : null;
                        String rankTitleDR = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;

                        int monthlyRank = (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()
                                && api.getLeaderboardsPlacePoints(349) < api.getPoints()) ? api.getMonthlyRank()
                                : 0;
                        if (rankTitleDR != null)
                            rank = DRRank.getFromDisplay(rankTitleDR);
                        List<String> messages = new ArrayList<>(DR.messagesToSend);
                        Iterator<String> it = messages.iterator();
                        for (String s : messages) {

                            if (s.trim().endsWith("'s Stats §6§m")) {
                                // " §6§m §f ItsNiklass's Stats §6§m "
                                // "§6§m §f ItsNiklass's Stats §6§m"
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
                                if (Setting.DR_SHOW_POINTS_TO_NEXT_RANK.getValue())
                                    sb.append(" / ").append(rank.getPointsToNextRank((int) points));
                                if (rank != null)
                                    sb.append("§b)");
                                The5zigAPI.getAPI().messagePlayer("§o " + sb.toString().trim());
                                continue;

                            }

                            The5zigAPI.getAPI().messagePlayer("§o " + s);

                        }

                        if (ppg != null) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Points per Game: §b" + ppg);
                        }
                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Achievements: §b" + achievements + "/68");
                        }
                        if (rwr != null) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Runner-Winrate: §b" + rwr + "%");
                        }
                        if (dpg != null) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Deaths per Game: §b" + dpg);
                        }
                        if (kpg != null) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Kills per Game: §b" + kpg);
                        }
                        if (tpb != null) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Total Personal Best: §b" + tpb);
                        }
                        if (monthlyRank != 0) {
                            The5zigAPI.getAPI().messagePlayer("§o " + "§3 Monthly Leaderboards: §b#" + monthlyRank);
                        }
                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());

                            The5zigAPI.getAPI().messagePlayer(
                                    "§o " + "§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : DR.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }

                        DR.messagesToSend.clear();
                        DR.footerToSend.clear();
                        DR.isRecordsRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            DR.messagesToSend.clear();
                            DR.footerToSend.clear();
                            DR.isRecordsRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : DR.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        for (String s : DR.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§o " + "                      §6§m                  §6§m                  ");
                        DR.messagesToSend.clear();
                        DR.footerToSend.clear();
                        DR.isRecordsRunning = false;
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
        } else if (message.startsWith("§8▍ §cDeathRun§8 ▏ §bYou finished your run in ")) {
            // §8▍ §cDeathRun§8 ▏ §bYou finished your run in 03:07.479§b!
            String time = (message.split("in "))[1].replace("§b!", "").trim();
            String[] data = time.split(":");
            int minutes = Integer.parseInt(data[0]);
            // data[1 ] is seconds.milliseconds
            double secondsMillis = Double.parseDouble(data[1]);
            double finalTime = 60 * minutes + secondsMillis; // e.g, You finished in 01:51.321 = 01*60 + 51.321 =
            // 111.321

            new Thread(() -> {
                ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
                double wr = api.getRawWorldRecord(DR.activeMap);
                double diff = (Math.round((finalTime - wr) * 1000d)) / 1000d;
                int finalPb;

                String pb = DR.currentMapPB;
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
                            "    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer(
                            Log.info + "   §c§lCongratulations! You §4§ltied §c§lthe §4§lWorld Record§c§l!");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer(
                            "    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer(message);
                } else if (diff < 0) {
                    The5zigAPI.getAPI().messagePlayer(
                            "    §c§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI()
                            .messagePlayer(Log.info + "   §c§lCongratulations! §4§lYou beat the World Record!!!");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer("§c  ");
                    The5zigAPI.getAPI().messagePlayer(
                            "    §c§m                                                                                    ");
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
            }).start();

            return true;
        }
        return false;
    }

    @Override
    public void onServerConnect(DR gameMode) {
        The5zigAPI.getLogger().info("Resetting! (DR)");
        DR.reset(gameMode);
    }

    @Override
    public void onTick(DR gameMode) {
        int i = 5;
        if (The5zigAPI.getAPI().getSideScoreboard() == null) return;
        HashMap<String, Integer> lines = The5zigAPI.getAPI().getSideScoreboard().getLines();
        for (Map.Entry<String, Integer> e : lines.entrySet()) {
            if (e.getValue() == i && e.getKey().contains("§7Points: ")) {
                int pts = Integer.parseInt(e.getKey().replace("§7Points: ", "").replace("§9", "").replaceAll("§f", ""));
                if (pts != HIDE.lastPts) {
                    DR.dailyPoints += (pts - DR.lastPts);
                    APIValues.DRpoints += (pts - DR.lastPts);
                    DR.lastPts = pts;
                }
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
