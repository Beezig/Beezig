package tk.roccodev.beezig.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRMap;

public class MapItem extends GameModeItem<DR>{

	public MapItem(){
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		DRMap map = DR.activeMap;
		if(map == null) return "Unknown map";
		String name = map.getDisplayName();
		StringBuilder tr = new StringBuilder();
		tr.append(name);
			if((boolean)getProperties().getSetting("showcheckpoints").get() && "Runner".equals(DR.role)){
				int totalc = map.getCheckpoints();
				tr.append(" (").append(DR.checkpoints).append("/").append(totalc).append(" " + Log.t("beezig.str.dr.checkpoints") + ")");
			}
		return tr.toString().trim(); 
	}
	
	@Override
	public String getName() {
		return Log.t("beezig.module.map");
	}
	
	@Override
	public void registerSettings(){
		getProperties().addSetting("showcheckpoints", true);
	}
	
	@Override
	public boolean shouldRender(boolean dummy){
		
		try{
			if(!(getGameMode() instanceof DR)) return false;
		return dummy || (DR.shouldRender(getGameMode().getState()) && DR.activeMap != null);
		}catch(Exception e){
			return false;
		}
	}

}
