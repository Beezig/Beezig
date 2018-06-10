package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;
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
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.modes.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsCommand implements Command {
    @Override
    public String getName() {
        return "playerstats";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/playerstats", "/ps"};
    }

    @Override
    public boolean execute(String[] args) {

        String game;
        if (args.length == 0) {
            game = ActiveGame.current();
        } else
            game = args[0];

        if (game.equalsIgnoreCase("timv")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiTIMV apiTIMV = new ApiTIMV(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiTIMV.getParentMode();
                        points.add(apiTIMV.getKarma());
                        title.add(TIMVRank.getFromDisplay(apiTIMV.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "TIMV Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("bed")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> titlecolor = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiBED apiBED = new ApiBED(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiBED.getParentMode();
                        points.add(apiBED.getPoints());

                        BEDRank rank = BEDRank.getRank(apiBED.getPoints());
                        if (rank != null) {
                            int level = rank.getLevel((int) apiBED.getPoints());
                            titlecolor.add(rank.getName().replaceAll(ChatColor.stripColor(rank.getName()), ""));
                            title.add(BED.NUMBERS[level] + " " + rank.getName());
                        }

                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, titlecolor, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(Log.info + titlecolor.get(i) + points.get(i) + "§7 - "
                                    + titlecolor.get(i) + title.get(i) + "§r " + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "BED Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("dr")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiDR apiDR = new ApiDR(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiDR.getParentMode();
                        points.add(apiDR.getPoints());
                        title.add(DRRank.getFromDisplay(apiDR.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "DR Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("cai")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiCAI apiCAI = new ApiCAI(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiCAI.getParentMode();
                        points.add(apiCAI.getPoints());
                        title.add(CAIRank.getFromDisplay(apiCAI.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "CAI Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("hide")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiHIDE apiHIDE = new ApiHIDE(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiHIDE.getParentMode();
                        points.add(apiHIDE.getPoints());
                        title.add(HIDERank.getFromDisplay(apiHIDE.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            if (title.get(i).equals("§e§lMaster §a§lof §b§lDisguise")) {
                                The5zigAPI.getAPI()
                                        .messagePlayer("§e"
                                                + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                            } else {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info
                                                + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                                + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                            }
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "HIDE Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("sky")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiSKY apiSKY = new ApiSKY(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiSKY.getParentMode();
                        points.add(apiSKY.getPoints());
                        title.add(SKYRank.getFromDisplay(apiSKY.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "SKY Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("grav")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiGRAV apiGRAV = new ApiGRAV(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiGRAV.getParentMode();
                        points.add(apiGRAV.getPoints());
                        title.add(GRAVRank.getFromDisplay(apiGRAV.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "GRAV Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("mimv")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiMIMV apiMIMV = new ApiMIMV(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiMIMV.getParentMode();
                        points.add(apiMIMV.getPoints());
                        title.add(MIMVRank.getFromDisplay(apiMIMV.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "MIMV Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("bp")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiBP apiBP = new ApiBP(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiBP.getParentMode();
                        points.add(apiBP.getPoints());
                        title.add(BPRank.getFromDisplay(apiBP.getTitle()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "BP Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
                The5zigAPI.getAPI().messagePlayer(
                        "    §7§m                                                                                    "
                                + "\n");
            }).start();
        } else if (game.equalsIgnoreCase("sgn")) {
            long startT = System.currentTimeMillis();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            new Thread(() -> {
                List<Long> points = new ArrayList<>();
                List<String> title = new ArrayList<>();
                List<String> name = new ArrayList<>();

                for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                    try {
                        ApiSGN apiSGN = new ApiSGN(npi.getGameProfile().getName(),
                                npi.getGameProfile().getId().toString());
                        ApiHiveGlobal apiHIVE = apiSGN.getParentMode();
                        points.add(apiSGN.getPoints());
                        title.add(SGNRank.getRank(apiSGN.getPoints()).getTotalDisplay());
                        name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                APIUtils.concurrentSort(points, points, title, name);

                The5zigAPI.getAPI().messagePlayer("\n"
                        + "    §7§m                                                                                    ");
                for (int i = 0; i < name.size(); i++) {
                    try {
                        if (points.get(i) != 0) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                            + points.get(i) + " §7- " + title.get(i) + " §r" + name.get(i));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                The5zigAPI.getAPI()
                        .messagePlayer(Log.info + "SGN Playerstats: §b" + name.size() + "P / "
                                + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                                + APIUtils.average(points.toArray()) + " Average");
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
