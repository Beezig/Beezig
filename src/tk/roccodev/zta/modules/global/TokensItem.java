package tk.roccodev.zta.modules.global;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameMode;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class TokensItem extends GameModeItem<GameMode>{

	public TokensItem(){
		super(GameMode.class);
	}
	
	
	
	
	@Override
	protected Object getValue(boolean dummy) {
		try{
			
			return HiveAPI.tokens;
		}catch(Exception e){
			e.printStackTrace();
			return "Server error";
		}
	}
	
	@Override
	public String getName() {
		return "Tokens";
	}
	
	
	
	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			
		return dummy || The5zigAPI.getAPI().getActiveServer() instanceof IHive;
		}catch(Exception e){
			return false;
		}
	}

}
