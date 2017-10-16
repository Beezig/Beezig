package tk.roccodev.zta.hiveapi.stuff.hide;

public enum HIDEMap {

	SHIPYARD("Shipyard"),
	CS_OFFICE("CS_Office"),
	KEEP("Keep"),
	VENICE_BRIDGE("Venice Bridge"),
	BORA_BORA("Bora Bora"),
	TOWN_SQUARE("Town Square"),
	ANIMAL_VILLAGE("Animal Village"),
	FROZEN("Frozen"),
	CRUISE("Cruise"),
	SPACE("Space"),
	PRIPYAT("Pripyat"),
	HOTEL("Hotel"),
	VILLA("Villa"),
	BEFORE_SPACE("Before Space"),
	LOTUS("Lotus"),
	PARIS("Paris"),
	MOREFROZEN("MoreFrozen"),
	HEARTHSTONE_VILLAGE("Hearthstone Village"),
	INDUSTRIA("Industria"),
	TOWN_SQUARE_REVAMP("Town Square: Revamped"),

	//not in API v
	OASIS("Oasis"),
	HOTEL_CALIFORNIA("Hotel California");

	private String displayName;

	HIDEMap(String display){
		this.displayName = display;
	}

	public static HIDEMap getFromDisplay(String display){
		for(HIDEMap map : HIDEMap.values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
	}

	public String getDisplayName() {
		return displayName;
	}

}

