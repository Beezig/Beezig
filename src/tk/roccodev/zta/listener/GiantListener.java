package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.GNT;
import tk.roccodev.zta.games.GNTM;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.hiveapi.GiantMap;

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
		
	}

	@Override
	public void onGameModeJoin(Giant gameMode) {
		if(this.lobby.equalsIgnoreCase("GNT")) ActiveGame.set("GNT");
		if(this.lobby.equalsIgnoreCase("GNTM")) ActiveGame.set("GNTM");
		gameMode.setState(GameState.STARTING);
		The5zigAPI.getLogger().info(instance.getName());
		
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
		return false;
	}


	@Override
	public void onServerConnect(Giant gameMode) {
		
		Giant.reset(gameMode);
		
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
