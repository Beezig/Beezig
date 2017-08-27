package tk.roccodev.zta.hiveapi;

public enum BEDMap {

	BED_CASTLE("Castle"),
	BED_OCEANIC("Oceanic"),
	BED_TRIBAL("Tribal"),
	BED_SANDSTORM("Sandstorm"),
	BED_FACILITY("Facility"),
	BED_ORBIT("Orbit"),
	BED_IGLOO("Igloo"),
	BED_FOOD("Food"),	//Duo only
	BED_ETHEREAL("Ethereal"),
	BED_MORROWLAND("Morrowland"),
	BED_MARIO("Mario"),
	BED_RUINS("Ruins"),	//Teams only
	BED_PIRATES("Pirates"),	//Teams only
	BED_FLORAL("Floral"),	//Teams only
	BED_HELL("Hell");	//Teams only

	private String displayName;
	
	BEDMap(String display){	
		this.displayName = display;
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
