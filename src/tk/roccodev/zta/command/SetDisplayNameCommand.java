package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.Log;

public class SetDisplayNameCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "setdisplayname";
	}

	@Override
	public String[] getAliases() {

		return new String[]{"/setdisplayname", "/sdn"};
	}

	@Override
	public boolean execute(String[] args) {

		/*
			Just a test:

		Color codes don't work at all, § blocked in chat by MC
		--> White name
		Doesn't affect tablist sorting
		Only changes in tablist - not even over your head via 5zig setting
		Changes void after server change

		= Not good

		 */
		// "/sdn §6HotBoy3294 ItsNiklass"

		String displayName = args[0];
		String targetPlayer = The5zigAPI.getAPI().getGameProfile().getName();
		if(args.length > 1){
			targetPlayer = args[1];
            The5zigAPI.getAPI().messagePlayer(Log.info + "§3Usage: /sdn <name> <color>");
		}

		for(NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()){
			if(npi.getGameProfile().getName().equalsIgnoreCase(targetPlayer)){
                npi.setDisplayName(ChatColor.translateAlternateColorCodes('&', "§" + displayName));
                The5zigAPI.getAPI().messagePlayer(Log.info + "§3Your display name has been updated to §r" + npi.getDisplayName());
			}
		}
		return true;
	}



}
