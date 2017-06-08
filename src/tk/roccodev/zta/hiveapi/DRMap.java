package tk.roccodev.zta.hiveapi;

public enum DRMap {

	DR_1("High City", 6),
	SuperSecretProject2("Alaran Ruins", 6),
	DRD5("The Prison", 5),
	DR_Legacy_Final("Legacy", 6),
	DR_Cave("Cave", 6),
	DR_Western("Western", 7),
	DR_JungleBase_Beta("Jungle Base", 7),
	DR_Sonic("Sonic", 6),
	DR_Ice("Ice", 8),
	DR_Alien_Facility("Alien Facility", 7),
	DR_Interstellar("Interstellar", 9),
	DR_Renaissance("Riviera", 8),
	DR_ToxicFactory("Toxic Factory", 6),
	DR_Yagrium("Yagrium", 7),
	DR_Elements("Elements", 7),
	DR_School("School", 6),
	DR_Expelliarmus("Expelliarmus", 7),
	DR_Wahoo("Wa-hoo", 7),
	DR_Zoo("Zoo", 6),
	DR_Mine("Mine", 0),
	DR_Ilvery("Ilvery", 7),
	DR_Throwback("Throwback", 7),
	DR_Vahltir("Vahltir", 5);

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
