package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.hiveapi.stuff.RankEnum;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.bp.BPRank;
import tk.roccodev.beezig.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRRank;
import tk.roccodev.beezig.hiveapi.stuff.grav.GRAVRank;
import tk.roccodev.beezig.hiveapi.stuff.hide.HIDERank;
import tk.roccodev.beezig.hiveapi.stuff.lab.LABRank;
import tk.roccodev.beezig.hiveapi.stuff.mimv.MIMVRank;
import tk.roccodev.beezig.hiveapi.stuff.sgn.SGNRank;
import tk.roccodev.beezig.hiveapi.stuff.sky.SKYRank;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.beezig.hiveapi.wrapper.APIGameMode;
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
        if (args.length == 0)
            game = ActiveGame.current();
        else
            game = args[0];

        String rankName = game.equalsIgnoreCase("gnt") || game.equals("gntm") ? "Giant" : game.toUpperCase();
        String rankNamePkg = game.equalsIgnoreCase("gnt") || game.equals("gntm") ? "gnt" : game;

        long startT = System.currentTimeMillis();


        new Thread(() -> {
            Class enumToUse = null;
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
                    APIGameMode api = new APIGameMode(npi.getGameProfile().getName(), npi.getGameProfile().getId().toString().replace("-", "")) {
                        @Override
                        public String getShortcode() {
                            return game.toUpperCase();
                        }

                        @Override
                        public long getPoints() {

                            return (long) object(pointStringToUse);
                        }
                    };
                    ApiHiveGlobal apiHIVE = api.getParentMode();
                    points.add(api.getPoints());

                    RankEnum rank = game.equalsIgnoreCase("bed") ? (BEDRank.isNo1(api.getParentMode().getCorrectName(), api.getPoints()) ? BEDRank.ZZZZZZ : BEDRank.getRank(api.getPoints())) : (game.equalsIgnoreCase("sgn") ? SGNRank.getRank(api.getPoints()) : null);
                    if(rank == null) {
                        rank = (RankEnum) enumToUse.getMethod("getFromDisplay", String.class).invoke(null, api.getTitle());
                    }

                    title.add(rank.getTotalDisplay());
                    name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
                } catch (Exception e) {
                  //  e.printStackTrace();
                }
            }

            APIUtils.concurrentSort(points, points, title, name);

            The5zigAPI.getAPI().messagePlayer("\n"
                    + "    §7§m                                                                                    ");
            for (int i = 0; i < name.size(); i++) {
                try {
                    if (points.get(i) != 0) {
                        The5zigAPI.getAPI().messagePlayer(
                                Log.info + title.get(i).replace(ChatColor.stripColor(title.get(i)), "")
                                        + points.get(i) + "§7 - " + title.get(i) + "§r " + name.get(i));
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            The5zigAPI.getAPI()
                    .messagePlayer(Log.info + game.toUpperCase() + " Playerstats: §b" + name.size() + "P / "
                            + ((System.currentTimeMillis() - startT) / 1000) + "s / "
                            + APIUtils.average(points.toArray()) + " Average");
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");
        }).start();

        return true;
    }
}
