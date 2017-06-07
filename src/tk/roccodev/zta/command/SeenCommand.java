package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.notes.NotesManager;

public class SeenCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "seen";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/seen"};
		return aliases;
	}

	@Override
	public boolean execute(String[] args) {
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(args.length == 1){
			
			String ign = args[0];
			new Thread(new Runnable(){
				@Override
				public void run(){
					The5zigAPI.getAPI().messagePlayer(Log.info + HiveAPI.getRankColor(HiveAPI.getNetworkRank(ign)) + ign + "§e was last seen on§a " + HiveAPI.getLastLogout(ign) + "§e.");
				}
			}).start();
			
			
			
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /seen [player]");
		}
		
		return true;
	}

	

}
