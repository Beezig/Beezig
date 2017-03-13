package tk.roccodev.zta;

import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class TIMV extends GameMode{

	public static int karmaCounter;
	
	public static void plus20(){
		karmaCounter +=20;
		HiveAPI.karma +=20;
	}
	public static void plus25(){
		karmaCounter +=25;
		HiveAPI.karma +=25;
	}
	public static void plus10(){
		karmaCounter +=10;
		HiveAPI.karma += 10;
		}
	public static void minus20(){
		karmaCounter -=20;
		HiveAPI.karma -=20;
		}
	public static void minus40(){
		karmaCounter -=40;
		HiveAPI.karma -=40;
		}
	
	public static void resetCounter(){
		karmaCounter = 0;
	}
	
	public static void reset(TIMV gm){
		resetCounter();
		gm.setState(GameState.FINISHED);
	}
	
	@Override
	public String getName(){
		return "Trouble in Mineville";
	}

	public static boolean shouldRender(GameState state){
		
		if(state == GameState.GAME) return true;
		if(state == GameState.PREGAME) return true;
		if(state == GameState.STARTING) return true;
		return false;
	}
	
}
