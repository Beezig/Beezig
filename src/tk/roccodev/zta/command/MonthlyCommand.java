package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.stuff.bed.MonthlyPlayer;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiBED;

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
		return new String[] { "/monthly" };
	}

	@Override
	public boolean execute(String[] args) {

		if (args.length == 1 || args.length == 2) {
			try {

				new Thread(() -> {
					String mode = args.length == 2 ? args[1] : ActiveGame.current().toLowerCase();
					String unit = null;
					if (mode.equals("bed")) {
						if (args[0].matches("-?\\d+")) {
							int place = Integer.parseInt(args[0]);
							MonthlyPlayer monthly = ApiBED.getMonthlyStatusByPlace(place);
							if(monthly == null) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "No monthly profile found.");
								return;
							}
							double kd = (double)monthly.getKills() / (double)monthly.getDeaths();
							double wg = (double)monthly.getVictories() / (double)monthly.getPlayed() * 100D;

							DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
							df.setMinimumFractionDigits(2);
							df.setMaximumFractionDigits(2);

							DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
							df1f.setMinimumFractionDigits(1);
							df1f.setMaximumFractionDigits(1);

							The5zigAPI.getAPI()
									.messagePlayer(Log.info + "§b#" + monthly.getPlace()
											+ " §3is §b" + monthly.getUsername() + " §3with §b" + monthly.getPoints()
											+ " §3points. (K/D: §b" + df.format(kd) + " §3| W/G: §b" + df1f.format(wg) + "%§3)");

						} else {
							ApiBED api = new ApiBED(args[0]);
							MonthlyPlayer monthly = api.getMonthlyStatus();
							if (monthly == null) {
								The5zigAPI.getAPI().messagePlayer(Log.error + "No monthly profile found.");
								return;
							}
							double kd = (double)monthly.getKills() / (double)monthly.getDeaths();
							double wg = (double)monthly.getVictories() / (double)monthly.getPlayed() * 100D;

							DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
							df.setMinimumFractionDigits(2);
							df.setMaximumFractionDigits(2);

							DecimalFormat df1f = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
							df1f.setMinimumFractionDigits(1);
							df1f.setMaximumFractionDigits(1);

							The5zigAPI.getAPI()
									.messagePlayer(Log.info + "§b" + api.getParentMode().getCorrectName()
											+ " §3is §b#" + monthly.getPlace() + " §3with §b" + monthly.getPoints()
											+ " §3points. (K/D: §b" + df.format(kd) + " §3| W/G: §b" + df1f.format(wg) + "%§3)");
						}
						return;
					}
					int index = Integer.parseInt(args[0]) - 1;
					if (mode.equals("timv")) {
						mode = "TIMV";
						unit = "karma.";
					} else if (mode.equals("dr")) {
						mode = "DR";
						unit = "points.";
					}
					String[] data = HiveAPI.getMonthlyLeaderboardsPlayerInfo(index, mode.toUpperCase()).split(",");
					switch (mode) {

					case "DR":
						The5zigAPI.getAPI()
								.messagePlayer(Log.info + "§B#" + (index + 1) + "§3 is §B" + data[0] + "§3 with §B"
										+ data[1] + " §3" + unit + " (PpG: §B" + data[2] + " §3| W/L: §B" + data[3]
										+ "%§3)");
						break;

					case "TIMV":
						The5zigAPI.getAPI()
								.messagePlayer(Log.info + "§B#" + (index + 1) + "§3 is §B" + data[0] + "§3 with §B"
										+ data[1] + " §3" + unit + " (K/R: §B" + data[2] + " §3| T/R: " + data[3]
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
