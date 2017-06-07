package tk.roccodev.zta.hiveapi;

public enum TIMVMap {

	ARMADILLO("Armadillo",3, 3),
	ASIAN_SKY("Asian Sky", 3,3),
	ARCWIND_TEMPLE("Arcwind Temple", 3, 3),
	AZURE_ISLAND("Azure Island", 4, 5),
	CABIN_IN_THE_WOODS("Cabin in the Woods",2,2),
	CALYPSO_HARBOR("Calypso Harbor",2,2),
	CASTLE("Castle",3,3),
	CATHEDRAL("Cathedral", 3,3),
	CLOCKWORLD_ISLAND("Clockworld Island", 3, 3),
	COSMIC_FORAY("Cosmic Foray", 3,3),
	FALLEN_BRIDGES("Fallen Bridges", 3, 3),
	FORGE("Forge", 6, 7),
	FORSAKEN("Forsaken", 3, 3),
	FROZEN_CARGO("Frozen Cargo", 2, 2),
	FUNFAIR("FunFair", 2, 2),
	HS_CLASSIC("Hermit Sands: Classic", 3, 3),
	HS_REVISIT("Hermit Sands: Revisited", 3,3),
	HS_REVAMP("Hermit Sands: Revamped", 2, 2),
	KAZAMUZO_TEMPLE("Kazamuzo Temple", 4, 4),
	METROPOLIS("Metropolis", 3, 3),
	MV_CLASSIC("MineVille: Classic", 4, 4),
	MV_REVAMP("MineVille: Revamped", 4, 4),
	PIRATES("Pirates", 3,3),
	PRECINCT("Precinct", 4,4),
	PUERTO_TEREZA("Puerto Tereza", 2,2),
	SERAPHIM("Seraphim", 2, 3),
	SKY_LANDS("Sky Lands", 2, 2),
	SKY_SHIP("Sky Ship",2 ,2),
	SPECTRAL("Spectral", 3,3),
	SPRING_FALLS("Spring Falls", 1, 1),
	STATION("Station", 3, 3),
	THE_CANAL("The Canal", 2, 2),
	THE_SHIRE("The Shire", 3, 3),
	WOODBURY("Welcome to Woodbury", 2, 2),
	WESTWOOD("Westwood", 3, 3),
	WONDERLAND("Wonderland", 2, 2);
	
	private String displayName;
	private int accessibleEnderchests;
	private int totalEnderchests;
	
	
	TIMVMap(String display, int accessible, int totalEnders){
		
		this.displayName = display;
		this.accessibleEnderchests = accessible;
		this.totalEnderchests = totalEnders;
		
	}


	public String getDisplayName() {
		return displayName;
	}


	public int getAccessibleEnderchests() {
		return accessibleEnderchests;
	}


	public int getTotalEnderchests() {
		return totalEnderchests;
	}
	
	public static TIMVMap getFromDisplay(String display){
		for(TIMVMap map : TIMVMap.values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
		
		
	}
	
	
}
