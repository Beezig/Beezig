package tk.roccodev.beezig.modules.cai;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.CAI;

public class MapItem extends GameModeItem<CAI>{

		public MapItem(){
			super(CAI.class);
		}


		@Override
		protected Object getValue(boolean dummy) {
			try{
				if(CAI.activeMap == null) return "No Map";
				return CAI.activeMap;
			}catch(Exception e){
				e.printStackTrace();
				return "No Map";
			}
		}

		@Override
		public String getName() {
			return Log.t("beezig.module.map");
		}



		@Override
		public boolean shouldRender(boolean dummy){
			try{
				return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("CAI") && CAI.activeMap != null);
			}catch(Exception e){
				return false;
			}
		}

}
