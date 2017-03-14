package tk.roccodev.zta.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.TIMV;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.TIMVMap;

public class TIMVListener extends AbstractGameListener<TIMV>{

	@Override
	public Class<TIMV> getGameMode() {
		// TODO Auto-generated method stub
		return TIMV.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		
		return lobby.equals("TIMV");
		
	}
	
	@Override
	public void onGameModeJoin(TIMV gameMode){
		gameMode.setState(GameState.STARTING);
		try {
			HiveAPI.updateKarma();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	@Override
	public boolean onServerChat(TIMV gameMode, String message) {
		
		if(message.equals("§8▍ §3TIMV§8 ▏ §6Welcome to Trouble in Mineville!")){
			gameMode.setState(GameState.STARTING);
			The5zigAPI.getLogger().info("DEBUG = Joined TIMV");
			try {
				HiveAPI.updateKarma();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		else if(message.contains("§cLost §e20§c karma") && gameMode != null){
			TIMV.minus20();
		}
		else if(message.contains("§cLost §e40§c karma") && gameMode != null){
			TIMV.minus40();
		}
		else if(message.contains("§aAwarded §e20§a karma") && gameMode != null){
			TIMV.plus20();
			
		}
		else if(message.contains("§aAwarded §e10§a karma") && gameMode != null){
			TIMV.plus10();
		}
		else if(message.contains("§aAwarded §e25§a karma") && gameMode != null){
			TIMV.plus25();
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §cGame Over§7") && gameMode != null){
			TIMV.reset(gameMode);
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6Voting has ended! The map") && gameMode != null){
			String afterMsg = message.split("§8▍ §3TIMV§8 ▏ §6Voting has ended! The map")[1];
			The5zigAPI.getLogger().info(afterMsg);
		// §bSky Lands§6
			String map = "";
		    
		    Pattern pattern = Pattern.compile(Pattern.quote("§b") + "(.*?)" + Pattern.quote("§6"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    The5zigAPI.getLogger().info(map);
		    TIMVMap map1 = TIMVMap.getFromDisplay(map);
		    
		    TIMV.activeMap = map1;
			
		}
		else if(message.startsWith(ChatColor.AQUA + "Role points:")){
			//Better /records
			try{
			long mp = HiveAPI.getKarmaPerGame(TIMV.lastRecords);
			long rp = HiveAPI.getRolepoints(TIMV.lastRecords);
			long karma = HiveAPI.getKarma(TIMV.lastRecords);
			int ach = HiveAPI.getAchievements(TIMV.lastRecords);
			
			double krr = (double)Math.round(((double)karma / (double)rp) * 100d) / 100d;
			
			
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Most Points: " + ChatColor.YELLOW + mp);
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Karma/Rolepoints: " + ChatColor.YELLOW + krr);
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Achievements: " + ChatColor.YELLOW + ach);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
		
		return false;
	}

	@Override
	public void onServerConnect(TIMV gameMode) {
		TIMV.reset(gameMode);
		
		
	}
	
	
	
	
	
	
	

	

}
