package tk.roccodev.zta.command;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;

public class DebugCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "debug";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/debug"};
		return aliases;
	}
	

	@Override
	public boolean execute(String[] args) {
			//some debug code here v
		
		
			Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
			
			for(Map.Entry<String, Integer> entry: sb.getLines().entrySet()) {
				
				if(entry.getValue() == Integer.valueOf(args[0])){
					
					The5zigAPI.getAPI().messagePlayer(entry.getKey());
					
				}
				
			}
			
				 
			return true;
		
	}
}

