package tk.roccodev.zta;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.bstats.MetricsLite;
import org.lwjgl.input.Keyboard;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.KeyPressEvent;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.event.ServerQuitEvent;
import eu.the5zig.mod.event.TitleEvent;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.mod.util.IKeybinding;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.autovote.watisdis;
import tk.roccodev.zta.command.AddNoteCommand;
import tk.roccodev.zta.command.AutoVoteCommand;
import tk.roccodev.zta.command.ColorDebugCommand;
import tk.roccodev.zta.command.MonthlyCommand;
import tk.roccodev.zta.command.NotesCommand;
import tk.roccodev.zta.command.PBCommand;
import tk.roccodev.zta.command.RealRankCommand;
import tk.roccodev.zta.command.SayCommand;
import tk.roccodev.zta.command.SeenCommand;
import tk.roccodev.zta.command.SettingsCommand;
import tk.roccodev.zta.command.WRCommand;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.GNT;
import tk.roccodev.zta.games.GNTM;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.notes.NotesManager;
import tk.roccodev.zta.settings.SettingsFetcher;
import tk.roccodev.zta.updater.Updater;

@Plugin(name="Beezig", version="4.0.0")
public class ZTAMain {
	
	public static List<Class<?>> services = new ArrayList<Class<?>>();
	
	public static IKeybinding notesKb;
	public static File mcFile;
	public static boolean isColorDebug = false;
	public static String playerRank = "";
	
	public static int getCustomVersioning(){
		String v = ZTAMain.class.getAnnotation(Plugin.class).version();
		String toParse = v.replaceAll("\\.", "");
		return Integer.parseInt(toParse);
	}
	
	@EventHandler(priority = EventHandler.Priority.LOW)
	public void onLoad(LoadEvent event) { 

		IOverlay news = The5zigAPI.getAPI().createOverlay();
		try {
			if(Updater.isVersionBlacklisted(getCustomVersioning())){
				The5zigAPI.getLogger().fatal("Beezig: This version is disabled!");
				news.displayMessage("Beezig: Version is disabled remotely! Update to the latest version.");
				
				return; //< one does not simply update beezig
			}
		} catch (IOException e) {
			The5zigAPI.getLogger().info("Failed checking for blacklist");
			e.printStackTrace();
		}
		try {
			if(Updater.checkForUpdates()){
				The5zigAPI.getLogger().fatal("Beezig: A new version of the plugin is available!");
				news.displayMessage("Beezig: A new version of the plugin is available!");
			}
		} catch (Exception e) {
			The5zigAPI.getLogger().info("Failed update check");
			e.printStackTrace();
		}
		
		The5zigAPI.getLogger().info("Loading Beezig");
		
		The5zigAPI.getAPI().registerModuleItem(this, "karma", tk.roccodev.zta.modules.timv.KarmaItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "karmacounter", tk.roccodev.zta.modules.timv.KarmaCounterItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "timvmap", tk.roccodev.zta.modules.timv.MapItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bodies", tk.roccodev.zta.modules.timv.BodiesItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "dbodies", tk.roccodev.zta.modules.timv.DBodiesItem.class, "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "drmap", tk.roccodev.zta.modules.dr.MapItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drrole", tk.roccodev.zta.modules.dr.RoleItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drpoints", tk.roccodev.zta.modules.dr.PointsItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drdeaths", tk.roccodev.zta.modules.dr.DeathsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drpb", tk.roccodev.zta.modules.dr.PBItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drwr", tk.roccodev.zta.modules.dr.WRItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "bedpoints", tk.roccodev.zta.modules.bed.PointsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedresources", tk.roccodev.zta.modules.bed.ResourcesItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedmap", tk.roccodev.zta.modules.bed.MapItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedteam", tk.roccodev.zta.modules.bed.TeamItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedkills", tk.roccodev.zta.modules.bed.KillsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedgamecounter", tk.roccodev.zta.modules.bed.PointsCounterItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "beddestroyed", tk.roccodev.zta.modules.bed.BedsDestroyedItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "globalmedals", tk.roccodev.zta.modules.global.MedalsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "globaltokens", tk.roccodev.zta.modules.global.TokensItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "gntmode", tk.roccodev.zta.modules.gnt.ModeItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntteam", tk.roccodev.zta.modules.gnt.TeamItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerServerInstance(this, IHive.class);	
		
		CommandManager.registerCommand(new NotesCommand());
		CommandManager.registerCommand(new AddNoteCommand());
		CommandManager.registerCommand(new SayCommand());
		CommandManager.registerCommand(new SettingsCommand());
		CommandManager.registerCommand(new RealRankCommand());
		CommandManager.registerCommand(new SeenCommand());
		CommandManager.registerCommand(new PBCommand());
		CommandManager.registerCommand(new WRCommand());
		CommandManager.registerCommand(new ColorDebugCommand());
		CommandManager.registerCommand(new MonthlyCommand());
		CommandManager.registerCommand(new AutoVoteCommand());
		
		ZTAMain.notesKb = The5zigAPI.getAPI().registerKeyBinding("TIMV: Show /notes", Keyboard.KEY_X, "TIMV Plugin");

		The5zigAPI.getLogger().info("Loaded Beezig");
		
		The5zigAPI.getLogger().info("Loading bStats");
		MetricsLite metrics = new MetricsLite(this);
		The5zigAPI.getLogger().info("Loaded bStats");
		
		String OS = System.getProperty("os.name").toLowerCase();
		try{
		if (OS.indexOf("mac") >= 0) {
		    mcFile = new File(System.getProperty("user.home") + "/Library/Application Support/minecraft/5zigtimv");
		} else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
			mcFile = new File(System.getProperty("user.home") + "/.minecraft/5zigtimv");
		} else if (OS.indexOf("win") >= 0) {
		    mcFile = new File(System.getenv("APPDATA") + "/.minecraft/5zigtimv");
		} else {
		   mcFile = new File(System.getProperty("user.home") + "/Minecraft5zig/5zigtimv");
		}
		}catch(Exception e){
			 mcFile = new File(System.getProperty("user.home") + "/Minecraft5zig/5zigtimv");
		}
		if(!mcFile.exists()) mcFile.mkdir();
		The5zigAPI.getLogger().info("MC Folder is at: " + mcFile.getAbsolutePath());
		checkForFileExist(new File(mcFile + "/timv/"), true);
		checkOldCsvPath();
		File settingsFile = new File(ZTAMain.mcFile.getAbsolutePath() + "/settings.properties");
		if(!settingsFile.exists()){
			try {
				settingsFile.createNewFile();
				SettingsFetcher.saveSettings();
			} catch (IOException e) {
				The5zigAPI.getLogger().info("Failed saving new Settings");
				e.printStackTrace();
			}
		}
		try {
			SettingsFetcher.loadSettings();
		} catch (IOException e1) {
			The5zigAPI.getLogger().info("Failed to load Settings");
			e1.printStackTrace();
		}
		
		checkForFileExist(new File(mcFile + "/autovote.yml"), false);
		AutovoteUtils.load();
		watisdis.wat = HiveAPI.TIMVgetRank("RoccoDev");
		
		playerRank = HiveAPI.getNetworkRank(The5zigAPI.getAPI().getGameProfile().getName());
		
		try {
			HiveAPI.updateMedals();
			HiveAPI.updateTokens();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Instantiate GNT Classes
		new Giant();
		new GNT();
		new GNTM();
	
		
		
			
	}
	
	private void checkForFileExist(File f, boolean directory) {
		if(!f.exists())
			try {
				if(directory) {
					f.mkdir();
					
				}
				else{
					f.createNewFile();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void checkOldCsvPath(){
		File oldPath = new File(mcFile + "/games.csv");
		File newPath = new File(mcFile + "/timv/games.csv");
		if(oldPath.exists() && newPath.exists()){
			return;
		}
		else if(oldPath.exists() && !newPath.exists()){
			The5zigAPI.getLogger().info("games.csv in 5zigtimv/ directory found! Migrating...");
			checkForFileExist(new File(mcFile + "/timv/"), true);
			try {
				newPath.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Files.move(FileSystems.getDefault().getPath(oldPath.getAbsolutePath()), FileSystems.getDefault().getPath(mcFile.getAbsolutePath() + "/timv/"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			The5zigAPI.getLogger().info("Migration complete!");
		}
		
	}
	
	private boolean isStaffChat(){
		if(playerRank.endsWith("Hive Moderator")) return true;
		if(playerRank.equalsIgnoreCase("Hive Developer")) return true;
		if(playerRank.equalsIgnoreCase("Hive Founder and Owner")) return true;
	
		return false;
	
	}
	
	
	@EventHandler(priority = EventHandler.Priority.HIGH)
	public void onChatSend(ChatSendEvent evt){
		if(evt.getMessage().startsWith("*") && isStaffChat()){
			String noStar = evt.getMessage().replaceAll("\\*", "");
			if(noStar.length() == 0) return;
			The5zigAPI.getAPI().sendPlayerMessage("/s " + noStar);
			
			return;
		}
		if(evt.getMessage().startsWith("/") && !evt.getMessage().startsWith("/ ")){
			
			if(CommandManager.dispatchCommand(evt.getMessage())){
				evt.setCancelled(true);
				return;
			}
			
			
		}
		if(evt.getMessage().startsWith("/records") || evt.getMessage().startsWith("/stats")){
			String[] args = evt.getMessage().split(" ");
			if(args.length == 1){
				if(ActiveGame.is("timv")){
					if(TIMV.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					TIMV.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if(ActiveGame.is("dr")){
					if(DR.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					DR.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if(ActiveGame.is("bed")){
					if(BED.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					BED.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				}
				
			}
			else{
				if(ActiveGame.is("timv")){
					if(TIMV.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					TIMV.lastRecords = args[1].trim();
				}
				else if(ActiveGame.is("dr")){
					if(DR.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					DR.lastRecords = args[1].trim();	
				}
				else if(ActiveGame.is("bed")){
					if(BED.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					BED.lastRecords = args[1].trim();	
				}
			}
		}
		
	}
	
	@EventHandler
	public void onDisconnect(ServerQuitEvent evt){
		NotesManager.notes.clear();
	}
	
	

@EventHandler
public void onKeypress(KeyPressEvent evt){
	if(evt.getKeyCode() == notesKb.getKeyCode() && notesKb.isPressed() && The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("TIMV")){
		The5zigAPI.getAPI().messagePlayer(Log.info + "Notes:");
		for(String s : NotesManager.notes){
			The5zigAPI.getAPI().messagePlayer("§e - §r" + s);
		}
	}
}
	@EventHandler(priority = EventHandler.Priority.LOW)
	public void onTitle(TitleEvent evt){
		//Map fallback
		if(ActiveGame.is("dr") && DR.activeMap == null){
			String map = ChatColor.stripColor(evt.getTitle());
			The5zigAPI.getLogger().info("FALLBACK MAP=" + map);
		    DRMap map1 = DRMap.getFromDisplay(map);
		    DR.activeMap = map1;
		    
		    new Thread(new Runnable(){
		    	@Override
			    public void run(){
			    if(DR.currentMapPB == null ){
			    	The5zigAPI.getLogger().info("Loading PB...");
			    	DR.currentMapPB = HiveAPI.DRgetPB(The5zigAPI.getAPI().getGameProfile().getName(), DR.activeMap);			
			    }
			    if(DR.currentMapWR == null ){
			    	The5zigAPI.getLogger().info("Loading WR...");
			    	DR.currentMapWR = HiveAPI.DRgetWR(DR.activeMap);
			    }
			    if(DR.currentMapWRHolder == null ){
			    	The5zigAPI.getLogger().info("Loading WRHolder...");
			    	DR.currentMapWRHolder = HiveAPI.DRgetWRHolder(DR.activeMap);
			    }
		    	}
		    }).start();
		}
	}
	
	
	
	@EventHandler
	public void onChat(ChatEvent evt){
		
		// The5zigAPI.getLogger().info("(" + evt.getMessage() + ")");
		
	}
}
	
	

	
	


	


