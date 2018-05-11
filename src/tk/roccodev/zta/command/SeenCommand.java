package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;

import java.util.Calendar;

public class SeenCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "seen";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/seen"};
	}

	@Override
	public boolean execute(String[] args) {
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(args.length == 1){
			
			String ign = args[0];
			ApiHiveGlobal api = new ApiHiveGlobal(ign);
			new Thread(new Runnable(){
				@Override
				public void run(){
					if(!api.getPlayerLocation().equals("the Land of Nods!")){
						The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + api.getCorrectName() + "§3 is online and in §b" + api.getPlayerLocation());
					}
					else{
						Calendar lastSeen = Calendar.getInstance();
						lastSeen.setTimeInMillis(api.getLastLogout()*1000);
					
						String minute = Integer.toString(lastSeen.get(lastSeen.MINUTE));
						if(lastSeen.get(lastSeen.MINUTE) < 10){
							minute = "0" + minute;
						}
						String hour = Integer.toString(lastSeen.get(lastSeen.HOUR_OF_DAY));
						if(lastSeen.get(lastSeen.HOUR_OF_DAY) < 10){
							hour = "0" + hour;
						}
					// Never again
						The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + api.getCorrectName() + "§3 was last seen on §b" + lastSeen.get(lastSeen.DAY_OF_MONTH) + "." + (lastSeen.get(lastSeen.MONTH) + 1) + "." + lastSeen.get(lastSeen.YEAR) + " " + hour + ":" + minute
							+ "§b (§b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()) + ".§b)");
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
