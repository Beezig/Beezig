package tk.roccodev.zta.modules.cai;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.games.CAI;
import tk.roccodev.zta.hiveapi.APIValues;

public class PointsItem extends GameModeItem<CAI>{

	public PointsItem(){
		super(CAI.class);
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
					sb.append(APIValues.CAIpoints).append(" (").append(CAI.rank).append(getMainFormatting());
					
				}else{
				
					sb.append(APIValues.CAIpoints).append(" (").append(ChatColor.stripColor(CAI.rank));
				}
				
				if((boolean)getProperties().getSetting("showpointstonextrank").get()){
					if(CAI.rankObject == null) return APIValues.CAIpoints;
					sb.append((boolean)getProperties().getSetting("showcolor").get() ? " / " + CAI.rankObject.getPointsToNextRank((int)APIValues.CAIpoints) : " / " + ChatColor.stripColor(CAI.rankObject.getPointsToNextRank((int)APIValues.CAIpoints)));
						
				}
				sb.append(
						
						(boolean)getProperties().getSetting("showcolor").get() ?
						
								getMainFormatting() + ")" :
						")");
				return sb.toString().trim();
				}
			return APIValues.CAIpoints;
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
			if(!(getGameMode() instanceof CAI)) return false;
		return dummy || (CAI.shouldRender(getGameMode().getState()));
		}catch(Exception e){
			return false;
		}
	}

}
