package tk.roccodev.zta.modules.sky;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.SKY;

public class MapItem extends GameModeItem<SKY>{

		public MapItem(){
			super(SKY.class);
		}


		@Override
		protected Object getValue(boolean dummy) {
			try{
				if(SKY.map == null) return "No Map";
				return SKY.map;
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
				return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("SKY") && SKY.map != null && !SKY.map.isEmpty());
			}catch(Exception e){
				return false;
			}
		}

}
