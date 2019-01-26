package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.anywhere.statistic.RecordsStatistic;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
import eu.beezig.core.hiveapi.stuff.sgn.SGNRank;
import eu.beezig.core.hiveapi.stuff.timv.TIMVRank;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.beezig.core.utils.TabCompletionUtils;
import eu.beezig.core.utils.mps.MultiPsStats;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.GameStats;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (args.length == 0)
            game = ActiveGame.current();
        else
            game = args[0];

        String rankName = game.equalsIgnoreCase("gnt") || game.equals("gntm") ? "Giant" : game.toUpperCase();
        String rankNamePkg = game.equalsIgnoreCase("gnt") || game.equals("gntm") ? "gnt" : game;

        long startT = System.currentTimeMillis();


        new Thread(() -> {
            Class enumToUse;
            try {
                enumToUse = Class.forName("eu.beezig.core.hiveapi.stuff." + rankNamePkg.toLowerCase() + "." + rankName + "Rank");
            } catch (ClassNotFoundException ignored) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Specified mode not found.");
                return;
            }


            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            List<Double> points = new ArrayList<>();
            List<String> title = new ArrayList<>();
            List<String> name = new ArrayList<>();

            String pointsStr = game.equalsIgnoreCase("timv") || game.equalsIgnoreCase("mimv")
                    ? "karma" : "points";

            RecordsStatistic pointStringToUse =
                    MultiPsStats.getRecordsStatistic(game, args.length < 2 ? pointsStr : args[1]);

            if (pointStringToUse == null) return;


            for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                try {
                    String uuid = npi.getGameProfile().getId().toString().replace("-", "");
                    String display = npi.getDisplayName() == null ? npi.getGameProfile().getName() : npi.getDisplayName();
                    GameStats api = new GameStats(uuid, game.toUpperCase());
                    api.getSource().fetch();
                    HivePlayer parent = api.getPlayer();
                    ChatColor rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();

                    points.add(((Number) pointStringToUse.getValueRaw(api.getSource().getInput())).doubleValue());

                    RankEnum rank = game.equalsIgnoreCase("bed")
                            ? (BEDRank.newIsNo1(api.getSource().getString("title"), api.getPoints()) ? BEDRank.ZZZZZZ
                            : BEDRank.getRank(api.getPoints())) : (game.equalsIgnoreCase("sgn") ? SGNRank.getRank(api.getPoints()) : null);
                    if (rank == null) {
                        rank = (RankEnum) enumToUse.getMethod("getFromDisplay", String.class).invoke(null, api.getSource().getString("title"));
                    }

                    if (game.equalsIgnoreCase("timv"))
                        title.add(((TIMVRank) rank).getTotalDisplay(api.getPoints()));
                    else
                        title.add(rank.getTotalDisplay());
                    name.add(rankColor + display);
                } catch (Exception ignored) {
                }
            }

            APIUtils.concurrentSort(points, points, title, name);

            The5zigAPI.getAPI().messagePlayer("\n"
                    + "    §7§m                                                                                    ");
            for (int i = 0; i < name.size(); i++) {
                try {
                    if (points.get(i) != 0D) {
                        if (title.get(i).equals("§a§lMaster §e§lof §b§lDisguise")) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + "§a§l" + Log.ratio(points.get(i)) + "§7 - " + title.get(i) + "§r " + name.get(i));
                        } else {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replace(ChatColor.stripColor(title.get(i)), "")
                                            + Log.ratio(points.get(i)) + "§7 - " + title.get(i) + "§r " + name.get(i));
                        }

                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            The5zigAPI.getAPI()
                    .messagePlayer(Log.info + game.toUpperCase() + " Playerstats: §b" + name.size() + "P / "
                            + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                            + Log.ratio(APIUtils.average(points.toArray())) + " Average");
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");
        }).start();

        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        if (args.length == 2) {
            return TabCompletionUtils.matching(args, MultiPsStats.getAllPossibleValues(args[0]));
        }
        return TabCompletionUtils.matching(args,
                Arrays.asList("bed", "sky", "timv", "dr", "bp", "cai", "dr", "grav",
                        "gnt", "gntm", "hide", "lab", "mimv", "sgn"));
    }
}
