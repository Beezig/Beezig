package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONObject;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHIDE;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BlockstatsCommand implements Command{

	@Override
	public String getName() {
		return "blockstats";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/blockstats", "/bs"};
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length == 0) {
			The5zigAPI.getAPI().messagePlayer(Log.info + "Blockstats Usage:");
			The5zigAPI.getAPI().messagePlayer(ChatColor.YELLOW + "/bs list (player) - Returns the levels of all blocks by the player");
		}
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("list") && args.length < 3) {
				new Thread(() -> {
					String ign;
					if (args.length == 1) {
						ign = The5zigAPI.getAPI().getGameProfile().getName();
					} else {
						ign = args[1];
					}
					The5zigAPI.getLogger().info("Init API");
					ApiHIDE api = new ApiHIDE(ign);
					JSONObject blockExp = api.getBlockExperience();
					JSONObject rawBlockEx = api.getRawBlockExperience();
					The5zigAPI.getAPI().messagePlayer("\n§3Blockstats of §b" + api.getParentMode().getNetworkRankColor() +  api.getParentMode().getCorrectName() + "§3:");

					Map<String, Integer> blockMap = new HashMap<>();
					for (Object block : blockExp.keySet()) {
						blockMap.put(block.toString(), Integer.valueOf(blockExp.get(block).toString()));
					}
					The5zigAPI.getLogger().info("blockMap size: " + blockMap.size());
					Object[] a = blockMap.entrySet().toArray();
					Arrays.sort(a, (Comparator) (o1, o2) -> ((Map.Entry<String, Integer>) o2).getValue().compareTo(((Map.Entry<String, Integer>) o1).getValue()));
					//Magic right there
					The5zigAPI.getLogger().info("magic happened");
					for (Object e : a) {
						String block = ((Map.Entry<String, Integer>) e).getKey();
						Integer level = Integer.valueOf((((Map.Entry<String, Integer>) e).getValue()).toString());

						String blockName = APIUtils.capitalize(block.replaceAll("_", " "));
						ChatColor levelColor = APIUtils.getLevelColorHIDE(Integer.valueOf(blockExp.get(block).toString()));
						int rawExperience = Integer.valueOf(rawBlockEx.get(block).toString());

						if (blockName.equals("Wood")) {
							blockName = "Oak Planks";
						}
						if (blockName.equals("Wood:1")) {
							//no idea
							blockName = "Wood";
						}

						The5zigAPI.getAPI().messagePlayer("§3" + blockName + ": " + levelColor + "" + level + APIUtils.getNextPecentHIDE(rawExperience, level));
					}

				}).start();
			}
		}

		return true;
	}
}
