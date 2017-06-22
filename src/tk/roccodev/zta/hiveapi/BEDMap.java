package tk.roccodev.zta.hiveapi;

public enum BEDMap {

	BED_CASTLE("Castle"),
	BED_OCEANIC("Oceanic"),
	BED_TRIBAL("Tribal"),
	BED_SANDSTORM("Sandstorm"),
	BED_FACILITY("Facilty"), 
	BED_ORBIT("Orbit"),
	BED_IGLOO("Igloo"),
	BED_FOOD("Food"),	//Teams only
	BED_ETHEREAL("Ethereal"),
	BED_MORROWLAND("Morrowland");

	private String displayName;
	
	BEDMap(String display){	
		this.displayName = display;
	}

	public static BEDMap getFromDisplay(String display){
		for(BEDMap map : values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
