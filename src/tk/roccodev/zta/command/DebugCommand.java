package tk.roccodev.zta.command;

import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHIDE;

public class DebugCommand implements Command{
	public static boolean go = false;
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
			
			new Thread(new Runnable(){
				@Override
				public void run(){
					ApiHIDE api = new ApiHIDE("ItsNiklass");
					api.getBlocks();
				}
			}).start();
				 
			return true;
		
	}
}

