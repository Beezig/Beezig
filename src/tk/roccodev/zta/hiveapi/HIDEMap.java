
	package tk.roccodev.zta.hiveapi;

	public enum HIDEMap {

		HIDE_CASTLE("Castle");

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

