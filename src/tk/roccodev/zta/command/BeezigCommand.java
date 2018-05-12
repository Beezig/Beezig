package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;

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
		The5zigAPI.getAPI().messagePlayer(Log.info + "Running Beezig v§b" + ZTAMain.BEEZIG_VERSION + "(" + (ZTAMain.VERSION_HASH.isEmpty() ? "Stable" : "Beta/" + ZTAMain.VERSION_HASH) + ")");
		The5zigAPI.getAPI().messagePlayer(Log.info + (ZTAMain.newUpdate ? "There is a new version available!" : "You're running the latest version.") + "\n");
		The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
		return true;
	}

	

	

}
