package tk.roccodev.zta.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.TIMV;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.TIMVMap;
import tk.roccodev.zta.hiveapi.TIMVRank;

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
		ZTAMain.isTIMV = true;
		Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
		if(sb != null) The5zigAPI.getLogger().info(sb.getTitle());
		if(sb != null && sb.getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Your TIMV Stats")){
			
			int karma = sb.getLines().get(ChatColor.AQUA + "Karma");
			HiveAPI.karma = (long) karma;
			
			
		}else{
		try {
			HiveAPI.updateKarma();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
	}
	

	@Override
	public boolean onServerChat(TIMV gameMode, String message) {
		// Uncomment this to see the real messages with chatcolor. vv
		// The5zigAPI.getLogger().info("(" + message + ")");
		if(message.equals("§8▍ §3TIMV§8 ▏ §6Welcome to Trouble in Mineville!")){
			gameMode.setState(GameState.STARTING);
			ZTAMain.isTIMV = true;
			The5zigAPI.getLogger().info("DEBUG = Joined TIMV");
			Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
			
			if(sb != null && sb.getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Your TIMV Stats")){
				
				int karma = sb.getLines().get(ChatColor.AQUA + "Karma");
				HiveAPI.karma = (long) karma;
				
				
			}else{
			
				Runnable rnn = new Runnable(){
					
					@Override
					public void run(){
						try {
							HiveAPI.updateKarma();
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				
			ExecutorService exec = Executors.newCachedThreadPool();
			exec.submit(rnn);
			exec.shutdown();
			try {
				exec.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}
		else if(message.contains("§cLost §e20§c karma") && gameMode != null){
			TIMV.minus20();
			if(TIMV.role.equals("Detective")){
				TIMV.applyPoints(-1, "i");
			}
			else{
				TIMV.applyPoints(-1);
			}
	
		}
		else if(message.contains("§cLost §e40§c karma") && gameMode != null){
			TIMV.minus40();
			if(TIMV.role.equals("Traitor")){
				TIMV.applyPoints(-2, "t");
			}else{
			TIMV.applyPoints(-2, "d");
			}
		}
		else if(message.contains("§aAwarded §e20§a karma") && gameMode != null){
			TIMV.plus20();
			if(TIMV.role.equals("Traitor")){
			TIMV.applyPoints(2);
			}else{
				TIMV.applyPoints(1);
			}
			
			
		}
		else if(message.contains("§aAwarded §e10§a karma") && gameMode != null){
			TIMV.plus10();
			TIMV.applyPoints(1);
		}
		else if(message.contains("§aAwarded §e25§a karma") && gameMode != null){
			TIMV.plus25();
			TIMV.applyPoints(2); //+1 Det point
			
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
		else if(message.startsWith(ChatColor.AQUA + "Karma:")){
			String[] contents = message.split(":");
			String karma1 = ChatColor.stripColor(contents[1].trim());
			long karma = Long.valueOf(karma1);
			TIMVRank rank = TIMVRank.getFromDisplay(HiveAPI.getRank(TIMV.lastRecords));
			String title = rank.getTotalDisplay();
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Karma: " + ChatColor.YELLOW + karma + " (" + title +  ChatColor.YELLOW + ")");
			
			return true;
			
			
		}
		else if(message.startsWith(ChatColor.AQUA + "Role points:")){
			
			//Better /records
			
			Runnable rnn = new Runnable(){
				@Override
				public void run(){
			try{
			long mp = HiveAPI.getKarmaPerGame(TIMV.lastRecords);
			long rp = HiveAPI.getRolepoints(TIMV.lastRecords);
			long karma = HiveAPI.getKarma(TIMV.lastRecords);
			int ach = HiveAPI.getAchievements(TIMV.lastRecords);
			
			
			double krr = (double)Math.round(((double)karma / (double)rp) * 100d) / 100d;
			
			
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Most Points: " + ChatColor.YELLOW + mp);
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "K/R: " + ChatColor.YELLOW + krr);
			The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Achievements: " + ChatColor.YELLOW + ach + "/41");
			// TODO may want to pull the maxachievements(/41) from the API.
			}
			catch(Exception e){
				e.printStackTrace();
			}
				}
			};
			
			ExecutorService exec = Executors.newCachedThreadPool();
			exec.submit(rnn);
			exec.shutdown();
			try {
				exec.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6The body of §4")){
			TIMV.traitorsDiscovered++;
			
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §7You are a")){
			gameMode.setState(GameState.GAME);
			TIMV.calculateTraitors(The5zigAPI.getAPI().getServerPlayers().size());
			The5zigAPI.getLogger().info("(" + message + ")");
			
			String role = "";
			if(message.contains("§aINNOCENT§7")){
				role = "Innocent";
			}
			else if(message.contains("§cTRAITOR§7")){
				role = "Traitor";
			}
			else if(message.contains("§9DETECTIVE§7")){
				role = "Detective";
			}
			TIMV.role = role;
		}
		
		
		
		
		
		return false;
	}

	@Override
	public void onServerConnect(TIMV gameMode) {
		TIMV.reset(gameMode);
		
		
	}
	
	
	
	
	
	
	

	

}
