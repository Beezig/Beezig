package tk.roccodev.zta.hiveapi.stuff.dr;

public enum DRMap {

	DR_1("High City", 6, "5wkk0xvw"),
	SuperSecretProject2("Alaran Ruins", 6, "nwlg0mo9"),
	DRD5("The Prison", 5, "29vmg6q9"),
	DR_Legacy_Final("Legacy", 6, "Legacy"),
	DR_Cave("Cave", 6, "rw6q21pd"),
	DR_Western("Western", 7, "n93qkx2w"),
	DR_JungleBase_Beta("Jungle Base", 7, "z98rpord"),
	DR_Sonic("Sonic", 6, "rdno3m5w"),
	DR_Ice("Ice", 8, "gdrpr0ew"),
	DR_Alien_Facility("Alien Facility", 7, "xd0kmg09"),
	DR_Interstellar("Interstellar", 9, "xd0kr449"),
	DR_Renaissance("Riviera", 8, "nwlgxrg9"),
	DR_ToxicFactory("Toxic Factory", 6, "592j4r7w"),
	DR_Yagrium("Yagrium", 7, "5d77qqqd"),
	DR_Elements("Elements", 7, "z98joqrw"),
	DR_School("School", 6, "ldy1qrpd"),
	DR_Expelliarmus("Expelliarmus", 7, "xd10qzy9"),
	DR_Wahoo("Wa-hoo", 7, "rdq02m1w"),
	DR_Zoo("Zoo", 6, "Zoo"),
	DR_Ilvery("Ilvery", 7, "ldyp01rd"),
	DR_Throwback("Throwback", 7, "ywe8p34w"),
	DR_Vahltir("Vahltir", 5, "69z4064w"),
	DR_Mine("Mine", 7, "69z4kxgw"),
	DR_SweetTooth("SweetTooth", 7, "r9g3vrpw"),
	DR_EnchantedLagoon("Enchanted Lagoon", 7, "z98627gd"),
	DR_Starbase("Starbase", 8, "69z34nld"),
	DR_Windfall("Windfall", 7, "29v3263w"),
	DR_SkyLands("SkyLands", 7, "ldyyqrjd"),
	DR_ToBeeOrNotToBee("To Bee Or Not To Bee", 7, "y9mj1xz9"),
	DR_JadeCoast("Jade Coast", 7, "z986gnrd"),
	DR_Snowfall("Snowfall", 7, "4956g4jd"),
	DR_CrystalNight("Crystal Night", 6, "29v3nelw"),
	DR_Safari("Safari Valley", 7, "");

	private String displayName;
	private int checkpoints;
	private String speedrunid;
	
	DRMap(String display, int checkpoints, String speedrunid){
		
		this.displayName = display;
		this.checkpoints = checkpoints;
		this.speedrunid = speedrunid;
	}

	
	public static DRMap valueFromDisplay(String display){
		for(DRMap map : values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
	}
	

	public String getDisplayName() {
		return displayName;
	}
	
	public int getCheckpoints() {
		return checkpoints;
	}
	
	public String getSpeedrunID(){
		return speedrunid;
	}

	public static DRMap getFromDisplay(String display){
		for(DRMap map : DRMap.values()){
			if(map.getDisplayName().equalsIgnoreCase(display)) return map;
		}
		return null;
		
		
	}
	
	
}
