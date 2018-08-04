package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.stuff.RankEnum;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.sgn.SGNRank;
import tk.roccodev.beezig.hiveapi.wrapper.APIGameMode;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiHiveGlobal;

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
        long indexEnd = Long.valueOf(args[1]);
        long indexStart = humanStart - 1L;


        String rankName = game.equalsIgnoreCase("gnt") || game.equals("gntm") ? "Giant" : game.toUpperCase();
        String rankNamePkg = game.equalsIgnoreCase("gnt") || game.equals("gntm") ? "gnt" : game;

        new Thread(() -> {
            Class enumToUse = null;
            try {
                enumToUse = Class.forName("tk.roccodev.beezig.hiveapi.stuff." + rankNamePkg.toLowerCase() + "." + rankName + "Rank");
            } catch (ClassNotFoundException ignored) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Specified mode not found.");
                return;
            }
            long startT = System.currentTimeMillis();

            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            JSONArray data = HiveAPI.getLeaderboardData(game, indexStart, indexEnd);

            List<Long> points = new ArrayList<>();
            List<String> title = new ArrayList<>();
            List<String> name = new ArrayList<>();
            String pointStringToUse = game.equalsIgnoreCase("grav") ||
                    game.equalsIgnoreCase("hide")
                    || game.equalsIgnoreCase("cai") ? "points" : (game.equalsIgnoreCase("timv")
            ? "karma" :"total_points");
            for (int i = 0; i < (indexEnd - humanStart + 1L); i++) {
                try {
                    APIGameMode api = new APIGameMode(((JSONObject) data.get(i)).get("username").toString(),
                            ((JSONObject) data.get(i)).get("UUID").toString()) {
                        @Override
                        public String getShortcode() {
                            return game.toUpperCase();
                        }

                        @Override
                        public long getPoints() {

                            return (long) object(pointStringToUse);
                        }

                    };
                    long pts = (long) ((JSONObject) data.get(i)).get(pointStringToUse);

                    ApiHiveGlobal apiHIVE = new ApiHiveGlobal(((JSONObject) data.get(i)).get("username").toString(),
                            ((JSONObject) data.get(i)).get("UUID").toString());

                    points.add(pts);

                    RankEnum rank = game.equalsIgnoreCase("bed") ? ((humanStart + i == 1) ? BEDRank.ZZZZZZ : BEDRank.getRank(pts)) : (game.equalsIgnoreCase("sgn") ? SGNRank.getRank(pts) : null);
                    if(rank == null) {
                        rank = (RankEnum) enumToUse.getMethod("getFromDisplay", String.class).invoke(null, api.getTitle());
                    }

                    title.add(rank.getTotalDisplay());
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
                        if (points.get(i) != 0) {
                            if(title.get(i).equals("§a§lMaster §e§lof §b§lDisguise")) {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                                + "§a§l"
                                                + points.get(i) + "§7 - " + title.get(i) + "§r " + name.get(i));
                            }
                            else {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                                + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                                + points.get(i) + "§7 - " + title.get(i) + "§r " + name.get(i));
                            }

                        }

                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            The5zigAPI.getAPI().messagePlayer(Log.info + game.toUpperCase() + " Leaderboards: §b" + name.size() + "P / "
                    + ((System.currentTimeMillis() - startT) / 1000) + "s / " + "#" + humanStart + "-" + indexEnd);
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");
        }).start();


        return true;
    }
}
