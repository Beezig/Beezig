package tk.roccodev.beezig.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.autovote.AutovoteUtils;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.hiveapi.APIValues;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.bed.MonthlyPlayer;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiBED;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.advancedrecords.AdvancedRecords;
import tk.roccodev.beezig.utils.StreakUtils;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

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
        // TODO Auto-generated method stub
        return arg0.equals("BED");
    }

    @Override
    public void onGameModeJoin(BED gameMode) {

        gameMode.setState(GameState.STARTING);
        ActiveGame.set("BED");
        IHive.genericJoin();

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


                if (sb != null && sb.getTitle().contains("BED")) {
                    BED.apiKills = sb.getLines().get(ChatColor.AQUA + "Kills");
                    BED.apiDeaths = sb.getLines().get(ChatColor.AQUA + "Deaths");
                } else {
                    String ign2 = The5zigAPI.getAPI().getGameProfile().getName();
                    ApiBED api = new ApiBED(ign2);
                    BED.apiDeaths = Math.toIntExact(api.getDeaths());
                    BED.apiKills = Math.toIntExact(api.getKills());
                }
                BED.updateMode();

                String ign1 = The5zigAPI.getAPI().getGameProfile().getName();
                APIValues.BEDpoints = new ApiBED(ign1).getPoints();
                BED.updateRank();
                BED.updateKdr();
                The5zigAPI.getLogger().info(BED.apiDeaths + " / " + BED.apiKills + " / " + BED.apiKdr);
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
        }
        else if(message.equals("                        §6§lBedWars§7 - §a§lDouble Fun")) {
          BED.inGame = true;
          BED.mode = "Double Fun";

        } else if (message.startsWith("§8▍ §3§lBed§b§lWars§8 ▏ §2✚")) {
            int pts = Integer.parseInt(message.split("§a")[1].split(" Points")[0]);
            BED.pointsCounter += pts;
            APIValues.BEDpoints += pts;
            BED.dailyPoints += pts;

            if(message.endsWith("Generator]")) return false;

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
            HiveAPI.medals++;
            HiveAPI.tokens += 100;
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

        else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ") && Setting.ADVANCED_RECORDS.getValue()) {
            BED.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {

            BED.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttps://hivemc.com/player/") && !message.startsWith("§o ") && Setting.ADVANCED_RECORDS.getValue()) {
            BED.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ") && !message.startsWith("§o ")) && Setting.ADVANCED_RECORDS.getValue()) {
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

                        ApiBED api = new ApiBED(AdvancedRecords.player);

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


                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? api.getParentMode().getNetworkTitle() : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {

                            rankColor = api.getParentMode().getNetworkRankColor();

                        }
                        long points;

                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
                        Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements() : null;
                        Integer streak = Setting.BED_SHOW_STREAK.getValue() ? api.getStreak() : null;


                        int monthlyRank = 0;
                        if (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()) {
                            MonthlyPlayer monthly = api.getMonthlyStatus();
                            if (monthly != null) {
                                monthlyRank = monthly.getPlace();
                            }
                        }

                        List<String> messages = new ArrayList<>(BED.messagesToSend);
                        for (String s : messages) {


                            if (s.trim().endsWith("'s Stats §6§m")) {
                                The5zigAPI.getLogger().info("Editing Header...");
                                StringBuilder sb = new StringBuilder();
                                String correctUser = api.getParentMode().getCorrectName();
                                if (correctUser.contains("nicked player")) correctUser = "Nicked/Not found";
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
                                    if (rankColor == null) rankColor = ChatColor.WHITE;
                                    The5zigAPI.getAPI().messagePlayer("§o           " + "§6§m       §6" + " (" + rankColor + rankTitle + "§6) " + "§m       ");
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
                                    BEDRank rank = BEDRank.isNo1(AdvancedRecords.player) ? BEDRank.ZZZZZZ : BEDRank.getRank((int) points);
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

                                The5zigAPI.getAPI().messagePlayer("§o" + sb.toString().trim());
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

                            The5zigAPI.getAPI().messagePlayer("§o" + s);

                        }


                        if (achievements != null) {
                            The5zigAPI.getAPI().messagePlayer("§o§3 Achievements: §b" + achievements + "/67");
                        }
                        if(streak != null) {
                            The5zigAPI.getAPI().messagePlayer("§o§3 Win Streak: §b" + streak);
                        }
                        // "§8▍ §3§lBed§b§lWars§8 ▏ §aYou gained 10§a points for killing"

                        if (Setting.BED_SHOW_ELIMINATIONS_PER_GAME.getValue()) {
                            double epg = eliminations / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§o§3 Eliminations per Game: §b" + df1f.format(epg));
                        }
                        if (Setting.BED_SHOW_BEDS_PER_GAME.getValue()) {
                            double bpg = bedsDestroyed / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§o§3 Beds per Game: §b" + df1f.format(bpg));
                        }
                        if (Setting.SHOW_RECORDS_DPG.getValue()) {
                            double dpg = deaths / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§o§3 Deaths per Game: §b" + df1f.format(dpg));
                        }
                        if (Setting.SHOW_RECORDS_KPG.getValue()) {
                            double kpg = kills / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§o§3 Kills per Game: §b" + df1f.format(kpg));
                        }
                        if (Setting.SHOW_RECORDS_PPG.getValue()) {
                            double ppg = BED.lastRecordsPoints / (double) (gamesPlayed == 0 ? 1 : gamesPlayed);
                            The5zigAPI.getAPI().messagePlayer("§o§3 Points per Game: §b" + df1f.format(ppg));
                        }
                        if (Setting.SHOW_RECORDS_KDR.getValue()) {
                            double kdr = kills / (double) (deaths == 0 ? 1 : deaths);
                            The5zigAPI.getAPI().messagePlayer("§o§3 K/D: §b" + df.format(kdr));
                        }
                        if (Setting.SHOW_RECORDS_WINRATE.getValue()) {
                            double wr = Math.floor(((double) victories / (double) gamesPlayed) * 1000d) / 10d;
                            The5zigAPI.getAPI().messagePlayer("§o§3 Winrate: §b" + df1f.format(wr) + "%");
                        }
                        if (monthlyRank != 0) {

                            The5zigAPI.getAPI().messagePlayer("§o§3 Monthly Place: §b#" + monthlyRank);
                        }


                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());

                            The5zigAPI.getAPI().messagePlayer("§o§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }


                        for (String s : BED.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o" + s);
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
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        for (String s : BED.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§o " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer("§o " + "                      §6§m                  §6§m                  ");
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
        } else if (message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l5. ") && !BED.hasVoted && Setting.AUTOVOTE.getValue()) {
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
                    String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l", "").replaceAll("▍ BedWars ▏", "").trim();
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

                }
                else if(Setting.AUTOVOTE_RANDOM.getValue()) {
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
        } else if (message.startsWith("§8▍ §3§3§lBed§b§l§b§lWars§8§l ▏ §6§l§e§l§e§l") && !BED.hasVoted && Setting.AUTOVOTE.getValue()) {
            BED.votesToParse.add(message);
        } else if (message.contains("§aYou levelled up to")) {
            //Update the rank module when you uprank
            new Thread(() -> {
                try {
                    String ign = The5zigAPI.getAPI().getGameProfile().getName();
                    APIValues.BEDpoints = new ApiBED(ign).getPoints();
                    Thread.sleep(200);
                    BED.updateRank();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } else if (message.trim().equals("§d§lNew Rank!")) {
            new Thread(() -> {

                try {
                    String ign = The5zigAPI.getAPI().getGameProfile().getName();
                    APIValues.BEDpoints = new ApiBED(ign).getPoints();
                    Thread.sleep(200);
                    BED.updateRank();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
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
