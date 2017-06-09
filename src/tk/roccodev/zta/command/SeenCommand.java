package tk.roccodev.zta.command;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.HiveAPI;

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
					Date now = new Date();
					Date lastSeen = HiveAPI.getLastLogout(ign);
					long diff = now.getTime() - lastSeen.getTime();
					
					
					The5zigAPI.getAPI().messagePlayer(Log.info + HiveAPI.getRankColor(HiveAPI.getNetworkRank(ign)) + ign + "§e was last seen on§a " + lastSeen + "§e (§a" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days ago§e).");
				}
			}).start();
			
			
			
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /seen [player]");
		}
		
		return true;
	}

	

}
