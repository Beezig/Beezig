package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.DRMap;

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
			if((boolean)getProperties().getSetting("showcheckpoints").get() && DR.role == "Runner"){
				int totalc = map.getCheckpoints();
				tr.append(" (").append(DR.checkpoints).append("/").append(totalc).append(" Checkpoints)");
			}
		return tr.toString().trim(); 
	}
	
	@Override
	public String getName() {
		return "Map";
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
