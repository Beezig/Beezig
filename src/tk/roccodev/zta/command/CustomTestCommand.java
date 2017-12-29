package tk.roccodev.zta.command;

public class CustomTestCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "customtest";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/customtest", "/ctest"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		
		return true;
	}

	

	

}
