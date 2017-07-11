package tk.roccodev.zta.command;

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
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(String s : sb.getLines().keySet()){
				The5zigAPI.getAPI().messagePlayer(s);		
			}
			return true;
	}
}

