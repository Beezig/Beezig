package tk.roccodev.zta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.modules.Category;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.mod.util.IKeybinding;
import tk.roccodev.zta.modules.*;
import tk.roccodev.zta.notes.NotesManager;
import tk.roccodev.zta.updater.Updater;

@Plugin(name="TIMVPlugin", version="3.2.0")
public class ZTAMain {
	
	public static List<Class<?>> services = new ArrayList<Class<?>>();
	public static boolean isTIMV = false;
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
				The5zigAPI.getLogger().fatal("TIMV: This version is disabled!");
				news.displayMessage("TIMV: Version is disabled remotely! Update to the latest version.");
				
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(Updater.checkForUpdates()){
				The5zigAPI.getLogger().fatal("TIMV: A new version of the module is available!");
				news.displayMessage("TIMV: A new version of the module is available!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		The5zigAPI.getLogger().info("Loading TIMVPlugin");
		The5zigAPI.getAPI().registerModuleItem(this, "karma", KarmaItem.class, Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerModuleItem(this, "karmacounter", KarmaCounterItem.class, Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerModuleItem(this, "timvmap", MapItem.class, Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerModuleItem(this, "bodies", BodiesItem.class, Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerModuleItem(this, "dbodies", DBodiesItem.class,Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerServerInstance(this, IHive.class);
		 ZTAMain.notesKb = The5zigAPI.getAPI().registerKeyBinding("TIMV: Show /notes", Keyboard.KEY_X, "TIMV Plugin");

		The5zigAPI.getLogger().info("Loaded TIMVPlugin");
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
		
		File csvFile = new File(mcFile + "/5zigtimv/games.csv");
		
	}
	
	
	
	@EventHandler(priority = EventHandler.Priority.HIGH)
	public void onChatSend(ChatSendEvent evt){
		if(evt.getMessage().startsWith("/records") || evt.getMessage().startsWith("/stats")){
			String[] args = evt.getMessage().split(" ");
			if(args.length == 1){
				TIMV.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
			}
			else{
				TIMV.lastRecords = args[1].trim();
			}
		}
		
		else if(evt.getMessage().trim().equalsIgnoreCase("/notes") && The5zigAPI.getAPI().getActiveServer() instanceof IHive && isTIMV){
			evt.setCancelled(true);
			The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eNotes:");
			for(String s : NotesManager.notes){
				The5zigAPI.getAPI().messagePlayer("§e - §r" + s);
			}
		}
		
		else if(evt.getMessage().startsWith("/addnote") && The5zigAPI.getAPI().getActiveServer() instanceof IHive && isTIMV){
			evt.setCancelled(true);			
			String args = evt.getMessage().substring(8).trim();
			if( args.length() == 0 ){
				The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eNote may not be empty.");
			}
			else {
				int letter8 = evt.getMessage().codePointAt(8);	
				// 32 is a space.
				if( letter8 != 32){
					The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eCommand is '/addnote [note]'.");
				}
				else {
					NotesManager.notes.add(args);
					The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eSuccesfully added note.");
			}
		}
			
		}
		else if(evt.getMessage().startsWith("/note") && The5zigAPI.getAPI().getActiveServer() instanceof IHive && isTIMV){
			evt.setCancelled(true);
			String args = evt.getMessage().substring(5).trim();
			if( args.length() == 0 ){
				The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eNote may not be empty.");
			}
			else {
				int letter5 = evt.getMessage().codePointAt(5);
					// 32 is a space.
				if( letter5 != 32){
					The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eCommand is '/note [note]'.");
				}
			else {
					NotesManager.notes.add(args);
					The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eSuccesfully added note.");
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
		The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eNotes:");
		for(String s : NotesManager.notes){
			The5zigAPI.getAPI().messagePlayer("§e - §r" + s);
		}
	}
}
}
	
	

	
	


	


