package tk.roccodev.zta.command;

import java.util.Calendar;

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
					if(!HiveAPI.getPlayerLocation(ign).equals("the Land of Nods!")){
						The5zigAPI.getAPI().messagePlayer(Log.info + HiveAPI.getRankColor(HiveAPI.getNetworkRank(ign)) + HiveAPI.getName(ign) + "§e is online and in §6" + HiveAPI.getPlayerLocation(ign));
					}
					else{
						Calendar lastSeen = Calendar.getInstance();;
						lastSeen.setTimeInMillis(HiveAPI.getLastLogout(ign).getTime());
					
						String minute = Integer.toString(lastSeen.get(lastSeen.MINUTE));
						if(lastSeen.get(lastSeen.MINUTE) < 10){
							minute = "0" + minute;
						}
						String hour = Integer.toString(lastSeen.get(lastSeen.HOUR_OF_DAY));
						if(lastSeen.get(lastSeen.HOUR_OF_DAY) < 10){
							hour = "0" + hour;
						}
					// Never again
						The5zigAPI.getAPI().messagePlayer(Log.info + HiveAPI.getRankColor(HiveAPI.getNetworkRank(ign)) + HiveAPI.getName(ign) + "§e was last seen on §6" + lastSeen.get(lastSeen.DAY_OF_MONTH) + "." + lastSeen.get(lastSeen.MONTH) + "." + lastSeen.get(lastSeen.YEAR) + " " + hour + ":" + minute
							+ "§e (§6" + HiveAPI.getTimeAgo(lastSeen.getTimeInMillis()) + ".§e)");
					}
				}
			}).start();
			
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /seen [player]");
		}
		
		return true;
	}

	

}
