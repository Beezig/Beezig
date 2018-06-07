package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiHiveGlobal;

public class MessageOverlayCommand implements Command{
	
	public static String toggledName = "";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "msg";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/msg", "/tell"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		if(args.length == 0 && !toggledName.isEmpty()){
			toggledName = "";
			The5zigAPI.getAPI().messagePlayer(Log.info + "Now sending messages to normal chat.");
			return true;
		}
		if(args.length != 1 || !(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
	
		
		MessageOverlayCommand.toggledName = args[0];
		new Thread(() -> {
			ApiHiveGlobal api = new ApiHiveGlobal(toggledName);
			The5zigAPI.getAPI().messagePlayer(Log.info + "Now sending messages to " + api.getNetworkRankColor() + api.getCorrectName() + "§3.");
		}).start();
		
		
		return true;
	}

	

	

}
