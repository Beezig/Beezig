package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.stuff.dr.ClosestToWR;

public class ClosestToWRCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "closestwr";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/cwr", "/bestmap"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		The5zigAPI.getAPI().messagePlayer(Log.info + "Checking...");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ClosestToWR.fetch(args.length == 0 ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "") : args[0], args.length > 1);

					The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
				}
				catch(Exception e){
					The5zigAPI.getAPI().messagePlayer(Log.error + "An error occured.");
				}

			}
		}).start();
		
		return true;
	}

	

	

}
