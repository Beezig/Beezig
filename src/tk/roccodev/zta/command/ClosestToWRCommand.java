package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.stuff.dr.ClosestToWR;
import tk.roccodev.zta.notes.NotesManager;

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
				ClosestToWR.fetch(args.length == 0 ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "") : args[0], args.length > 1 );
				
			}
		}).start();
		
		return true;
	}

	

	

}
