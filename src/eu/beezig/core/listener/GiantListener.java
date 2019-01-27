package eu.beezig.core.listener;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.AdvancedRecords;
import eu.beezig.core.autovote.AutovoteUtils;
import eu.beezig.core.games.Giant;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.gnt.GiantRank;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.StreakUtils;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.gnt.GntMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.GntStats;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.GntmStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class GiantListener extends AbstractGameListener<Giant> {

    public static GiantListener listener;
    private Giant instance = Giant.instance;
    private String lobby;

    public GiantListener() {
        listener = this;
    }

    @Override
    public Class<Giant> getGameMode() {
        // TODO Auto-generated method stub
        return Giant.class;
    }

    @Override
    public boolean matchLobby(String arg0) {

        if (arg0.toUpperCase().startsWith("GNT")) { // Support for GNTM
            lobby = arg0;
            return true;
        } else {
            return false;
        }
    }

    public void setGameMode(Class<? extends Giant> newMode, Giant instance) {
        Class<Giant> gameMode = (Class<Giant>) newMode;
        this.instance = instance;
        The5zigAPI.getLogger().info(instance.getClass());

    }

    @Override
    public void onGameModeJoin(Giant gameMode) {
        if (this.lobby.equalsIgnoreCase("GNT"))
            ActiveGame.set("GNT");
        if (this.lobby.equalsIgnoreCase("GNTM"))
            ActiveGame.set("GNTM");
        gameMode.setState(GameState.STARTING);
        IHive.genericJoin();
        SendTutorial.send("gnt");
        new Thread(() -> {
            try {
                Giant.initDailyPointsWriter();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            String ign = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
            GntStats gnt = ActiveGame.is("GNTM") ? new GntmStats(ign) : new GntStats(ign);

            Giant.totalKills = (int) gnt.getKills();
            Giant.totalDeaths = (int) gnt.getDeaths();

            Giant.totalKdr = (double) Giant.totalKills / Giant.totalDeaths;
            Giant.gameKdr = Giant.totalKdr;


            Giant.rankObject = GiantRank
                    .getFromDisplay(gnt.getTitle());
            Giant.rank = Giant.rankObject.getTotalDisplay();

            APIValues.Giantpoints = gnt.getPoints();

        }).start();


    }

    @Override
    public boolean onServerChat(Giant gameMode, String message) {

        if (message.startsWith(getPrefixWithBoldDivider(ActiveGame.current()) + "§a§lVote received. §3Your map now has")
                && Setting.AUTOVOTE.getValue()) {
            Giant.hasVoted = true;
        }

        // §8▍ §aSky§b§b§lGiants§a§l§a§l:Mini§8§l ▏ §6§l§e§l§e§l1. §f§6Blossom §a[§f0§a
        // Votes]
        else if (message.startsWith(getPrefixWithBoldDivider(ActiveGame.current()) + "§6§l§e§l§e§l6. ")
                && !Giant.hasVoted && Setting.AUTOVOTE.getValue()) {
            Giant.votesToParse.add(message);
            new Thread(() -> {
                List<String> votesCopy = new ArrayList<>(Giant.votesToParse);

                List<String> parsedMaps = new ArrayList<>(AutovoteUtils.getMapsForMode("gnt"));

                TreeMap<String, Integer> votesindex = new TreeMap<>();
                LinkedHashMap<String, Integer> finalvoting = new LinkedHashMap<>();

                for (String s : votesCopy) {

                    String[] data = s.split("\\.");
                    String index = ChatColor.stripColor(data[0])
                            .replaceAll(getPrefixWithBoldDivider(ActiveGame.current()) + "§6§l§e§l§e§l", "")
                            .replaceAll(ChatColor.stripColor(getPrefixWithBoldDivider(ActiveGame.current())), "")
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
                    The5zigAPI.getAPI()
                            .messagePlayer(getPrefix(ActiveGame.current()) + "§eAutomatically voted for map §6#" + votesindex.firstEntry().getValue());

                }
                Giant.votesToParse.clear();
                Giant.hasVoted = true;


            }).start();

        } else if (message.startsWith(getPrefixWithBoldDivider(ActiveGame.current()) + "§6§l§e§l§e§l")
                && !Giant.hasVoted && Setting.AUTOVOTE.getValue()) {
            Giant.votesToParse.add(message);
            The5zigAPI.getLogger().info("Added map");
        } else if (message.startsWith(getPrefix(ActiveGame.current()) + "§3You are now playing on the ")) {
            Giant.team = message.replaceAll(getPrefix(ActiveGame.current()) + "§3You are now playing on the ", "")
                    .replaceAll("Team!", "");
            Giant.inGame = true;
            The5zigAPI.getAPI().sendPlayerMessage("/gameid");
            gameMode.setState(GameState.GAME);
            DiscordUtils.updatePresence("Slaying in SkyGiants" + (ActiveGame.is("gntm") ? ":Mini" : ""), "Battling on " + Giant.activeMap, "game_giant");
        } else if (message.startsWith(getPrefix(ActiveGame.current()) + "§3Voting has ended! §bThe map §f")) {
            String data[] = message.replaceAll(getPrefix(ActiveGame.current()) + "§3Voting has ended! §bThe map §f", "")
                    .split("§b");
            String mapString = data[0].trim();
            The5zigAPI.getLogger().info(mapString.trim() + " / " + ActiveGame.is("GNTM"));

            Giant.activeMap = ChatColor.stripColor(mapString.trim());


        } else if (message.startsWith("§8▍ §e§lGiant§8 ▏ §7You killed the")) {
            APIValues.Giantpoints += 50;
            Giant.dailyPoints += 50;
        } else if (message.startsWith("§8▍ §6§lGold§8 ▏ §a✚ §3You gained")
                && message.contains("for killing")) {
            if (message.contains("as a team")) {
                if (Giant.isEnding) {
                    System.out.println("Won!");
                    // Won
                    Giant.isEnding = false;
                    Giant.hasWon = true;
                    Giant.winstreak++;
                    if (Giant.winstreak >= Giant.bestStreak) Giant.bestStreak = Giant.winstreak;
                    StreakUtils.incrementWinstreakByOne("gnt");
                }
                Giant.giantKills++; // Giant kill

            } else {
                Giant.gameKills++;
                APIValues.Giantpoints += 10;
                Giant.dailyPoints += 10;
                Giant.gameKdr = ((double) (Giant.totalKills + Giant.gameKills)
                        / (double) (Giant.gameDeaths + Giant.totalDeaths == 0 ? 1
                        : Giant.gameDeaths + Giant.totalDeaths));
            }
        } else if (message.contains(" §aWant to see this game's recap")) {
            Giant.isEnding = true;
        }

        // Advanced Records

        else if (message.contains("'s Stats §6§m                  ") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            Giant.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found header");
            return true;
        } else if (message.startsWith("§3 ") && Setting.ADVANCED_RECORDS.getValue()) {
            if (message.contains("User") || message.contains("Skill Rating")) {
                return true;
            }

            Giant.messagesToSend.add(message);
            The5zigAPI.getLogger().info("found entry");

            return true;
        } else if (message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§f ") && Setting.ADVANCED_RECORDS.getValue()) {
            Giant.footerToSend.add(message);
            The5zigAPI.getLogger().info("Found Player URL");

            return true;
        } else if ((message.equals("                      §6§m                  §6§m                  ")
                && !message.startsWith("§f ")) && Setting.ADVANCED_RECORDS.getValue()) {
            The5zigAPI.getLogger().info("found footer");
            Giant.footerToSend.add(message);
            The5zigAPI.getLogger().info("executed /records");
            if (Giant.footerToSend.contains("                      §6§m                  §6§m                  ")) {
                // Advanced Records - send
                The5zigAPI.getLogger().info("Sending adv rec");
                new Thread(() -> {

                    AdvancedRecords.isRunning = true;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
                    try {
                        GiantRank rank = null;

                        GntStats api = ActiveGame.is("GNTM")
                                ? new GntmStats(AdvancedRecords.player, true)
                                : new GntStats(AdvancedRecords.player, true);
                        HivePlayer parent = api.getPlayer();

                        String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue()
                                ? parent.getRank().getHumanName()
                                : "";
                        ChatColor rankColor = null;
                        if (Setting.SHOW_NETWORK_RANK_COLOR.getValue()) {
                            rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                        }
                        long points = 0;
                        int vics = 0, played = 0, kills = 0, deaths = 0;
                        Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue()
                                ? api.getLastLogin()
                                : null;
                        String rankTitleGiant = Setting.SHOW_RECORDS_RANK.getValue()
                                ? api.getTitle()
                                : null;
                        The5zigAPI.getLogger().info(rankTitleGiant);
                        long monthlyRank = 0;
                        if (Setting.SHOW_RECORDS_MONTHLYRANK.getValue()) {
                            try {
                                GntMonthlyProfile monthly = api.getMonthlyProfile();
                                if (monthly != null) {
                                    monthlyRank = monthly.getPlace();
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        if (rankTitleGiant != null)
                            rank = GiantRank.getFromDisplay(rankTitleGiant);
                        List<String> messages = new ArrayList<>(Giant.messagesToSend);
                        Iterator<String> it = messages.iterator();
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

                            if (s.startsWith("§3 Total Points: §b")) {
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


                            } else if (s.startsWith("§3 Games Played: §b")) played = Math.toIntExact(currentValue);
                            else if (s.startsWith("§3 Victories: §b")) vics = Math.toIntExact(currentValue);
                            else if (s.startsWith("§3 Kills: §b")) kills = Math.toIntExact(currentValue);
                            else if (s.startsWith("§3 Deaths: §b")) deaths = Math.toIntExact(currentValue);

                            The5zigAPI.getAPI().messagePlayer("§f " + s);

                        }

                        Integer wr = Setting.SHOW_RECORDS_WINRATE.getValue() ? (int) (Math
                                .floor(((double) vics
                                        / (double) played) * 1000d)
                                / 10d) : null;
                        if (monthlyRank != 0) {
                            The5zigAPI.getAPI().messagePlayer("§f §3 Monthly Place: §b#" + monthlyRank);
                        }
                        Double kd = Setting.SHOW_RECORDS_KDR.getValue()
                                ? Math.floor(((double) kills
                                / (double) deaths * 100d)) / 100d
                                : null;
                        Double ppg = Setting.SHOW_RECORDS_PPG.getValue() ? Math
                                .round(((double) points
                                        / (double) played) * 10d)
                                / 10d : null;


                        if (ppg != null)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Points per Game: §b" + ppg);
                        if (kd != null)
                            The5zigAPI.getAPI().messagePlayer("§f §3 K/D: §b" + kd);
                        if (wr != null)
                            The5zigAPI.getAPI().messagePlayer("§f §3 Winrate: §b" + wr + "%");

                        if (lastGame != null) {
                            Calendar lastSeen = Calendar.getInstance();
                            lastSeen.setTimeInMillis(lastGame.getTime());

                            The5zigAPI.getAPI().messagePlayer(
                                    "§f §3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()));
                        }

                        for (String s : Giant.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }

                        Giant.messagesToSend.clear();
                        Giant.footerToSend.clear();
                        AdvancedRecords.isRunning = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getCause() instanceof FileNotFoundException) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
                            Giant.messagesToSend.clear();
                            Giant.footerToSend.clear();
                            AdvancedRecords.isRunning = false;
                            return;
                        }
                        The5zigAPI.getAPI().messagePlayer(Log.error
                                + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");

                        for (String s : Giant.messagesToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        for (String s : Giant.footerToSend) {
                            The5zigAPI.getAPI().messagePlayer("§f " + s);
                        }
                        The5zigAPI.getAPI().messagePlayer(
                                "§f " + "                      §6§m                  §6§m                  ");
                        Giant.messagesToSend.clear();
                        Giant.footerToSend.clear();
                        AdvancedRecords.isRunning = false;
                    }
                }).start();
                return true;

            }

        } else if (message.contains("§bYou can find all §emessages and game events §bat")) {
            Giant.gameId = message.split("skygiants/game/")[1];
        }

        return false;
    }

    @Override
    public void onServerConnect(Giant gameMode) {
        if (instance != null) {
            Giant.reset(instance);
        } else {
            Giant.reset(gameMode);
        }

    }

    @Override
    public void onTitle(Giant gameMode, String title, String subTitle) {
        if (subTitle != null) {

            if (ChatColor.stripColor(subTitle).equalsIgnoreCase("Respawning in 3 seconds")) {
                Giant.gameDeaths++;
                Giant.gameKdr = (((double) Giant.totalKills + (double) Giant.gameKills)
                        / ((double) Giant.gameDeaths + (double) Giant.totalDeaths == 0 ? 1
                        : (double) Giant.gameDeaths + (double) Giant.totalDeaths));
            }

        }
    }

    private String getPrefix(String mode) {
        // §8▍ §aSky§b§b§lGiants§8§l ▏ §6§l§e§l§e§l6. §f§6Lost §7[§f0§7 Votes]
        if (mode.equalsIgnoreCase("gnt")) {
            return "§8▍ §aSky§b§lGiants§8 ▏ ";
        } else if (mode.equalsIgnoreCase("gntm")) {
            return "§8▍ §aSky§b§lGiants§a§l:Mini§8 ▏ ";
        }

        return "";
    }

    private String getPrefixWithBoldDivider(String mode) {
        // §8▍ §aSky§b§b§lGiants§8§l ▏ §6§l§e§l§e§l6. §f§6Lost §7[§f0§7 Votes]
        if (mode.equalsIgnoreCase("gnt")) {
            return "§8▍ §aSky§b§b§lGiants§8§l ▏ ";
        } else if (mode.equalsIgnoreCase("gntm")) {
            return "§8▍ §aSky§b§b§lGiants§a§l§a§l:Mini§8§l ▏ ";
        }

        return "";
    }

}
