package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;

public class SayCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "say";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/say", "/s"};
		return aliases;
	}

	@Override
	public void execute(String[] args) {
		
		if(args.length == 1){
			if(args[0].startsWith("/")){
				The5zigAPI.getAPI().sendPlayerMessage(args[0]);
			}else{
			The5zigAPI.getAPI().sendPlayerMessage("/" + args[0]);
			}
		}
		else{
			The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin]§e Usage: /say [command]");
		}
		
	}



}
