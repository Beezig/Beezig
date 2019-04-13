/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
import eu.beezig.core.hiveapi.stuff.sgn.SGNRank;
import eu.beezig.core.hiveapi.stuff.timv.TIMVRank;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.game.Game;
import pw.roccodev.beezig.hiveapi.wrapper.game.leaderboard.LeaderboardPlace;
import pw.roccodev.beezig.hiveapi.wrapper.player.GameStats;

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


        String rankName = game.equalsIgnoreCase("gnt") || game.equalsIgnoreCase("gntm") ? "Giant" : game.toUpperCase();
        String rankNamePkg = game.equalsIgnoreCase("gnt") || game.equalsIgnoreCase("gntm") ? "gnt" : game;

        new Thread(() -> {
            Class enumToUse;
            try {
                enumToUse = Class.forName("eu.beezig.core.hiveapi.stuff." + rankNamePkg.toLowerCase() + "." + rankName + "Rank");
            } catch (ClassNotFoundException ignored) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Specified mode not found.");
                return;
            }
            long startT = System.currentTimeMillis();

            The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
            List<LeaderboardPlace> data = new Game(game.toUpperCase()).getLeaderboard((int) indexStart, (int) indexEnd).getPlayers();


            List<Long> points = new ArrayList<>();
            List<String> title = new ArrayList<>();
            List<String> name = new ArrayList<>();

            for (LeaderboardPlace place : data) {
                try {
                    GameStats stats = new GameStats((String) place.get("UUID"), game.toUpperCase());
                    String ptsToUse = place.containsKey("points") ? "points" :
                            (place.containsKey("total_points") ? "total_points" : "karma");

                    long pts = (long) place.get(ptsToUse);

                    points.add(pts);

                    RankEnum rank = game.equalsIgnoreCase("bed") ? ((place.getHumanPlace() == 1) ? BEDRank.ZZZZZZ : BEDRank.getRank(pts)) : (game.equalsIgnoreCase("sgn") ? SGNRank.getRank(pts) : null);
                    if (rank == null) {
                        rank = (RankEnum) enumToUse.getMethod("getFromDisplay", String.class).invoke(null, stats.getSource().getString("title"));
                    }

                    if (game.equalsIgnoreCase("timv"))
                        title.add(((TIMVRank) rank).getTotalDisplay(pts));
                    else
                        title.add(rank.getTotalDisplay());
                    name.add(NetworkRank.fromDisplay(stats.getPlayer().getRank().getHumanName()).getColor() +
                            place.get("username").toString());


                } catch (Exception ignored) {}

            }

            The5zigAPI.getAPI().messagePlayer("\n"
                    + "    §7§m                                                                                    ");
            for (int i = 0; i < name.size(); i++) {
                try {
                    if (points.get(i) != 0) {
                        if (points.get(i) != 0) {
                            if (title.get(i).equals("§a§lMaster §e§lof §b§lDisguise")) {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                                + "§a§l"
                                                + Log.df(points.get(i)) + "§7 - " + title.get(i) + "§r " + name.get(i));
                            } else {
                                The5zigAPI.getAPI()
                                        .messagePlayer(Log.info + "#§b" + (humanStart + i) + "§7 ▏ §3"
                                                + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "")
                                                + Log.df(points.get(i)) + "§7 - " + title.get(i) + "§r " + name.get(i));
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
