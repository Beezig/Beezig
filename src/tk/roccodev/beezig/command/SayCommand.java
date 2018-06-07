package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.Log;

public class SayCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "servercommand";
	}

	@Override
	public String[] getAliases() {
		// "/s" is a staff command.
		return new String[]{"/servercommand"};
	}

	@Override
	public boolean execute(String[] args) {
		
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
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /servercommand [command]");
		}
		return true;
	}



}
