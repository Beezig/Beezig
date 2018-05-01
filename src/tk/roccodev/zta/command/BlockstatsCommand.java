package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONObject;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHIDE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
					The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "§3Blockstats of §b" + api.getParentMode().getNetworkRankColor() +  api.getParentMode().getCorrectName() + "§3:");


					List<Object> blocks = new ArrayList<>(Arrays.asList(blockExp.keySet().toArray()));
					List<Long> levels = new ArrayList<>();
					List<Long> rawExp = new ArrayList<>();
					for(int i = 0; i < blocks.size(); i++){
						levels.add(Long.valueOf(blockExp.get(blocks.get(i)).toString()));
						rawExp.add(Long.valueOf(rawBlockEx.get(blocks.get(i)).toString()));
					}

					APIUtils.concurrentSort(rawExp, blocks,levels,rawExp);

					for(int i = blocks.size()-1; i != 0; i--){

						String blockName = APIUtils.capitalize(blocks.get(i).toString().replaceAll("_", " "));
						if (blockName.equals("Wood")) {
							blockName = "Oak Planks";
						}
						if (blockName.equals("Wood:1")) {
							blockName = "Wood";
						}
						if (blockName.equals("Wood:3")) {
							blockName = "Jungle Planks";
						}
						if (blockName.equals("Stone:6")) {
							blockName = "Polished Andesite";
						}
						if (blockName.equals("Prismarine:2")) {
							blockName = "Dark Prismarine";
						}

						The5zigAPI.getAPI().messagePlayer("§3" + blockName + ": " + APIUtils.getLevelColorHIDE(levels.get(i).intValue()) + "" + levels.get(i) + APIUtils.getNextPecentHIDE(rawExp.get(i).intValue(), levels.get(i).intValue()));
					}
					The5zigAPI.getAPI().messagePlayer("");
				}).start();
			}
		}

		return true;
	}
}
