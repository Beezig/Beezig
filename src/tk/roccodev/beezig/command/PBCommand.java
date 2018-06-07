package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRMap;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiDR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PBCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "pb";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/pb"};
	}

	@Override
	public boolean execute(String[] args) {
		if(!(ActiveGame.is("dr"))) return false;
		if(args.length == 1 && DR.activeMap != null){
			
			String ign = args[0];
			
			new Thread(() -> {
				ApiDR api = new ApiDR(ign);
				The5zigAPI.getAPI().messagePlayer(Log.info + api.getParentMode().getNetworkRankColor() + api.getParentMode().getCorrectName() + "§3's Personal Best on map §b" + DR.activeMap.getDisplayName() + "§3 is §b" + api.getPersonalBest(DR.activeMap));
			}).start();
	
		}
		else {
			
			String ign = args[0];
			List<String> argsL = new ArrayList<>(Arrays.asList(args));
			argsL.remove(0);
			DRMap map = DR.mapsPool.get(String.join(" ", argsL).toLowerCase());
			
				
			new Thread(() -> {
				ApiDR api = new ApiDR(ign);
				The5zigAPI.getAPI().messagePlayer(Log.info + api.getParentMode().getNetworkRankColor() + api.getParentMode().getCorrectName() + "§3's Personal Best on map §b" + map.getDisplayName() + "§3 is §b" + api.getPersonalBest(map));
			}).start();
	
		}
			
		return true;
	}

	

}
