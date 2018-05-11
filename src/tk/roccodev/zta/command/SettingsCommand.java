package tk.roccodev.zta.command;

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
		return new String[]{"/settings"};
	}

	@Override
	public boolean execute(String[] args) {
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(args.length == 0){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage:");
			The5zigAPI.getAPI().messagePlayer("§7-§b /settings list §3to list all settings");
			The5zigAPI.getAPI().messagePlayer("§7-§b /settings filter [gamemode] §3to list filtered settings");
			The5zigAPI.getAPI().messagePlayer("§7-§b /settings filter global §3to list general settings");
			The5zigAPI.getAPI().messagePlayer("§7-§b /settings [setting] §3to get the value of a setting");
			The5zigAPI.getAPI().messagePlayer("§7-§b /settings [setting] [true/false] §3to set the value of a setting.");
		}
		else if(args.length == 1){
		if(args[0].equalsIgnoreCase("list")){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Settings:");
			for(Setting sett : Setting.values()){
				String todisplay = sett.getValue() ? "§aTrue" : "§cFalse";
				The5zigAPI.getAPI().messagePlayer("§3 - " + sett.name() + " (" + todisplay + "§3) (§b" + sett.getBriefDescription() + "§3)");
			}
			return true;
		}
		String setting = args[0];
		Setting sett = null;
		try{
			sett = Setting.valueOf(setting.toUpperCase());
		}
		catch(IllegalArgumentException e){
			The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid setting.");
		}
		String todisplay = sett.getValue() ? "§bTrue" : "§cFalse";
		The5zigAPI.getAPI().messagePlayer(Log.info + sett.name() + ": " + todisplay +"§3 (§b" + sett.getBriefDescription() + "§3)");
		
		}
		else if(args.length == 2){
			String setting = args[0];
			String value = args[1];
			if(setting.equalsIgnoreCase("filter")){
				
				The5zigAPI.getAPI().messagePlayer(Log.info + "Filter results:");
				if(value.equalsIgnoreCase("global")){
					value = "show";
					Setting sett = Setting.AUTOVOTE;
					String todisplay = sett.getValue() ? "§bTrue" : "§cFalse";
					The5zigAPI.getAPI().messagePlayer("§3 - " + sett.name() + " (" + todisplay + "§3) (§b" + sett.getBriefDescription() + "§3)");
				}
				for(Setting sett : Setting.values()){
					if(sett.name().toUpperCase().startsWith(value.toUpperCase())){
						String todisplay = sett.getValue() ? "§bTrue" : "§cFalse";
						The5zigAPI.getAPI().messagePlayer("§3 - " + sett.name() + " (" + todisplay + "§3) (§b" + sett.getBriefDescription() + "§3)");
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

		}
		return true;
	}

	

}
