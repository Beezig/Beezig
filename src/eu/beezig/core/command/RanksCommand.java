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
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
import eu.beezig.core.hiveapi.stuff.bp.BPRank;
import eu.beezig.core.hiveapi.stuff.dr.DRRank;
import eu.beezig.core.hiveapi.stuff.grav.GRAVRank;
import eu.beezig.core.hiveapi.stuff.hide.HIDERank;
import eu.beezig.core.hiveapi.stuff.sgn.SGNRank;
import eu.beezig.core.hiveapi.stuff.sky.SKYRank;
import eu.beezig.core.hiveapi.stuff.timv.TIMVRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;


public class RanksCommand implements Command {
    @Override
    public String getName() {
        return "ranks";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/ranks"};
    }

    @Override
    public boolean execute(String[] args) {

        String game;
        if (args.length == 0) {
            game = ActiveGame.current();
        } else game = args[0];

        if (game.equalsIgnoreCase("timv")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (TIMVRank timvRank : TIMVRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + timvRank.getTotalDisplay().replaceAll("➊ ","") + " §7- " + timvRank.getTotalDisplay().replaceAll(timvRank.getDisplay(), "") + Log.df(timvRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("bed")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");
                for (BEDRank bedRank : BEDRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + "" + bedRank.getName() + "" + "§7 - " + "" + bedRank.getName().replaceAll(ChatColor.stripColor(bedRank.getName()), "") + "" + Log.df(bedRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("dr")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (DRRank drRank : DRRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + drRank.getTotalDisplay() + " §7- " + drRank.getTotalDisplay().replaceAll(drRank.getDisplay(), "") + Log.df(drRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("hide")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (HIDERank hideRank : HIDERank.values()) {
                    if (hideRank == HIDERank.MASTER_OF_DISGUISE) {
                        The5zigAPI.getAPI().messagePlayer(Log.info + hideRank.getTotalDisplay() + " §7- §e§l" + hideRank.getStart());
                    } else {
                        The5zigAPI.getAPI().messagePlayer(Log.info + hideRank.getTotalDisplay() + " §7- " + hideRank.getTotalDisplay().replaceAll(hideRank.getDisplay(), "") + Log.df(hideRank.getStart()));
                    }

                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("sky")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (SKYRank skyRank : SKYRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + skyRank.getTotalDisplay() + " §7- " + skyRank.getTotalDisplay().replaceAll(skyRank.getDisplay(), "") + Log.df(skyRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("grav")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (GRAVRank gravRank : GRAVRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + gravRank.getTotalDisplay() + " §7- " + gravRank.getTotalDisplay().replaceAll(gravRank.getDisplay(), "") + Log.df(gravRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("bp")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (BPRank bpRank : BPRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + bpRank.getTotalDisplay() + " §7- " + bpRank.getTotalDisplay().replaceAll(bpRank.getDisplay(), "") + Log.df(bpRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.equalsIgnoreCase("sgn")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (SGNRank sgnRank : SGNRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + sgnRank.getTotalDisplay() + " §7- " + sgnRank.getTotalDisplay().replaceAll(sgnRank.getDisplay(), "") + Log.df(sgnRank.getRequiredPoints()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }

        return true;
    }
}
