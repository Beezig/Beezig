package tk.roccodev.zta.hiveapi.wrapper;

public class PvPMode extends APIGameMode {

	public PvPMode(String playerName) {
		super(playerName);
		
	}
	
	public int getKills(){
		return (int) object("kills");
	}
	
	public int getDeaths(){
		
		return (int) object("deaths");
		
	}

	
	
	
	
}
