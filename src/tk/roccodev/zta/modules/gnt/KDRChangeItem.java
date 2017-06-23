package tk.roccodev.zta.modules.gnt;

import java.text.DecimalFormat;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.Giant;

public class KDRChangeItem extends GameModeItem<Giant>{

	public KDRChangeItem(){
		super(Giant.class);
	}
	
	
	
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			double kdr = Giant.gameKdr;
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			return df.format(kdr) + " (" + (Giant.gameKdr - Giant.totalKdr > 0 ? "+" : "") + df.format((Giant.gameKdr - Giant.totalKdr)) + ")";
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "New KDR";
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			
		return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && (Giant.totalKdr - Giant.gameKdr != 0));
		}catch(Exception e){
			return false;
		}
	}

}
