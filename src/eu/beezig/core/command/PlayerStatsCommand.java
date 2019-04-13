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

    private List<String> modes = Arrays.asList("bed", "sky", "timv", "dr", "bp", "cai", "dr", "grav",
            "gnt", "gntm", "hide", "lab", "mimv", "sgn");

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

        new Thread(() -> {
            String game;
            String earlyStat = null;
            if (args.length == 0)
                game = ActiveGame.current();
            else if(args.length == 1) {
                if(modes.contains(args[0])) {
                    game = args[0];
                }
                else {
                    game = ActiveGame.current();
                    earlyStat = args[0];
                }
            }
            else game = args[0];

            String rankName = game.equalsIgnoreCase("gnt") || game.equalsIgnoreCase("gntm") ? "Giant" : game.toUpperCase();
            String rankNamePkg = game.equalsIgnoreCase("gnt") || game.equalsIgnoreCase("gntm") ? "gnt" : game;

            long startT = System.currentTimeMillis();


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
                    MultiPsStats.getRecordsStatistic(game, earlyStat != null ? earlyStat
                            : (args.length < 2 ? pointsStr : args[1]));

            if (pointStringToUse == null) return;


            for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
                try {
                    String uuid = npi.getGameProfile().getId().toString().replace("-", "");
                    String display = npi.getDisplayName() == null ? npi.getGameProfile().getName() : npi.getDisplayName();
                    GameStats api = new GameStats(uuid, game.toUpperCase());
                    api.getSource().fetch();
                    HivePlayer parent = api.getPlayer();
                    ChatColor rankColor = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();

                    if(("Karma/Role Points".equalsIgnoreCase(pointStringToUse.getKey())
                    || "Traitor Share".equalsIgnoreCase(pointStringToUse.getKey()))
                            && api.getPoints() < 1000) continue;

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
                    .messagePlayer(Log.info + game.toUpperCase() + " " + pointStringToUse.getKey() + " Playerstats: §b" + name.size() + "P / "
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
        else if(args.length == 1 && modes.contains(ActiveGame.current().toLowerCase())) {
            List<String> everything = new ArrayList<>();
            everything.addAll(modes);
            everything.addAll(MultiPsStats.getAllPossibleValues(ActiveGame.current().toLowerCase()));
            return TabCompletionUtils.matching(args, everything);
        }
        return TabCompletionUtils.matching(args, modes);
    }
}
