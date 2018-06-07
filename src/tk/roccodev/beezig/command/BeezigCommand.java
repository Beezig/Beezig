package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.BeezigMain;

public class BeezigCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "info";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/beezig"};
	}
	

	@Override
	public boolean execute(String[] args) {
		The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
		The5zigAPI.getAPI().messagePlayer(Log.info + "Running Beezig §bv" + BeezigMain.BEEZIG_VERSION + " (" + (BeezigMain.VERSION_HASH.isEmpty() ? "Stable" : "Beta/" + BeezigMain.VERSION_HASH) + ")");
		The5zigAPI.getAPI().messagePlayer(Log.info + (BeezigMain.newUpdate ? "There is a new version available!" : "You're running the latest version.") + "\n");
		The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
		return true;
	}

	

	

}
