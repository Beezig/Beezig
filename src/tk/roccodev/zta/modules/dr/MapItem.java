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
			if((boolean)getProperties().getSetting("showtotalcheckpoints").get()){
				int totalc = map.getCheckpoints();
				tr.append(" (").append(totalc).append(")");
			}
		return tr.toString().trim(); 
	}
	
	@Override
	public String getName() {
		return "Map";
	}
	
	@Override
	public void registerSettings(){
		getProperties().addSetting("showtotalcheckpoints", true);
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
