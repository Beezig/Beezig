package tk.roccodev.beezig.command;

public class DebugCommand implements Command{
	public static boolean go = false;
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "debug";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/debug"};
	}
	

	@Override
	public boolean execute(String[] args) {
			//some debug code here v
			
			new Thread(() -> {

			}).start();
				 
			return true;
		
	}
}

