package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.games.BED;
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
import eu.beezig.core.hiveapi.stuff.bp.BPRank;
import eu.beezig.core.hiveapi.stuff.cai.CAIRank;
import eu.beezig.core.hiveapi.stuff.dr.DRRank;
import eu.beezig.core.hiveapi.stuff.gnt.GiantRank;
import eu.beezig.core.hiveapi.stuff.grav.GRAVRank;
import eu.beezig.core.hiveapi.stuff.hide.HIDERank;
import eu.beezig.core.hiveapi.stuff.lab.LABRank;
import eu.beezig.core.hiveapi.stuff.mimv.MIMVRank;
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
        if (game.equalsIgnoreCase("cai")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (CAIRank caiRank : CAIRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + caiRank.getTotalDisplay() + " §7- " + caiRank.getTotalDisplay().replaceAll(caiRank.getDisplay(), "") + Log.df(caiRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }
        if (game.toLowerCase().startsWith("gnt")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (GiantRank gntRank : GiantRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + gntRank.getTotalDisplay() + " §7- " + gntRank.getTotalDisplay().replaceAll(gntRank.getDisplay(), "") + Log.df(gntRank.getStart()));
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
        if (game.equalsIgnoreCase("mimv")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (MIMVRank mimvRank : MIMVRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + mimvRank.getTotalDisplay() + " §7- " + mimvRank.getTotalDisplay().replaceAll(mimvRank.getDisplay(), "") + Log.df(mimvRank.getStart()));
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
        if (game.equalsIgnoreCase("lab")) {
            new Thread(() -> {
                The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

                for (LABRank sgnRank : LABRank.values()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + sgnRank.getTotalDisplay() + " §7- " + sgnRank.getTotalDisplay().replaceAll(sgnRank.getDisplay(), "") + Log.df(sgnRank.getStart()));
                }

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            }).start();
        }

        return true;
    }
}
