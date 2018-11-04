package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.GameStats;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.stuff.RankEnum;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.sgn.SGNRank;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.NetworkRank;

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
                enumToUse = Class.forName("tk.roccodev.beezig.hiveapi.stuff." + rankNamePkg.toLowerCase() + "." + rankName + "Rank");
            } catch (ClassNotFoundException ignored) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Specified mode not found.");
                return;
            }


            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            List<Long> points = new ArrayList<>();
            List<String> title = new ArrayList<>();
            List<String> name = new ArrayList<>();

            String pointStringToUse = game.equalsIgnoreCase("grav") ? "points" : "total_points";


            for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                try {
                    String uuid = npi.getGameProfile().getId().toString().replace("-", "");
                    String display = npi.getDisplayName() == null ? npi.getGameProfile().getName() : npi.getDisplayName();
                    GameStats api = new GameStats(uuid, game.toUpperCase()) {

                        @Override
                        public long getPoints() {

                            return getSource().getLong(pointStringToUse);
                        }
                    };
                    HivePlayer parent = api.getPlayer();
                    ChatColor rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();

                    points.add(api.getPoints());

                    RankEnum rank = game.equalsIgnoreCase("bed") ? (BEDRank.isNo1(uuid, api.getPoints()) ? BEDRank.ZZZZZZ : BEDRank.getRank(api.getPoints())) : (game.equalsIgnoreCase("sgn") ? SGNRank.getRank(api.getPoints()) : null);
                    if (rank == null) {
                        rank = (RankEnum) enumToUse.getMethod("getFromDisplay", String.class).invoke(null, api.getSource().getString("title"));
                    }

                    title.add(rank.getTotalDisplay());
                    name.add(rankColor + display);
                } catch (Exception ignored) { }
            }

            APIUtils.concurrentSort(points, points, title, name);

            The5zigAPI.getAPI().messagePlayer("\n"
                    + "    §7§m                                                                                    ");
            for (int i = 0; i < name.size(); i++) {
                try {
                    if (points.get(i) != 0) {
                        if (title.get(i).equals("§a§lMaster §e§lof §b§lDisguise")) {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + "§a§l" + Log.df(points.get(i)) + "§7 - " + title.get(i) + "§r " + name.get(i));
                        } else {
                            The5zigAPI.getAPI().messagePlayer(
                                    Log.info + title.get(i).replace(ChatColor.stripColor(title.get(i)), "")
                                            + Log.df(points.get(i)) + "§7 - " + title.get(i) + "§r " + name.get(i));
                        }

                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            The5zigAPI.getAPI()
                    .messagePlayer(Log.info + game.toUpperCase() + " Playerstats: §b" + name.size() + "P / "
                            + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                            + Log.df(APIUtils.average(points.toArray())) + " Average");
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");
        }).start();

        return true;
    }
}
