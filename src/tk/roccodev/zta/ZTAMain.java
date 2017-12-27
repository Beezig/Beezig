package tk.roccodev.zta;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.*;
import eu.the5zig.mod.event.EventHandler.Priority;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.util.minecraft.ChatColor;
import org.bstats.MetricsLite;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.autovote.watisdis;
import tk.roccodev.zta.command.*;
import tk.roccodev.zta.games.*;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.stuff.bed.StreakUtils;
import tk.roccodev.zta.hiveapi.stuff.dr.DRMap;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiDR;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiTIMV;
import tk.roccodev.zta.notes.NotesManager;
import tk.roccodev.zta.settings.SettingsFetcher;
import tk.roccodev.zta.updater.Updater;
import tk.roccodev.zta.utils.TIMVDay;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Plugin(name="Beezig", version="4.3.0")
public class ZTAMain {
	
	public static List<Class<?>> services = new ArrayList<Class<?>>();
	
	
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
			if(Updater.isVersionBlacklisted(getCustomVersioning()) && !ZTAMain.class.getAnnotation(Plugin.class).version().contains("experimental")){
				new Thread(new Runnable(){
					@Override
					public void run(){
						try {
							TimeUnit.SECONDS.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						The5zigAPI.getLogger().error("Beezig: This version is disabled!");
						news.displayMessage("Beezig: Version is disabled!", "Please update to the latest version.");
					}
				}).start();
				return; //< one does not simply update beezig
			}
		} catch (IOException e) {
			The5zigAPI.getLogger().info("Failed checking for blacklist");
			e.printStackTrace();
		}
		try {
			if(Updater.checkForUpdates() && !ZTAMain.class.getAnnotation(Plugin.class).version().contains("experimental")){
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
		The5zigAPI.getAPI().registerModuleItem(this, "timvdailykarma", tk.roccodev.zta.modules.timv.DailyKarmaItem.class, "serverhivemc");
		
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
		The5zigAPI.getAPI().registerModuleItem(this, "beddeaths", tk.roccodev.zta.modules.bed.DeathsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedkdrchange", tk.roccodev.zta.modules.bed.KDRChangeItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedteamsleft", tk.roccodev.zta.modules.bed.TeamsLeftItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedsummoners", tk.roccodev.zta.modules.bed.SummonersItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedwinstreak", tk.roccodev.zta.modules.bed.WinstreakItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "globalmedals", tk.roccodev.zta.modules.global.MedalsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "globaltokens", tk.roccodev.zta.modules.global.TokensItem.class , "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "gntmode", tk.roccodev.zta.modules.gnt.ModeItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntteam", tk.roccodev.zta.modules.gnt.TeamItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntmap", tk.roccodev.zta.modules.gnt.MapItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntkills", tk.roccodev.zta.modules.gnt.KillsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntdeaths", tk.roccodev.zta.modules.gnt.DeathsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntkdrchange", tk.roccodev.zta.modules.gnt.KDRChangeItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntpoints", tk.roccodev.zta.modules.gnt.PointsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntgiantkills", tk.roccodev.zta.modules.gnt.GiantKillsItem.class , "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntgold", tk.roccodev.zta.modules.gnt.GoldItem.class, "serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "hidemap", tk.roccodev.zta.modules.hide.MapItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "hidepoints", tk.roccodev.zta.modules.hide.PointsItem.class, "serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "caimap", tk.roccodev.zta.modules.cai.MapItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "caipoints", tk.roccodev.zta.modules.cai.PointsItem.class, "serverhivemc");
		
		
		The5zigAPI.getAPI().registerServerInstance(this, IHive.class);	
		
		CommandManager.registerCommand(new NotesCommand());
		CommandManager.registerCommand(new AddNoteCommand());
		CommandManager.registerCommand(new SayCommand());
		CommandManager.registerCommand(new SettingsCommand());
		CommandManager.registerCommand(new MedalsCommand());
		CommandManager.registerCommand(new TokensCommand());
		CommandManager.registerCommand(new PBCommand());
		CommandManager.registerCommand(new WRCommand());
		CommandManager.registerCommand(new DebugCommand());
		CommandManager.registerCommand(new ColorDebugCommand());
		CommandManager.registerCommand(new MonthlyCommand());
		CommandManager.registerCommand(new AutoVoteCommand());
		CommandManager.registerCommand(new ShrugCommand());
		CommandManager.registerCommand(new MathCommand());
		CommandManager.registerCommand(new MessageOverlayCommand());
		CommandManager.registerCommand(new ReVoteCommand());
		CommandManager.registerCommand(new CheckPingCommand());
		CommandManager.registerCommand(new BlockstatsCommand());
		
		if(The5zigAPI.getAPI().getGameProfile().getId().toString().equals("8b687575-2755-4506-9b37-538b4865f92d") || 
				The5zigAPI.getAPI().getGameProfile().getId().toString().equals("bba224a2-0bff-4913-b042-27ca3b60973f")){
			CommandManager.registerCommand(new RealRankCommand());
			CommandManager.registerCommand(new SeenCommand());			
		}
			

		
	
		
		The5zigAPI.getLogger().info("Loaded Beezig");
		
		The5zigAPI.getLogger().info("Loading bStats");
		MetricsLite metrics = new MetricsLite(this);
		The5zigAPI.getLogger().info("Loaded bStats");
		
		String OS = System.getProperty("os.name").toLowerCase();
		try{
		if (OS.contains("mac")) {
		    mcFile = new File(System.getProperty("user.home") + "/Library/Application Support/minecraft/5zigtimv");
		} else if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
			mcFile = new File(System.getProperty("user.home") + "/.minecraft/5zigtimv");
		} else if (OS.contains("win")) {
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
		checkForFileExist(new File(mcFile + "/timv/dailykarma/"), true);
		checkForFileExist(new File(mcFile + "/bedwars/"), true);
		checkForFileExist(new File(mcFile + "/bedwars/streak.txt"), false);
		StreakUtils.init();
		
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
		watisdis.wat = new ApiTIMV("RoccoDev").getTitle();

		ApiHiveGlobal api = new ApiHiveGlobal(The5zigAPI.getAPI().getGameProfile().getName());
		playerRank = api.getNetworkTitle();
		
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
	
		TIMV.setDailyKarmaFileName(TIMVDay.fromCalendar(Calendar.getInstance()) + ".txt");
		
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.DAY_OF_MONTH) == 0x1E && cal.get(Calendar.MONTH) == 0xA){
			NotesManager.HR1cm5z = true; //Hbd
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
		return playerRank.equalsIgnoreCase("Hive Founder and Owner");

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
		if(!MessageOverlayCommand.toggledName.isEmpty() && !evt.getMessage().startsWith("/")){
			evt.setCancelled(true);
			The5zigAPI.getAPI().sendPlayerMessage("/msg " + MessageOverlayCommand.toggledName + " " + evt.getMessage());
		}
		if(evt.getMessage().toUpperCase().trim().equals("/P")){
			MessageOverlayCommand.toggledName = "";
		}
		if(evt.getMessage().toUpperCase().startsWith("/RECORDS") || evt.getMessage().toUpperCase().startsWith("/STATS")){
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
				} else if(ActiveGame.is("gnt") || ActiveGame.is("gntm")){
					if(Giant.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					Giant.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				}  else if(ActiveGame.is("hide")){
					if(HIDE.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					HIDE.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				}
				else if(ActiveGame.is("cai")){
					if(CAI.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					CAI.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
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
				else if(ActiveGame.is("gnt") || ActiveGame.is("gntm")){
					if(Giant.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					Giant.lastRecords = args[1].trim();	
				}
				else if(ActiveGame.is("hide")){
					if(HIDE.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					HIDE.lastRecords = args[1].trim();	
				}
				else if(ActiveGame.is("cai")){
					if(CAI.isRecordsRunning){
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					CAI.lastRecords = args[1].trim();	
				}
			}
		}
		
	}
	
	@EventHandler(priority=Priority.HIGHEST)
	public void onDisconnect(ServerQuitEvent evt){
		NotesManager.notes.clear();
		if(ActiveGame.current() == null || ActiveGame.current().isEmpty()) return;
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					StreakUtils.saveDailyStreak();
					
					
					String className = ActiveGame.current().toUpperCase();
					if(className.startsWith("GNT")) className = "Giant";
					Class gameModeClass = Class.forName("tk.roccodev.zta.games." + className);
					Method resetMethod = gameModeClass.getMethod("reset", gameModeClass);
					resetMethod.invoke(null, gameModeClass.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	


	@EventHandler(priority = EventHandler.Priority.LOW)
	public void onTitle(TitleEvent evt){
		//Map fallback
		if(ActiveGame.is("dr") && DR.activeMap == null){
			String map = ChatColor.stripColor(evt.getTitle());
			if(map.equals("HiveMC.EU")) return;
			if(map.equals("play.HiveMC.com")) return;
			The5zigAPI.getLogger().info("FALLBACK MAP=" + map);
			DR.activeMap = DRMap.getFromDisplay(map);
		    
		    new Thread(new Runnable(){
		    	@Override
			    public void run(){
		    		ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
			    if(DR.currentMapPB == null ){
			    	The5zigAPI.getLogger().info("Loading PB...");
			    	DR.currentMapPB = api.getPersonalBest(DR.activeMap);		
			    }
			    if(DR.currentMapWR == null ){
			    	The5zigAPI.getLogger().info("Loading WR...");
			    	DR.currentMapWR = api.getWorldRecord(DR.activeMap);
			    }
			    if(DR.currentMapWRHolder == null ){
			    	The5zigAPI.getLogger().info("Loading WRHolder...");
			    	DR.currentMapWRHolder = api.getWorldRecordHolder(DR.activeMap);
			    }
		    	}
		    }).start();
		}
	}
	
	
	
	@EventHandler
	public void onChat(ChatEvent evt){
		
		if(evt.getMessage() != null){
			if(The5zigAPI.getAPI().getActiveServer() instanceof IHive) {
				if(ZTAMain.isColorDebug)
				The5zigAPI.getLogger().info("Global Color Debug: (" + evt.getMessage() + ")");
			}
			if(ChatColor.stripColor(evt.getMessage().trim()).equals("▍ Friends ▏ ✚ Toccata")){
				NotesManager.tramontoccataStelle();
			}
		}
		
	}
	
	@EventHandler
	public void onActionBar(ActionBarEvent bar){
		//The5zigAPI.getLogger().info(bar.getMessage());
		if((ActiveGame.is("gnt") || ActiveGame.is("gntm")) && bar.getMessage().contains("❂")){
			//§6❂ §e12§7  ❘  §c§lDestructible Land§7  ❘  §f0§b Kills
			Giant.gold = Integer.parseInt(ChatColor.stripColor(bar.getMessage().split("❘")[0].replaceAll("❂", "")).trim());
		}
	}
}
	
	

	
	


	


