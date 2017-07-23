package tk.roccodev.zta.command;

import java.util.ArrayList;
import java.util.List;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.settings.Setting;

public class SettingsCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "settings";
	}

	@Override
	public String[] getAliases() {
		String aliases[] = {"/settings"};
		return aliases;
	}

	@Override
	public boolean execute(String[] args) {
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(args.length == 0){ //List settings
			The5zigAPI.getAPI().messagePlayer(Log.info + "Settings:");
			for(Setting sett : Setting.values()){
				String todisplay = sett.getValue() ? "§aTrue" : "§cFalse";
				The5zigAPI.getAPI().messagePlayer("§e - " + sett.name() + " (" + todisplay + "§e) (§a" + sett.getBriefDescription() + "§e)");
			}
		}
		else if(args.length == 1){
		String setting = args[0];
		Setting sett = null;
		try{
			sett = Setting.valueOf(setting.toUpperCase());
		}
		catch(IllegalArgumentException e){
			The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid setting.");
		}
		String todisplay = sett.getValue() ? "§aTrue" : "§cFalse";
		The5zigAPI.getAPI().messagePlayer(Log.info + sett.name() + ": " + todisplay +"§e (§a" + sett.getBriefDescription() + "§e)");
		
		}
		else if(args.length == 2){
			String setting = args[0];
			String value = args[1];
			if(setting.equalsIgnoreCase("filter")){
				
				The5zigAPI.getAPI().messagePlayer(Log.info + "Filter results:");
				for(Setting sett : Setting.values()){
					if(sett.name().toUpperCase().contains(value.toUpperCase())){
						String todisplay = sett.getValue() ? "§aTrue" : "§cFalse";
						The5zigAPI.getAPI().messagePlayer("§e - " + sett.name() + " (" + todisplay + "§e) (§a" + sett.getBriefDescription() + "§e)");
					}
				}
				
				return true;
			}
			boolean b = Boolean.valueOf(value);
			Setting sett = null;
			try{
				sett = Setting.valueOf(setting.toUpperCase());
			}
			catch(IllegalArgumentException e){
				The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid setting.");
				return true;
			}
			sett.setValue(b);
			The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully updated setting.");
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage:");
			The5zigAPI.getAPI().messagePlayer("§e - /settings §a to list the settings");
			The5zigAPI.getAPI().messagePlayer("§e - /settings [setting] §a to get the value of a setting");
			The5zigAPI.getAPI().messagePlayer("§e - /settings [setting] [true/false] §a to set the value of a setting.");
		}
		return true;
	}

	

}
