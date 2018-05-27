package tk.roccodev.zta.command;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;

public class UUIDCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "uuid";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/uuid"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		
		if(args.length == 0 ){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /uuid [player] (s/c)");
			return true;
		}
		else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String pl = args[0];
					boolean copy = false;
					if(args.length == 2) {
						String modes = args[1];
						copy = modes.contains("c");
					}
					
					String uuid = APIUtils.getUUID(pl);
					The5zigAPI.getAPI().messagePlayer(Log.info + pl + "'s UUID is §b" + uuid);
					if(copy) {
						StringSelection sel = new StringSelection(uuid);
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
						The5zigAPI.getAPI().messagePlayer(Log.info + "Copied to clipboard!");
					}
				}
			}).start();
			
		}
		
			
			
			
		
		
		return true;
	}

	

	

}
