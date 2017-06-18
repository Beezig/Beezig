package tk.roccodev.zta.modules.timv;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class KarmaItem extends GameModeItem<TIMV> {

	public KarmaItem(){
		super(TIMV.class);
	}
	
	private String getMainFormatting(){
		//if(this.getProperties().getFormatting().getMainColor() != null || this.getProperties().getFormatting().getMainColor() != null){
		//		return something;
		//	TODO actually make this, but it very dumb; getProperties().getFormatting() has two diffrent methods for color & formatting, getFormatting() only one.
		//}
		return The5zigAPI.getAPI().getFormatting().getMainFormatting();	
	}
	
	@Override
	protected Object getValue(boolean dummy) {		
		try{
			if((boolean) getProperties().getSetting("showrank").get()){
				if((boolean) getProperties().getSetting("showcolor").get()){
					return HiveAPI.TIMVkarma + " (" + TIMV.rank + getMainFormatting() + ")";
				}
				return HiveAPI.TIMVkarma + " (" + ChatColor.stripColor(TIMV.rank) + ")";
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
		getProperties().addSetting("showcolor", true);
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
