package tk.roccodev.zta.listener;

import java.text.DecimalFormat;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.hiveapi.GiantMap;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class GiantListener extends AbstractGameListener<Giant>{

	private Class<Giant> gameMode = Giant.class;
	private Giant instance = Giant.instance;
	public static GiantListener listener;
	private String lobby;
	
	public GiantListener(){
		listener = this;
	}
	
	
	@Override
	public Class<Giant> getGameMode() {
		// TODO Auto-generated method stub
		return Giant.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
	
		if(arg0.toUpperCase().startsWith("GNT")){ // Support for GNTM
			lobby = arg0;
			return true;
		} 
		else{
			return false;
		}
	}
	
	public void setGameMode(Class<? extends Giant> newMode, Giant instance){
		gameMode = (Class<Giant>) newMode;
		this.instance = instance;
		The5zigAPI.getLogger().info(instance.getClass());
		
	}

	@Override
	public void onGameModeJoin(Giant gameMode) {
		if(this.lobby.equalsIgnoreCase("GNT")) ActiveGame.set("GNT");
		if(this.lobby.equalsIgnoreCase("GNTM")) ActiveGame.set("GNTM");
		gameMode.setState(GameState.STARTING);
		new Thread(new Runnable(){
			@Override
			public void run(){
				String ign = The5zigAPI.getAPI().getGameProfile().getName();
				Giant.totalKills = (int) HiveAPI.getKills(ign, ActiveGame.current());
				Giant.totalDeaths = (int) HiveAPI.getDeaths(ign, ActiveGame.current());
				
				
				Giant.totalKdr = (double)Giant.totalKills / Giant.totalDeaths;
				Giant.gameKdr = new Double(Giant.totalKdr);
				The5zigAPI.getLogger().info(Giant.totalKdr);
				
			}
		}).start();
		
		The5zigAPI.getLogger().info(instance.getName());
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
					
						
						
							try {
								
								HiveAPI.GiantupdatePoints(instance.isMini());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
				
			
		}}).start();
	}

	
	
	

	@Override
	public boolean onServerChat(Giant gameMode, String message) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info(gameMode.getName() + " Color Debug: (" + message + ")");
		}
		
		if(message.startsWith(getPrefix(ActiveGame.current()) + "§3You are now playing on the ")){
			String team = message.replaceAll(getPrefix(ActiveGame.current()) + "§3You are now playing on the ", "").replaceAll("Team!", "");
			Giant.team = team;
			gameMode.setState(GameState.GAME);
		}
		else if(message.startsWith(getPrefix(ActiveGame.current()) + "§3Voting has ended! §bThe map §f")){
			String data[] = message.replaceAll(getPrefix(ActiveGame.current()) + "§3Voting has ended! §bThe map §f", "").split("§b");
			String mapString = data[0].trim();
			The5zigAPI.getLogger().info(mapString.trim() + " / " + ActiveGame.is("GNTM"));
			GiantMap map = GiantMap.get(ChatColor.stripColor(mapString.trim()), ActiveGame.is("GNTM") ? true : false);
			Giant.activeMap = map;
			The5zigAPI.getLogger().info(map.getDisplay());
			
		}
		else if(message.startsWith(getPrefix(ActiveGame.current()) + "§a✚ §3You gained") && message.contains("for killing")){
			if(message.contains("as a team")){
				Giant.giantKills++; // Giant kill
			} else { 
			 Giant.gameKills++;
			 Giant.gameKdr = ((double)(Giant.totalKills + Giant.gameKills) / (double)(Giant.gameDeaths + Giant.totalDeaths == 0 ? 1 : Giant.gameDeaths + Giant.totalDeaths));
			}
		}
		return false;
	}


	@Override
	public void onServerConnect(Giant gameMode) {
		if(instance != null){
			Giant.reset(instance);
		}else{
		Giant.reset(gameMode);
		}
		
	}
	
	
	
	@Override
	public void onTitle(Giant gameMode, String title, String subTitle) {
		if(subTitle != null){
			
			if(ChatColor.stripColor(subTitle).equalsIgnoreCase("Respawning in 3 seconds")){
				Giant.gameDeaths++;
				Giant.gameKdr = ((double)((double)Giant.totalKills + (double)Giant.gameKills) / (double)((double)Giant.gameDeaths + (double)Giant.totalDeaths == 0 ? 1 :  (double)Giant.gameDeaths +  (double)Giant.totalDeaths));
			}
			
		}
	}


	private String getPrefix(String mode){
		
		if(mode.equalsIgnoreCase("gnt")){
			return "§8▍ §aSky§b§lGiants§8 ▏ ";
		}
		else if(mode.equalsIgnoreCase("gntm")){
			return "§8▍ §aSky§b§lGiants§a§l:Mini§8 ▏ ";
		}
		
		return "";
	}
	
	
	
	
	

	
}
