package tk.roccodev.zta.hiveapi.stuff.cai;

public enum CAIMap {

	CAI_Berlin("Berlin"),
	BlackhorseBasin("Blackhorse Basin"),
	Hoodoo("Hoodoo"),
	CAI_China("China"),
	ReindeerBasin("Reindeer Basin"),
	CAI_Warehouse("Warehouse"),
	CAI_Farm("Farm"),
	CAI_Orbital("Orbital"),
	CAI_Pirates("Pirates"),
	CAI_Shipwreck("Shipwreck"),
	CAI_DesolateHills("Desolate Hills"),
	CAI_Atlantic("Atlantic"),
	CAI_Knossos("Knossos"),
	CAI_Sky("Sky"),
	CAI_SkyView("SkyView"),
	CAI_Temple("Temple"),
	CAI_Antarctica("Antarctica"),
	CAI_Subterranean("Subterranean"),
	CAI_OakheartManor("Oakheart Manor"),
	CAI_Roar("Roar");

	private String displayName;

	CAIMap(String display){
		this.displayName = display;
	}

	public static CAIMap getFromDisplay(String display){
		for(CAIMap map : CAIMap.values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
	}

	public String getDisplayName() {
		return displayName;
	}

}

