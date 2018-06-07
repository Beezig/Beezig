package tk.roccodev.beezig.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.games.Giant;

public class DeathsItem extends GameModeItem<Giant>{

	public DeathsItem(){
		super(Giant.class);
	}
	
	
	
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			
			if((boolean) getProperties().getSetting("showtotal").get()) return Giant.gameDeaths + " (" + (Giant.gameDeaths + Giant.totalDeaths) + ")";
			return Giant.gameDeaths;
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "Deaths";
	}
	
	@Override
	public void registerSettings() {
		getProperties().addSetting("showtotal", true);
	}
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			
		return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.gameDeaths != 0);
		}catch(Exception e){
			return false;
		}
	}

}
