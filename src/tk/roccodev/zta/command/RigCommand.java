package tk.roccodev.zta.command;

import java.util.concurrent.ThreadLocalRandom;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.notes.NotesManager;

public class RigCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "rig";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/rig"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(System.currentTimeMillis() % 5 == 0) {
			The5zigAPI.getAPI().messagePlayer(Log.info + "I can feel it! It's the §bbest moment§3 to open a crate! Good luck.");
		}
		else {
			The5zigAPI.getAPI().messagePlayer(Log.error + "Nah... maybe next time?");
		}
		
		return true;
	}

	

	

}
