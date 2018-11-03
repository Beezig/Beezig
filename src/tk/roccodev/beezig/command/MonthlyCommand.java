package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bed.BedMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.maxthat.dr.DrMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.maxthat.timv.TimvMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.BedStats;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.TimvStats;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;

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
                    String mode = args.length == 2 ? args[1] : ActiveGame.current().toLowerCase();
                    String unit = null;
                    if (mode.equals("bed")) {
                        if (args[0].matches("-?\\d+")) {
                            int place = Integer.parseInt(args[0]);
                            BedMonthlyProfile monthly = new BedStats(null).getMonthlyProfile(place);
                            if (monthly == null) {
                                The5zigAPI.getAPI().messagePlayer(Log.error + "No monthly profile found.");
                                return;
                            }
                            double kd = (double) monthly.getKills() / (double) monthly.getDeaths();
                            double wg = (double) monthly.getVictories() / (double) monthly.getGamesPlayed() * 100D;

                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "§b#" + monthly.getPlace()
                                            + " §3is §b" + monthly.getUsername() + " §3with §b" + monthly.getPoints()
                                            + " §3points. (K/D: §b" + df.format(kd) + " §3| W/G: §b" + df1f.format(wg) + "%§3)");

                        } else {
                            BedStats api = new BedStats(args[0], true);
                            BedMonthlyProfile monthly = api.getMonthlyProfile();
                            if (monthly == null) {
                                The5zigAPI.getAPI().messagePlayer(Log.error + "No monthly profile found.");
                                return;
                            }
                            double kd = (double) monthly.getKills() / (double) monthly.getDeaths();
                            double wg = (double) monthly.getVictories() / (double) monthly.getGamesPlayed() * 100D;


                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "§b" + monthly.getUsername()
                                            + " §3is §b#" + monthly.getPlace() + " §3with §b" + monthly.getPoints()
                                            + " §3points. (K/D: §b" + df.format(kd) + " §3| W/G: §b" + df1f.format(wg) + "%§3)");
                        }
                        return;
                    }
                    int index = Integer.parseInt(args[0]);
                    if (mode.equals("timv")) {
                        mode = "TIMV";
                        unit = "karma.";
                    } else if (mode.equals("dr")) {
                        mode = "DR";
                        unit = "points.";
                    }

                    switch (mode) {

                        case "DR":
                            DrMonthlyProfile profile = new DrStats(null).getMonthlyProfile(index);
                            double wg = (double) profile.getVictories() / (double) profile.getGamesPlayed() * 100D;
                            double ppg = (double) profile.getPoints() / (double) profile.getGamesPlayed();
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "§B#" + profile.getHumanPlace() + "§3 is §B" + profile.getUsername() + "§3 with §B"
                                            + profile.getPoints() + " §3" + unit + " (PpG: §B" + df.format(ppg) + " §3| W/L: §B" + df1f.format(wg)
                                            + "%§3)");
                            break;

                        case "TIMV":
                            TimvMonthlyProfile timvProfile = new TimvStats(null).getMonthlyProfile(index);
                            double kr = (double) timvProfile.getPoints() / (double) timvProfile.getRolePoints();
                            double tr = (double) timvProfile.getTraitorPoints() / (double) timvProfile.getRolePoints() * 100D;
                            The5zigAPI.getAPI()
                                    .messagePlayer(Log.info + "§B#" + timvProfile.getHumanPlace() + "§3 is §B" + timvProfile.getUsername() + "§3 with §B"
                                            + timvProfile.getPoints() + " §3" + unit + " (K/R: §B" + df.format(kr) + " §3| T/R: " + df1f.format(tr)
                                            + "%§3)");
                            break;

                    }
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

}
