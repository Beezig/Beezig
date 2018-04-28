package tk.roccodev.zta.modules.hide;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.HIDE;

public class MapItem extends GameModeItem<HIDE>{

		public MapItem(){
			super(HIDE.class);
		}


		@Override
		protected Object getValue(boolean dummy) {
			try{
				return HIDE.activeMap;
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
				return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("hide") && HIDE.activeMap != null);
			}catch(Exception e){
				return false;
			}
		}

}
