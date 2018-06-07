package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.BED;

public class MapItem extends GameModeItem<BED>{

	public MapItem(){
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		if((boolean) getProperties().getSetting("mode").get()) return BED.activeMap + " (" + BED.mode + ")";
		return BED.activeMap;	
	}
	
	@Override
	public String getName() {
		return "Map";
	}
	
	@Override
	public void registerSettings(){
		getProperties().addSetting("mode", true);
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){
		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || (BED.shouldRender(getGameMode().getState()) && BED.activeMap != null);
		}catch(Exception e){
			return false;
		}
	}

}
