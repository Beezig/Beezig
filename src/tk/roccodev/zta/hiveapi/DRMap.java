package tk.roccodev.zta.hiveapi;

public enum DRMap {

	HIGH_CITY("High City", 6),
	ALARAN_RUINS("Alaran Ruins", 6),
	THE_PRISON("The Prison", 5),
	LEGACY("Legacy", 6),
	CAVE("Cave", 6),
	WESTERN("Western", 7),
	JUNGLE_BASE("Jungle Base", 7),
	SONIC("Sonic", 6),
	ICE("Ice", 8),
	ALIEN_FACILITY("Alien Facility", 7),
	INTERSTELLAR("Interstellar", 9),
	RIVIERA("Riviera", 8),
	TOXIC_FACTORY("Toxic Factory", 6),
	YAGRIUM("Yagrium", 7),
	ELEMENTS("Elements", 7),
	SCHOOL("School", 6),
	EXPELLIARMUS("Expelliarmus", 7),
	WAHOO("Wa-hoo", 7),
	ZOO("Zoo", 6),
	MINE("Mine", 0),
	ILVERY("Ilvery", 7),
	THROWBACK("Throwback", 7),
	VAHLTIR("Vahltir", 5);

	private String displayName;
	private int checkpoints;
	
	DRMap(String display, int checkpoints){
		
		this.displayName = display;
		this.checkpoints = checkpoints;
		
	}


	public String getDisplayName() {
		return displayName;
	}
	
	public int getCheckpoints() {
		return checkpoints;
	}

	public static DRMap getFromDisplay(String display){
		for(DRMap map : DRMap.values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
		
		
	}
	
	
}
