package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.autovote.AutovoteUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoVoteCommand implements Command {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "autovote";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "/autovote", "/avote" };
	}

	@Override
	public boolean execute(String[] args) {

		if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive))
			return false;

		// Format would be /autovote add dr_throwback
		if (args.length == 2) {
			String mode = args[0];
			if (mode.equalsIgnoreCase("add")) {
				String map = args[1];
				String[] data = map.split("_");
				String gamemode = data[0]; // ex: dr
				List<String> mapStr = new ArrayList<String>(Arrays.asList(data));
				mapStr.remove(0);

				StringBuilder sb = new StringBuilder();
				String mapString = "";
				for (String s : mapStr) {
					sb.append(s + "_");
				}
				mapString = sb.deleteCharAt(sb.length() - 1).toString().trim();

				List<String> maps = (List<String>) AutovoteUtils.get(gamemode.toLowerCase());
				maps.add(mapString.toUpperCase());
				AutovoteUtils.set(gamemode.toLowerCase(), maps);
				AutovoteUtils.dump();
				The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");

			} else if (mode.equalsIgnoreCase("list")) {
				String game = args[1];

				The5zigAPI.getAPI().messagePlayer(Log.info + "Maps for mode " + game.toUpperCase());
				for (String s : AutovoteUtils.getMapsForMode(game.toLowerCase())) {
					The5zigAPI.getAPI().messagePlayer("§7 - §b" + s);
				}

			} else if (mode.equalsIgnoreCase("remove")) {
				String map = args[1];
				String[] data = map.split("_");
				String gamemode = data[0]; // ex: dr

				List<String> mapStr = new ArrayList<String>(Arrays.asList(data));
				mapStr.remove(0);
				StringBuilder sb = new StringBuilder();
				String mapString = "";
				for (String s : mapStr) {
					sb.append(s + "_");
				}
				mapString = sb.deleteCharAt(sb.length() - 1).toString().trim();

				List<String> maps = (List<String>) AutovoteUtils.getMapsForMode(gamemode.toLowerCase());
				maps.remove(mapString.trim().toUpperCase());
				AutovoteUtils.set(gamemode.toLowerCase(), maps);
				AutovoteUtils.dump();
				The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");

			}

		} else {
			The5zigAPI.getAPI().messagePlayer(
					Log.info + "Usage:" + "\n" + "§b/autovote add mode_map §7- §3Adds a favorite map to a gamemode" + "\n"
							+ "§b/autovote list mode §7- §3Lists your favorite maps for that gamemode" + "\n"
							+ "§b/autovote remove mode_map §7- §3Removes a favorite map from a gamemode");
		}

		return true;
	}

}
