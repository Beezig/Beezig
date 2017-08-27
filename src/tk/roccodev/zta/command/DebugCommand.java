package tk.roccodev.zta.command;

import java.util.concurrent.TimeUnit;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.games.BED;

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
					int i = 0;
					while(true){						
						try {					
							TimeUnit.MILLISECONDS.sleep(100);
							if(The5zigAPI.getAPI().isInWorld()) The5zigAPI.getAPI().messagePlayer(i + "ms " + BED.mode);
							i = i+100;
						} catch (Exception e) {
							e.printStackTrace();
						}						
					}
				}
			}).start();
				 
			return true;
		
	}
}

