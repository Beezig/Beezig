package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;

public class ColorDebugCommand implements Command {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "colordebug";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "/colordebug", "/cdebug" };
	}

	@Override
	public boolean execute(String[] args) {

		ZTAMain.isColorDebug = !ZTAMain.isColorDebug;

		The5zigAPI.getAPI()
				.messagePlayer(Log.info + "Color debug is now: " + (ZTAMain.isColorDebug ? "§aON" : "§cOFF"));

		return true;

	}

}
