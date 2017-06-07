package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;

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
		
		if(args.length != 0){
			StringBuilder sb = new StringBuilder();
			for(String s : args){
				sb.append(s).append(" ");
			}
			if(args[0].startsWith("/")){
				The5zigAPI.getAPI().sendPlayerMessage(sb.toString());
			}else{
			The5zigAPI.getAPI().sendPlayerMessage("/" + sb.toString());
			}
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /say [command]");
		}
		
	}



}
