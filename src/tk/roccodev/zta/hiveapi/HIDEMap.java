
	package tk.roccodev.zta.hiveapi;

	public enum HIDEMap {

		HIDE_Shipyard("Shipyard"),
		HIDE_CS_Office("CS_Office"),
		HIDE_Keep("Keep"),
		HIDE_Venice_Bridge("Venice Bridge"),
		HIDE_BoraBora("Bora Bora"),
		HIDE_TownSquare("Town Square"),
		HIDE_AnimalVillage("Animal Village"),
		HIDE_Frozen("Frozen"),
		HIDE_Cruise("Cruise"),
		HIDE_Space("Space"),
		HIDE_Pripyat("Pripyat"),
		HIDE_Hotel("Hotel"),
		HIDE_Villa("Villa"),
		HIDE_BeforeSpace("Before Space"),
		HIDE_Lotus("Lotus"),
		HIDE_Paris("Paris"),
		HIDE_MoreFrozen("MoreFrozen"),
		HIDE_HearthstoneVillage("Hearthstone Village"),
		HIDE_Industria("Industria"),
		HIDE_TownSquareRevamp("Town Square: Revamped"),

		HIDE_Oasis("Oasis"),
		HIDE_Hotel_California("Hotel California");

		private String displayName;
		
		HIDEMap(String display){
			this.displayName = display;	
		}

		public static HIDEMap getFromDisplay(String display){
			for(HIDEMap map : values()){
				if(map.getDisplayName().equalsIgnoreCase(display)) return map;
			}
			return null;
		}
		
		public String getDisplayName() {
			return displayName;
		}

	}

