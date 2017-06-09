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
import tk.roccodev.zta.command.AddNoteCommand;
import tk.roccodev.zta.command.NotesCommand;
import tk.roccodev.zta.command.PBCommand;
import tk.roccodev.zta.command.RealRankCommand;
import tk.roccodev.zta.command.SayCommand;
import tk.roccodev.zta.command.SeenCommand;
import tk.roccodev.zta.command.SettingsCommand;
import tk.roccodev.zta.command.WRCommand;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.notes.NotesManager;
import tk.roccodev.zta.settings.SettingsFetcher;
import tk.roccodev.zta.updater.Updater;

@Plugin(name="Beezig", version="4.0.0")
public class ZTAMain {
	
	public static List<Class<?>> services = new ArrayList<Class<?>>();
	public static boolean isTIMV = false;
	public static boolean isDR = false;
	public static IKeybinding notesKb;
	public static File mcFile;
	
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
				
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(Updater.checkForUpdates()){
				The5zigAPI.getLogger().fatal("Beezig: A new version of the plugin is available!");
				news.displayMessage("Beezig: A new version of the plugin is available!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		The5zigAPI.getAPI().registerModuleItem(this, "drkills", tk.roccodev.zta.modules.dr.KillsItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drdeaths", tk.roccodev.zta.modules.dr.DeathsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drpb", tk.roccodev.zta.modules.dr.PBItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drwr", tk.roccodev.zta.modules.dr.WRItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerServerInstance(this, IHive.class);	
		
		CommandManager.registerCommand(new NotesCommand());
		CommandManager.registerCommand(new AddNoteCommand());
		CommandManager.registerCommand(new SayCommand());
		CommandManager.registerCommand(new SettingsCommand());
		CommandManager.registerCommand(new RealRankCommand());
		CommandManager.registerCommand(new SeenCommand());
		CommandManager.registerCommand(new PBCommand());
		CommandManager.registerCommand(new WRCommand());
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			SettingsFetcher.loadSettings();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	
	
	@EventHandler(priority = EventHandler.Priority.HIGH)
	public void onChatSend(ChatSendEvent evt){
		if(evt.getMessage().startsWith("/") && !evt.getMessage().startsWith("/ ")){
			
			if(CommandManager.dispatchCommand(evt.getMessage())){
				evt.setCancelled(true);
				return;
			}
			
			
		}
		if(evt.getMessage().startsWith("/records") || evt.getMessage().startsWith("/stats")){
			String[] args = evt.getMessage().split(" ");
			if(args.length == 1){
				if(isTIMV){
					TIMV.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if(isDR){
					DR.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				}
			}
			else{
				if(isTIMV){
					TIMV.lastRecords = args[1].trim();
				}
				else if(isDR){
					DR.lastRecords = args[1].trim();	
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
	if(evt.getKeyCode() == notesKb.getKeyCode() && notesKb.isPressed() && The5zigAPI.getAPI().getActiveServer() instanceof IHive && isTIMV){
		The5zigAPI.getAPI().messagePlayer(Log.info + "Notes:");
		for(String s : NotesManager.notes){
			The5zigAPI.getAPI().messagePlayer("§e - §r" + s);
		}
	}
}
	@EventHandler(priority = EventHandler.Priority.LOW)
	public void onTitle(TitleEvent evt){
		//Map fallback
		if(ZTAMain.isDR && DR.activeMap == null){
			String map = ChatColor.stripColor(evt.getTitle());
			The5zigAPI.getLogger().info("FALLBACK MAP=" + map);
		    DRMap map1 = DRMap.getFromDisplay(map);
		    DR.activeMap = map1;
		    if(DR.currentMapPB == null ){
		    	The5zigAPI.getLogger().info("Loading PB...");
				DR.currentMapPB = HiveAPI.DRgetPB(The5zigAPI.getAPI().getGameProfile().getName(), DR.activeMap);			
			}
		    if(DR.currentMapWR == null ){
		    The5zigAPI.getLogger().info("Loading WR...");
			DR.currentMapWR = HiveAPI.DRgetWR(DR.activeMap);
		    }
		}
	}
}
	
	

	
	


	


