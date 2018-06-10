package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.bp.BPRank;
import tk.roccodev.beezig.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRRank;
import tk.roccodev.beezig.hiveapi.stuff.grav.GRAVRank;
import tk.roccodev.beezig.hiveapi.stuff.hide.HIDERank;
import tk.roccodev.beezig.hiveapi.stuff.mimv.MIMVRank;
import tk.roccodev.beezig.hiveapi.stuff.sgn.SGNRank;
import tk.roccodev.beezig.hiveapi.stuff.sky.SKYRank;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.beezig.hiveapi.wrapper.modes.*;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardCommand implements Command {
    @Override
    public String getName() {
        return "leaderboard";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/lb", "/leaderboard", "/leaderboards"};
    }

    @Override
    public boolean execute(String[] args) {
        // /lb (start) (end) (mode)
        // /lb 1 10 timv
        // --> /leaderboard/0/10
        String game;
        if (args.length == 2) {
            game = ActiveGame.current();
        } else
            game = args[2];

        long humanStart = Long.valueOf(args[0]);
        long humanEnd = Long.valueOf(args[1]);
        long indexStart = humanStart - 1L;
        long indexEnd = humanEnd;

        if (game.equalsIgnoreCase("timv")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {

                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiTIMV apiTIMV = new ApiTIMV(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiTIMV.getParentMode();

                        points.add(apiTIMV.getKarma());
                        title.add(TIMVRank.getFromDisplay(apiTIMV.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "TIMV Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("bed")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {

                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> titlecolor = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiBED apiBED = new ApiBED(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiBED.getParentMode();

                        points.add(apiBED.getPoints());
                        // BEDRank rank = BEDRank.isNo1(((JSONObject)
                        // data.get(i)).get("username").toString()) ? BEDRank.ZZZZZZ :
                        // BEDRank.getRank(Long.valueOf(((JSONObject)
                        // data.get(i)).get("points").toString()));
                        // BEDRank rank = BEDRank.getRank(Long.valueOf(((JSONObject)
                        // data.get(i)).get("points").toString()));
                        BEDRank rank = BEDRank.getRank(apiBED.getPoints());
                        if (rank != null) {
                            int level = rank.getLevel((int) apiBED.getPoints());

                            if (i == 0 && humanStart == 1) {
                                title.add(BEDRank.ZZZZZZ.getName());
                                titlecolor.add(BEDRank.ZZZZZZ.getName()
                                        .replaceAll(ChatColor.stripColor(BEDRank.ZZZZZZ.getName()), ""));
                            } else {
                                titlecolor.add(rank.getName().replaceAll(ChatColor.stripColor(rank.getName()), ""));
                                title.add(BED.NUMBERS[level] + " " + rank.getName());
                            }

                        }

                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3" + titlecolor.get(i)
                                            + points.get(i) + "§7 - " + titlecolor.get(i) + title.get(i) + "§r "
                                            + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "BED Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("dr")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiDR apiDR = new ApiDR(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiDR.getParentMode();

                        points.add(apiDR.getPoints());
                        title.add(DRRank.getFromDisplay(apiDR.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "DR Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("cai")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiCAI apiCAI = new ApiCAI(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiCAI.getParentMode();

                        points.add(apiCAI.getPoints());
                        title.add(CAIRank.getFromDisplay(apiCAI.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "CAI Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("hide")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiHIDE apiHIDE = new ApiHIDE(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiHIDE.getParentMode();

                        points.add(apiHIDE.getPoints());
                        title.add(HIDERank.getFromDisplay(apiHIDE.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            if (title.get(i).equals("§e§lMaster §a§lof §b§lDisguise")) {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                                + "§e§l"
                                                + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                            } else {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                                + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                                + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));

                            }
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "HIDE Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("sky")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiSKY apiSKY = new ApiSKY(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiSKY.getParentMode();

                        points.add(apiSKY.getPoints());
                        title.add(SKYRank.getFromDisplay(apiSKY.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "SKY Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("grav")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiGRAV apiGRAV = new ApiGRAV(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiGRAV.getParentMode();

                        points.add(apiGRAV.getPoints());
                        title.add(GRAVRank.getFromDisplay(apiGRAV.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "GRAV Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("mimv")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiMIMV apiMIMV = new ApiMIMV(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiMIMV.getParentMode();

                        points.add(apiMIMV.getPoints());
                        title.add(MIMVRank.getFromDisplay(apiMIMV.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "MIMV Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("bp")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiBP apiBP = new ApiBP(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiBP.getParentMode();

                        points.add(apiBP.getPoints());
                        title.add(BPRank.getFromDisplay(apiBP.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "BP Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("sgn")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (int i = 0; i < (humanEnd - humanStart + 1L); i++) {
                    try {
                        ApiSGN apiSGN = new ApiSGN(((JSONObject) data.get(i)).get("username").toString(),
                                ((JSONObject) data.get(i)).get("UUID").toString());

                        ApiHiveGlobal apiHIVE = apiSGN.getParentMode();

                        points.add(apiSGN.getPoints());
                        title.add(SGNRank.getRank(apiSGN.getPoints()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + ((JSONObject) data.get(i)).get("username").toString());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                            + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "SGN Leaderboards: §b" + name.size() + "P / "
                        + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + humanEnd);
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Specified mode not found.");
        }
        return true;
    }
}
