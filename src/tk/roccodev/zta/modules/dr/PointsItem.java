package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class PointsItem extends GameModeItem<DR>{

	public PointsItem(){
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try{
			if((boolean) getProperties().getSetting("showrank").get()){
				return HiveAPI.DRpoints + " (" + DR.rank + ")";
			}
			return HiveAPI.DRpoints;
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
			if(!(getGameMode() instanceof DR)) return false;
		return dummy || (DR.shouldRender(getGameMode().getState()));
		}catch(Exception e){
			return false;
		}
	}

}
