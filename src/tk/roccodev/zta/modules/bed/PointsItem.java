package tk.roccodev.zta.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class PointsItem extends GameModeItem<BED>{

	public PointsItem(){
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try{
			if((boolean) getProperties().getSetting("showrank").get()){
				return HiveAPI.BEDpoints + " (" + BED.rank + ")";
			}
			return HiveAPI.BEDpoints;
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "Points";
	}
	
	@Override
	public void registerSettings() {
		getProperties().addSetting("showrank", false);
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || (BED.shouldRender(getGameMode().getState()));
		}catch(Exception e){
			return false;
		}
	}

}
