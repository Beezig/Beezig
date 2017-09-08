package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONObject;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHIDE;

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
		if(args.length == 0){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Blockstats Usage:");
			The5zigAPI.getAPI().messagePlayer("/bs list (player) - Returns the levels of all blocks by the player");
			The5zigAPI.getAPI().messagePlayer("Blockstats Usage:");
			The5zigAPI.getAPI().messagePlayer("Blockstats Usage:");
		}
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("list") && args.length < 3) {
				new Thread(() -> {
					String ign;
					if (args.length == 1) {
						ign = The5zigAPI.getAPI().getGameProfile().getName();
					} else {
						ign = args[1];
					}
					ApiHIDE api = new ApiHIDE(ign);
					JSONObject blockEx = api.getBlockExperience();
					JSONObject rawBlockEx = api.getRawBlockExperience();
					The5zigAPI.getAPI().messagePlayer("\n§3Blockstats of §b" + api.getParentMode().getCorrectName() + "§3:");

					for (Object block : blockEx.keySet()) {
						String blockName =  APIUtils.capitalize(block.toString().replaceAll("_", " "));
						ChatColor levelColor = APIUtils.getLevelColorHIDE(Integer.valueOf(blockEx.get(block).toString()));
						int rawExperience = Integer.valueOf(rawBlockEx.get(block).toString());
						int level = Integer.valueOf(blockEx.get(block).toString());
						if(blockName.equals("Wood")){
							blockName = "Oak Planks";
						}
						if(blockName.equals("Wood:1")){
							//no idea
							blockName = "Wood";
						}

						The5zigAPI.getAPI().messagePlayer("§3" + blockName + ": " + levelColor + "" + level + APIUtils.getNextPecentHIDE(rawExperience,level));
					}

				}).start();
			}
		}












		return true;
	}
}
