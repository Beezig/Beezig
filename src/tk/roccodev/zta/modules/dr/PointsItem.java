package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class PointsItem extends GameModeItem<DR>{

	public PointsItem(){
		super(DR.class);
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
					return HiveAPI.DRpoints + " (" + DR.rank + getMainFormatting() + ")";
				}
				return HiveAPI.DRpoints + " (" + ChatColor.stripColor(DR.rank) + ")";
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
		getProperties().addSetting("showcolor", true);
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
