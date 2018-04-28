package tk.roccodev.zta.modules.sky;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.games.SKY;
import tk.roccodev.zta.hiveapi.APIValues;

public class PointsItem extends GameModeItem<SKY>{

	public PointsItem(){
		super(SKY.class);
	}
	
	private String getMainFormatting(){
		if(this.getProperties().getFormatting() != null){
			if(this.getProperties().getFormatting().getMainColor() != null && this.getProperties().getFormatting().getMainFormatting() == null){
				return The5zigAPI.getAPI().getFormatting().getMainFormatting().replace((The5zigAPI.getAPI().getFormatting().getMainFormatting()).charAt(1), this.getProperties().getFormatting().getMainColor().toString().charAt(1));
				//Replaces Char at index 1 (ColorTag) of the Main formatting with the custom one.
			}
			if(this.getProperties().getFormatting().getMainColor() == null && this.getProperties().getFormatting().getMainFormatting() != null){
				return The5zigAPI.getAPI().getFormatting().getMainFormatting().replace((The5zigAPI.getAPI().getFormatting().getMainFormatting()).charAt(3), this.getProperties().getFormatting().getMainFormatting().toString().charAt(3));
				//Replaces Char at index 3 (FormattingTag) of the Main formatting with the custom one.
			}
			if(this.getProperties().getFormatting().getMainColor() != null && this.getProperties().getFormatting().getMainFormatting() != null){
				return this.getProperties().getFormatting().getMainColor() +""+ this.getProperties().getFormatting().getMainFormatting();
			}
		}
		return The5zigAPI.getAPI().getFormatting().getMainFormatting();	
	}
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			if((boolean) getProperties().getSetting("showrank").get()){
				StringBuilder sb = new StringBuilder();
				if((boolean) getProperties().getSetting("showcolor").get()){
					sb.append(APIValues.SKYpoints).append(" (").append(SKY.rank).append(getMainFormatting());
					
				}else{
				
					sb.append(APIValues.SKYpoints).append(" (").append(ChatColor.stripColor(SKY.rank));
				}
				
				if((boolean)getProperties().getSetting("showpointstonextrank").get()){
					if(SKY.rankObject == null) return APIValues.SKYpoints;
					sb.append((boolean)getProperties().getSetting("showcolor").get() ? " / " + SKY.rankObject.getPointsToNextRank((int)APIValues.SKYpoints) : " / " + ChatColor.stripColor(SKY.rankObject.getPointsToNextRank((int)APIValues.SKYpoints)));
						
				}
				sb.append(
						
						(boolean)getProperties().getSetting("showcolor").get() ?
						
								getMainFormatting() + ")" :
						")");
				return sb.toString().trim();
				}
			return APIValues.SKYpoints;
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
		getProperties().addSetting("showpointstonextrank", false);
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof SKY)) return false;
		return dummy || (SKY.shouldRender(getGameMode().getState()));
		}catch(Exception e){
			return false;
		}
	}

}
