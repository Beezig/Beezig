package tk.roccodev.zta.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class KarmaItem extends GameModeItem<TIMV> {

	public KarmaItem(){
		super(TIMV.class);
	}
	
	@Override
	protected Object getValue(boolean dummy) {
		
		try{
			if((boolean) getProperties().getSetting("showrank").get()){
				return HiveAPI.TIMVkarma + " (" + TIMV.rank + ")";
			}
			return HiveAPI.TIMVkarma;
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}

	@Override
	public String getName() {
		return "Karma";
	}
	
	@Override
	public void registerSettings() {
		getProperties().addSetting("showrank", false);
	}
	
	@Override
	public boolean shouldRender(boolean dummy){
		try{
			if(!(getGameMode() instanceof TIMV)) return false;
		return dummy || TIMV.shouldRender(getGameMode().getState());
		}catch(Exception e){
			return false;
		}
	}

}
