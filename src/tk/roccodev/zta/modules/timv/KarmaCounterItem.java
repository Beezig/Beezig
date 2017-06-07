package tk.roccodev.zta.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.TIMV;

public class KarmaCounterItem extends GameModeItem<TIMV>{

	public KarmaCounterItem(){
		super(TIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		
		
	StringBuilder sb = new StringBuilder();
	sb.append(TIMV.karmaCounter + " Karma");
	if((boolean)getProperties().getSetting("showrolepoints").get()){
		
		if(TIMV.dPoints != 0){
			sb.append(" / " + TIMV.dPoints + " Detective Points");
		}
		if(TIMV.iPoints != 0){
			sb.append(" / " + TIMV.iPoints + " Innocent Points");
		}
		if(TIMV.tPoints != 0){
			sb.append(" / " + TIMV.tPoints + " Traitor Points");
		}
		
		
		
	}
	return sb.toString().trim();
	
		
	}
	
	@Override
	public void registerSettings() {
		// TODO Auto-generated method stub
		getProperties().addSetting("showrolepoints", true);
		
		
	}

	
	@Override
	public String getName() {
		return "Game";
	}
	@Override
	public boolean shouldRender(boolean dummy){
		try{
			if(!(getGameMode() instanceof TIMV)) return false;
		return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.karmaCounter != 0);
		}catch(Exception e){
			return false;
		}
	}

}
