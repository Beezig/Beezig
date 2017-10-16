package tk.roccodev.zta.hiveapi;

public enum BEDMap {

	BED_CASTLE("Castle"),
	BED_OCEANIC("Oceanic"),
	BED_TRIBAL("Tribal"),
	BED_SANDSTORM("Sandstorm"),
	BED_FACILITY("Facility"),
	BED_ORBIT("Orbit"),
	BED_IGLOO("Igloo"),
	BED_YINYANG("Yin-Yang"),
	BED_ETHEREAL("Ethereal"),
	BED_MORROWLAND("Morrowland"),
	BED_MARIO("Mario"),
	BED_FOOD("Food", true),	 //Duo only
	BED_TREASURE_ISLAND("Treasure Island", true), //Duo only
	BED_RUINS("Ruins", true, true),	//Teams only
	BED_PIRATES("Pirates", true, true),	//Teams only
	BED_FLORAL("Floral", true, true),	//Teams only
	BED_HELL("Hell", true, true),	//Teams only
	BED_PALACE("Palace", true, true), //Teams only
	BED_ROME("Rome", true, true), //Teams only
	BED_SPRING("Spring", true, true), //Teams only
	BED_WORLD_EXHIBITION("World Exhibition", true, true), //Teams only
	BED_SLEEPY_HOLLOW("Sleepy Hollow", true, true); //Teams only

	private String displayName;
	private boolean[] exclusiveModes;
	
	BEDMap(String display, boolean... exclusiveModes){	// Modes (by length): 0-solo, 1-duo, 2-teams
		this.displayName = display;
		this.exclusiveModes = exclusiveModes;
		
	}

	public boolean[] getExclusiveModes() {
		return exclusiveModes;
	}

	public static BEDMap getFromDisplay(String display){
		if(display.equals("Facilty")) return BED_FACILITY;
		for(BEDMap map : values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
