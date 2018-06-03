package tk.roccodev.zta.modules.sgn;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.SGN;

public class MapItem extends GameModeItem<SGN>{

		public MapItem(){
			super(SGN.class);
		}


		@Override
		protected Object getValue(boolean dummy) {
			try{
				if(SGN.activeMap == null) return "No Map";
				return SGN.activeMap;
			}catch(Exception e){
				e.printStackTrace();
				return "No Map";
			}
		}

		@Override
		public String getName() {
			return "Map";
		}



		@Override
		public boolean shouldRender(boolean dummy){
			try{
				return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("SGN") && SGN.activeMap != null && !SGN.activeMap.isEmpty());
			}catch(Exception e){
				return false;
			}
		}

}
