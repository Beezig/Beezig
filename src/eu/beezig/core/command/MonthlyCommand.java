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
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.MonthliesReady;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.MonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bed.BedMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bp.BpMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.cai.CaiMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.dr.DrMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.gnt.GntMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.gnt.GntmMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.hide.HideMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.sky.SkyMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.timv.TimvMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MonthlyCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "monthly";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/monthly"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length == 1 || args.length == 2) {
            DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);

            DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
            df1f.setMinimumFractionDigits(1);
            df1f.setMaximumFractionDigits(1);
            try {

                new Thread(() -> {
                    String mode = args.length == 2 ? args[1].toLowerCase() : ActiveGame.current().toLowerCase();
                    String unit = mode.equals("timv") ? "karma." : "points.";

                    String str = "§b{u}§3 is §b#{#}§3 with §b{p} §3" + unit;
                    switch(mode) {
                        case "bed":
                            BedMonthlyProfile bed =
                                    (BedMonthlyProfile) getProfile(new BedStats(null), args[0]);
                            if(!assertNotNull(bed)) return;

                            double bedKD = (double) bed.getKills() / (double) bed.getDeaths();
                            double bedWG = (double) bed.getVictories() / (double) bed.getGamesPlayed() * 100D;

                            str = str.replace("{u}", bed.getUsername())
                                    .replace("{#}", bed.getPlace() + "")
                                    .replace("{p}", Log.df(bed.getPoints()))
                                    + " (K/D: §b" + df.format(bedKD) + " §3| W/G: §b" + df1f.format(bedWG) + "%§3)";
                            break;
                        case "cai":
                            CaiMonthlyProfile cai =
                                    (CaiMonthlyProfile) getProfile(new CaiStats(null), args[0]);
                            if(!assertNotNull(cai)) return;

                            double caiWG = (double) cai.getVictories() / (double) cai.getGamesPlayed() * 100D;

                            str = str.replace("{u}", cai.getUsername())
                                    .replace("{#}", cai.getPlace() + "")
                                    .replace("{p}", Log.df(cai.getPoints()))
                                    + " (W/G: §b" + df1f.format(caiWG) + "%§3)";
                            break;
                        case "timv":
                            TimvMonthlyProfile timv =
                                    (TimvMonthlyProfile) getProfile(new TimvStats(null), args[0]);
                            if(!assertNotNull(timv)) return;

                            double timvKR = (double) timv.getPoints() / (double) timv.getRolePoints();
                            double timvTR = (double) timv.getTraitorPoints() / (double) timv.getRolePoints() * 100D;

                            str = str.replace("{u}", timv.getUsername())
                                    .replace("{#}", timv.getPlace() + "")
                                    .replace("{p}", Log.df(timv.getPoints()))
                                    + " (K/R:§b " + df.format(timvKR) + "§3 | TShare: §b" + df1f.format(timvTR) + "%§3)";
                            break;
                        case "dr":
                            DrMonthlyProfile dr =
                                    (DrMonthlyProfile) getProfile(new DrStats(null), args[0]);
                            if(!assertNotNull(dr)) return;

                            double drWG = (double) dr.getVictories() / (double) dr.getGamesPlayed() * 100D;

                            str = str.replace("{u}", dr.getUsername())
                                    .replace("{#}", dr.getPlace() + "")
                                    .replace("{p}", Log.df(dr.getPoints()))
                                    + " (W/G:§b " + df1f.format(drWG) + "%§3)";
                            break;
                        case "sky":
                            SkyMonthlyProfile sky =
                                    (SkyMonthlyProfile) getProfile(new SkyStats(null), args[0]);
                            if(!assertNotNull(sky)) return;

                            double skyKD = (double) sky.getKills() / (double) sky.getDeaths();
                            double skyWG = (double) sky.getVictories() / (double) sky.getGamesPlayed() * 100D;

                            str = str.replace("{u}", sky.getUsername())
                                    .replace("{#}", sky.getPlace() + "")
                                    .replace("{p}", Log.df(sky.getPoints()))
                                    + " (K/D:§b " + df.format(skyKD) + "§3 | W/G: §b" + df1f.format(skyWG) + "%§3)";
                            break;
                        case "bp":
                            BpMonthlyProfile bp =
                                    (BpMonthlyProfile) getProfile(new BpStats(null), args[0]);
                            if(!assertNotNull(bp)) return;

                            double bpWG = (double) bp.getVictories() / (double) bp.getGamesPlayed() * 100D;

                            str = str.replace("{u}", bp.getUsername())
                                    .replace("{#}", bp.getPlace() + "")
                                    .replace("{p}", Log.df(bp.getPoints()))
                                    + " (W/G:§b " + df1f.format(bpWG) + "%§3)";
                            break;
                        case "gnt":
                            GntMonthlyProfile gnt =
                                    (GntMonthlyProfile) getProfile(new GntStats(null), args[0]);
                            if(!assertNotNull(gnt)) return;

                            double gntKD = (double) gnt.getKills() / (double) gnt.getDeaths();
                            double gntWG = (double) gnt.getVictories() / (double) gnt.getGamesPlayed() * 100D;

                            str = str.replace("{u}", gnt.getUsername())
                                    .replace("{#}", gnt.getPlace() + "")
                                    .replace("{p}", Log.df(gnt.getPoints()))
                                    + " (K/D: §b" + df.format(gntKD) + "§3 | W/G: §b" + df1f.format(gntWG) + "%§3)";
                            break;
                        case "gntm":
                            GntmMonthlyProfile gntm =
                                    (GntmMonthlyProfile) getProfile(new GntmStats(null), args[0]);
                            if(!assertNotNull(gntm)) return;

                            double gntmKD = (double) gntm.getKills() / (double) gntm.getDeaths();
                            double gntmWG = (double) gntm.getVictories() / (double) gntm.getGamesPlayed() * 100D;

                            str = str.replace("{u}", gntm.getUsername())
                                    .replace("{#}", gntm.getPlace() + "")
                                    .replace("{p}", Log.df(gntm.getPoints()))
                                    + " (K/D: §b" + df.format(gntmKD) + " §3| W/G: §b" + df1f.format(gntmWG) + "%§3)";
                            break;
                        case "hide":
                            HideMonthlyProfile hide =
                                    (HideMonthlyProfile) getProfile(new HideStats(null), args[0]);
                            if(!assertNotNull(hide)) return;

                            str = str.replace("{u}", hide.getUsername())
                                    .replace("{#}", hide.getPlace() + "")
                                    .replace("{p}", Log.df(hide.getPoints()))
                                    + " (Victories: §b" + Log.df(hide.getVictories()) + "§3)";
                            break;
                        default:
                            The5zigAPI.getAPI().messagePlayer(Log.error + "Mode not found.");
                            return;
                    }

                    The5zigAPI.getAPI().messagePlayer(Log.info + str);

                }).start();
            } catch (Exception e) {
                The5zigAPI.getAPI().messagePlayer(
                        Log.info + "Usage: /monthly [position] | Use /records to find out a players' rank.");
                return true;
            }
        } else {
            The5zigAPI.getAPI()
                    .messagePlayer(Log.info + "Usage: /monthly [position] | Use /records to find out a players' rank.");
        }
        return true;
    }

    private boolean assertNotNull(Object o) {
        if(o == null) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Monthly profile not found.");
            return false;
        }
        return true;
    }

    private MonthlyProfile getProfile(MonthliesReady stats, String firstArg) {
        if(firstArg.matches("-?\\d+"))
            return stats.getMonthlyProfile(Integer.parseInt(firstArg));
        else return stats.getMonthlyProfile(UsernameToUuid.getUUID(firstArg));
    }

}
