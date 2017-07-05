package tk.roccodev.zta.games;

public class GNTM extends Giant{

	public static GNTM instance;
	
	@Override
	public boolean isMini() {
		
		return true;
	}

	public GNTM(){
		instance = this;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SkyGiants:Mini";
	}

	

	
	
	
}
