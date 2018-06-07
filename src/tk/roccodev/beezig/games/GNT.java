package tk.roccodev.beezig.games;

public class GNT extends Giant{

	public static GNT instance;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SkyGiants";
	}

	public GNT(){
		instance = this;
	}
	
	//No need to override isMini(), as it returns false as default.
	
	
	
	
	
	

}
