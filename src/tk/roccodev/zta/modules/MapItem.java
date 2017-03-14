package tk.roccodev.zta.modules;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.TIMV;
import tk.roccodev.zta.hiveapi.TIMVMap;

public class MapItem extends GameModeItem<TIMV>{

	public MapItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		TIMVMap map = TIMV.activeMap;
		if(map == null) return "Unknown map";
		String name = map.getDisplayName();
		int a = map.getAccessibleEnderchests();
		int t = map.getTotalEnderchests();
		
		return name + " (" + a + "/" + t + ")"; 
	}
	
	@Override
	public String getName() {
		return "Map";
	}
	@Override
	public boolean shouldRender(boolean dummy){
		try{
		return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.activeMap != null);
		}catch(NullPointerException e){
			return false;
		}
	}

}
