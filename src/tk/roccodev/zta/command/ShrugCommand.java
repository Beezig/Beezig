package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;

public class ShrugCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "shrug";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/shrug"};
		
		return aliases;
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length == 0){
			The5zigAPI.getAPI().sendPlayerMessage("¯\\_(ツ)_/¯");
		}
		else{
			StringBuilder sb = new StringBuilder();
			for(String arg : args){
				sb.append(arg).append(" ");
			}
			sb.append(" ¯\\_(ツ)_/¯");
			The5zigAPI.getAPI().sendPlayerMessage(sb.toString().trim());
		}
		return true;
	}



}
