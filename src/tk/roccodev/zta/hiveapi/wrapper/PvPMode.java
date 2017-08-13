package tk.roccodev.zta.hiveapi.wrapper;

public class PvPMode extends APIGameMode {

	public PvPMode(String playerName) {
		super(playerName);
		
	}
	
	public long getKills(){
		return (long) object("kills");
	}
	
	public long getDeaths(){
		
		return (long) object("deaths");
		
	}

	
	
	
	
}
