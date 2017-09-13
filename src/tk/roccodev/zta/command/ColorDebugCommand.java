package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;

import java.util.UUID;

public class ColorDebugCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "colordebug";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/colordebug", "/cdebug"};
	}
	

	@Override
	public boolean execute(String[] args) {
		UUID id = The5zigAPI.getAPI().getGameProfile().getId();
		
		if(id.toString().equals("8b687575-2755-4506-9b37-538b4865f92d") || id.toString().equals("bba224a2-0bff-4913-b042-27ca3b60973f")){
			
			ZTAMain.isColorDebug = !ZTAMain.isColorDebug;
			
			The5zigAPI.getAPI().messagePlayer(Log.info + "Color debug is now: " + (ZTAMain.isColorDebug ? "§aON" : "§cOFF"));
			
			return true;
		}
		
		return false;
	}

	

	

}
