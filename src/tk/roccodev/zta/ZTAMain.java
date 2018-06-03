package tk.roccodev.zta;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ActionBarEvent;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.EventHandler.Priority;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.event.ServerQuitEvent;
import eu.the5zig.mod.event.TitleEvent;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.util.internal.ThreadLocalRandom;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.briefing.NewsServer;
import tk.roccodev.zta.briefing.Pools;
import tk.roccodev.zta.briefing.fetcher.NewsFetcher;
import tk.roccodev.zta.command.AddNoteCommand;
import tk.roccodev.zta.command.AutoVoteCommand;
import tk.roccodev.zta.command.BeezigCommand;
import tk.roccodev.zta.command.BlockstatsCommand;
import tk.roccodev.zta.command.ChatReportCommand;
import tk.roccodev.zta.command.CheckPingCommand;
import tk.roccodev.zta.command.ClosestToWRCommand;
import tk.roccodev.zta.command.ColorDebugCommand;
import tk.roccodev.zta.command.CustomTestCommand;
import tk.roccodev.zta.command.DebugCommand;
import tk.roccodev.zta.command.LeaderboardCommand;
import tk.roccodev.zta.command.MathCommand;
import tk.roccodev.zta.command.MedalsCommand;
import tk.roccodev.zta.command.MessageOverlayCommand;
import tk.roccodev.zta.command.MonthlyCommand;
import tk.roccodev.zta.command.NotesCommand;
import tk.roccodev.zta.command.PBCommand;
import tk.roccodev.zta.command.PlayerStatsCommand;
import tk.roccodev.zta.command.RanksCommand;
import tk.roccodev.zta.command.ReVoteCommand;
import tk.roccodev.zta.command.RealRankCommand;
import tk.roccodev.zta.command.ReportCommand;
import tk.roccodev.zta.command.RigCommand;
import tk.roccodev.zta.command.SayCommand;
import tk.roccodev.zta.command.SeenCommand;
import tk.roccodev.zta.command.SettingsCommand;
import tk.roccodev.zta.command.ShrugCommand;
import tk.roccodev.zta.command.TokensCommand;
import tk.roccodev.zta.command.UUIDCommand;
import tk.roccodev.zta.command.WRCommand;
import tk.roccodev.zta.command.ZigCheckCommand;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.games.BP;
import tk.roccodev.zta.games.CAI;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.GNT;
import tk.roccodev.zta.games.GNTM;
import tk.roccodev.zta.games.GRAV;
import tk.roccodev.zta.games.Giant;
import tk.roccodev.zta.games.HIDE;
import tk.roccodev.zta.games.MIMV;
import tk.roccodev.zta.games.SGN;
import tk.roccodev.zta.games.SKY;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.StuffFetcher;
import tk.roccodev.zta.hiveapi.stuff.bed.StreakUtils;
import tk.roccodev.zta.hiveapi.stuff.grav.GRAVListenerv2;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiDR;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;
import tk.roccodev.zta.notes.NotesManager;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.settings.SettingsFetcher;
import tk.roccodev.zta.updater.Updater;
import tk.roccodev.zta.utils.ChatComponentUtils;
import tk.roccodev.zta.utils.NotificationManager;
import tk.roccodev.zta.utils.TIMVDay;
import tk.roccodev.zta.utils.TIMVTest;
import tk.roccodev.zta.utils.acr.Connector;
import tk.roccodev.zta.utils.rpc.DiscordUtils;
import tk.roccodev.zta.utils.rpc.NativeUtils;

@Plugin(name = "Beezig", version = ZTAMain.BEEZIG_VERSION)
public class ZTAMain {
	public static final String BEEZIG_VERSION = "4.8.0";
	public static String VERSION_HASH = "";
	public static String OS;
	public static boolean newUpdate;

	public static boolean hasServedNews;
	public static boolean checkForNewLR; // Login Report
	public static boolean crInteractive;
	public static String lrID;
	public static String lrRS;
	public static List<Class<?>> services = new ArrayList<Class<?>>();

	public static File mcFile;
	public static boolean isColorDebug = false;
	public static String playerRank = "";

	public static int getCustomVersioning() {
		String v = ZTAMain.class.getAnnotation(Plugin.class).version();
		String toParse = v.replaceAll("\\.", "");
		return Integer.parseInt(toParse);
	}

	@EventHandler(priority = EventHandler.Priority.LOW)
	public void onLoad(LoadEvent event) {

		IOverlay news = The5zigAPI.getAPI().createOverlay();
		try {
			if (Updater.isVersionBlacklisted(getCustomVersioning())
					&& !ZTAMain.class.getAnnotation(Plugin.class).version().contains("experimental")) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							TimeUnit.SECONDS.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						The5zigAPI.getLogger().error("Beezig: This version is disabled!");
						news.displayMessage("Beezig: Version is disabled!", "Please update to the latest version.");
					}
				}).start();
				return; // < one does not simply update beezig
			}
		} catch (IOException e) {
			The5zigAPI.getLogger().info("Failed checking for blacklist");
			e.printStackTrace();
		}
		try {
			if (Updater.checkForUpdates()
					&& !ZTAMain.class.getAnnotation(Plugin.class).version().contains("experimental")) {
				The5zigAPI.getLogger().fatal("Beezig: A new version of the plugin is available!");
				newUpdate = true;
				news.displayMessage("Beezig: A new version of the plugin is available!");
			}
		} catch (Exception e) {
			The5zigAPI.getLogger().info("Failed update check");
			e.printStackTrace();
		}

		InputStream expHash = getClass().getResourceAsStream("/version.txt");
		if (expHash != null) {

			String result = new BufferedReader(new InputStreamReader(expHash)).lines()
					.collect(Collectors.joining("\n"));

			try {
				expHash.close();

			} catch (IOException e) {

			}
			String[] data = result.split(" ");
			if (data.length != 0 && data[0].equals("experimental")) {
				VERSION_HASH = data[1].substring(0, 7);
			}

		}

		The5zigAPI.getLogger().info("Loading Beezig");
		The5zigAPI.getLogger().info("Version is " + BEEZIG_VERSION + ". Hash is " + VERSION_HASH);
		The5zigAPI.getLogger().info("Using Java version: " + Runtime.class.getPackage().getImplementationVersion());

		The5zigAPI.getAPI().registerModuleItem(this, "karma", tk.roccodev.zta.modules.timv.KarmaItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "karmacounter",
				tk.roccodev.zta.modules.timv.KarmaCounterItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "timvmap", tk.roccodev.zta.modules.timv.MapItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bodies", tk.roccodev.zta.modules.timv.BodiesItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "dbodies", tk.roccodev.zta.modules.timv.DBodiesItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "timvdailykarma",
				tk.roccodev.zta.modules.timv.DailyKarmaItem.class, "serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "drmap", tk.roccodev.zta.modules.dr.MapItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drrole", tk.roccodev.zta.modules.dr.RoleItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drpoints", tk.roccodev.zta.modules.dr.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drdeaths", tk.roccodev.zta.modules.dr.DeathsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drpb", tk.roccodev.zta.modules.dr.PBItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drwr", tk.roccodev.zta.modules.dr.WRItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "drdaily", tk.roccodev.zta.modules.dr.DailyItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "bedpoints", tk.roccodev.zta.modules.bed.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedresources", tk.roccodev.zta.modules.bed.ResourcesItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedmap", tk.roccodev.zta.modules.bed.MapItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedteam", tk.roccodev.zta.modules.bed.TeamItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedkills", tk.roccodev.zta.modules.bed.KillsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedgamecounter",
				tk.roccodev.zta.modules.bed.PointsCounterItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "beddestroyed",
				tk.roccodev.zta.modules.bed.BedsDestroyedItem.class, "serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "beddeaths", tk.roccodev.zta.modules.bed.DeathsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedkdrchange", tk.roccodev.zta.modules.bed.KDRChangeItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedteamsleft", tk.roccodev.zta.modules.bed.TeamsLeftItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bedsummoners", tk.roccodev.zta.modules.bed.SummonersItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "beddaily", tk.roccodev.zta.modules.bed.DailyItem.class,
				"serverhivemc");
		// The5zigAPI.getAPI().registerModuleItem(this, "bedwinstreak",
		// tk.roccodev.zta.modules.bed.WinstreakItem.class , "serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "globalmedals", tk.roccodev.zta.modules.global.MedalsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "globaltokens", tk.roccodev.zta.modules.global.TokensItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "gntmode", tk.roccodev.zta.modules.gnt.ModeItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntteam", tk.roccodev.zta.modules.gnt.TeamItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntmap", tk.roccodev.zta.modules.gnt.MapItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntkills", tk.roccodev.zta.modules.gnt.KillsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntdeaths", tk.roccodev.zta.modules.gnt.DeathsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntkdrchange", tk.roccodev.zta.modules.gnt.KDRChangeItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntpoints", tk.roccodev.zta.modules.gnt.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntgiantkills", tk.roccodev.zta.modules.gnt.GiantKillsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntgold", tk.roccodev.zta.modules.gnt.GoldItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gntdaily", tk.roccodev.zta.modules.gnt.DailyItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "hidemap", tk.roccodev.zta.modules.hide.MapItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "hidepoints", tk.roccodev.zta.modules.hide.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "hidedaily", tk.roccodev.zta.modules.hide.DailyItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "caimap", tk.roccodev.zta.modules.cai.MapItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "caipoints", tk.roccodev.zta.modules.cai.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "caigame", tk.roccodev.zta.modules.cai.GamePointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "caiteam", tk.roccodev.zta.modules.cai.TeamItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "caidaily", tk.roccodev.zta.modules.cai.DailyItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "skypoints", tk.roccodev.zta.modules.sky.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skyteam", tk.roccodev.zta.modules.sky.TeamItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skykills", tk.roccodev.zta.modules.sky.KillsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skymode", tk.roccodev.zta.modules.sky.ModeItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skykdr", tk.roccodev.zta.modules.sky.KDRChangeItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skygame", tk.roccodev.zta.modules.sky.GamePointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skydaily", tk.roccodev.zta.modules.sky.DailyItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "skymap", tk.roccodev.zta.modules.sky.MapItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "mimvkarma", tk.roccodev.zta.modules.mimv.KarmaItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "mimvrole", tk.roccodev.zta.modules.mimv.RoleItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "mimvmap", tk.roccodev.zta.modules.mimv.MapItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "mimvcounter", tk.roccodev.zta.modules.mimv.KarmaCounterItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "mimvdaily", tk.roccodev.zta.modules.mimv.DailyItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "gravpoints", tk.roccodev.zta.modules.grav.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "gravstages", tk.roccodev.zta.modules.grav.StagesItem.class,
				"serverhivemc");

		The5zigAPI.getAPI().registerModuleItem(this, "bppoints", tk.roccodev.zta.modules.bp.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bpcounter", tk.roccodev.zta.modules.bp.PointsCounterItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bpdaily", tk.roccodev.zta.modules.bp.DailyItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "bpsong", tk.roccodev.zta.modules.bp.SongItem.class,
				"serverhivemc");
		
		The5zigAPI.getAPI().registerModuleItem(this, "sgnpoints", tk.roccodev.zta.modules.sgn.PointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "sgngame", tk.roccodev.zta.modules.sgn.GamePointsItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "sgndaily", tk.roccodev.zta.modules.sgn.DailyItem.class,
				"serverhivemc");
		The5zigAPI.getAPI().registerModuleItem(this, "sgnmap", tk.roccodev.zta.modules.sgn.MapItem.class,
				"serverhivemc");


		The5zigAPI.getAPI().registerServerInstance(this, IHive.class);

		The5zigAPI.getAPI().getPluginManager().registerListener(this, new GRAVListenerv2());

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
		CommandManager.registerCommand(new PlayerStatsCommand());
		CommandManager.registerCommand(new CustomTestCommand());
		CommandManager.registerCommand(new ZigCheckCommand());
		CommandManager.registerCommand(new RanksCommand());
		CommandManager.registerCommand(new ClosestToWRCommand());
		CommandManager.registerCommand(new BeezigCommand());
		CommandManager.registerCommand(new ReportCommand());
		CommandManager.registerCommand(new LeaderboardCommand());
		CommandManager.registerCommand(new RigCommand());
		CommandManager.registerCommand(new UUIDCommand());
		// CommandManager.registerCommand(new ChatReportCommand());

		if (The5zigAPI.getAPI().getGameProfile().getId().toString().equals("8b687575-2755-4506-9b37-538b4865f92d")
				|| The5zigAPI.getAPI().getGameProfile().getId().toString()
						.equals("bba224a2-0bff-4913-b042-27ca3b60973f")) {
			CommandManager.registerCommand(new RealRankCommand());
			CommandManager.registerCommand(new SeenCommand());
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				DR.mapsPool = StuffFetcher.getDeathRunMaps();
				TIMV.mapsPool = StuffFetcher.getTroubleInMinevilleMaps();
				GRAV.mapsPool = StuffFetcher.getGravityMaps();
			}
		}, "Maps Fetcher").start();

		The5zigAPI.getLogger().info("Loaded BeezigCore");

		String OS = System.getProperty("os.name").toLowerCase();
		try {
			if (OS.contains("mac")) {
				ZTAMain.OS = "mac";
				mcFile = new File(System.getProperty("user.home") + "/Library/Application Support/minecraft/5zigtimv");
			} else if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
				ZTAMain.OS = "unix";
				mcFile = new File(System.getProperty("user.home") + "/.minecraft/5zigtimv");
			} else if (OS.contains("win")) {
				ZTAMain.OS = "win";
				mcFile = new File(System.getenv("APPDATA") + "/.minecraft/5zigtimv");
			} else {
				mcFile = new File(System.getProperty("user.home") + "/Minecraft5zig/5zigtimv");
			}
		} catch (Exception e) {
			mcFile = new File(System.getProperty("user.home") + "/Minecraft5zig/5zigtimv");
		}
		if (!mcFile.exists())
			mcFile.mkdir();
		The5zigAPI.getLogger().info("MC Folder is at: " + mcFile.getAbsolutePath());
		checkForFileExist(new File(mcFile + "/timv/"), true);
		checkForFileExist(new File(mcFile + "/timv/dailykarma/"), true);
		checkForFileExist(new File(mcFile + "/timv/testMessages.txt"), false);
		checkForFileExist(new File(mcFile + "/bedwars/"), true);
		checkForFileExist(new File(mcFile + "/bedwars/streak.txt"), false);

		checkForFileExist(new File(mcFile + "/bp/"), true);
		checkForFileExist(new File(mcFile + "/bp/dailyPoints/"), true);

		checkForFileExist(new File(mcFile + "/cai/"), true);
		checkForFileExist(new File(mcFile + "/cai/dailyPoints/"), true);

		checkForFileExist(new File(mcFile + "/bedwars/dailyPoints/"), true);

		checkForFileExist(new File(mcFile + "/sky/"), true);
		checkForFileExist(new File(mcFile + "/sky/dailyPoints/"), true);

		checkForFileExist(new File(mcFile + "/hide/"), true);
		checkForFileExist(new File(mcFile + "/hide/dailyPoints/"), true);

		checkForFileExist(new File(mcFile + "/mimv/"), true);
		checkForFileExist(new File(mcFile + "/mimv/dailyPoints/"), true);

		checkForFileExist(new File(mcFile + "/dr/"), true);
		checkForFileExist(new File(mcFile + "/dr/dailyPoints/"), true);
		
		checkForFileExist(new File(mcFile + "/gnt/"), true);
		checkForFileExist(new File(mcFile + "/gnt/dailyPoints/"), true);
		
		checkForFileExist(new File(mcFile + "/sgn/"), true);
		checkForFileExist(new File(mcFile + "/sgn/dailyPoints/"), true);
		
		StreakUtils.init();
		new Thread(new Runnable() {
			@Override
			public void run() {
				File f = new File(mcFile + "/lastlogin.txt");
				long lastLogin = 0;
				if (!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						ArrayList<String> bloccs = new ArrayList<String>(
								Files.readAllLines(Paths.get(f.getPath())).stream().collect(Collectors.toList()));
						lastLogin = Long.parseLong(bloccs.get(0));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				List<String> lines = new ArrayList<String>(Arrays.asList(System.currentTimeMillis() + ""));
				try {
					Files.write(Paths.get(f.getPath()), lines, Charset.forName("UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Pools.newsPool = NewsFetcher.getApplicableNews(lastLogin);
				Pools.newMapsPool = NewsFetcher.getApplicableNewMaps(lastLogin);
				Pools.staffPool = NewsFetcher.getApplicableStaffUpdates(lastLogin);

			}
		}, "News Fetcher").start();

		try {
			TIMVTest.init();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		checkOldCsvPath();
		The5zigAPI.getLogger().info("Loading BeezigConfig...");
		File settingsFile = new File(ZTAMain.mcFile.getAbsolutePath() + "/settings.properties");
		if (!settingsFile.exists()) {
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
		// watisdis.wat = new ApiTIMV("RoccoDev").getTitle();

		ApiHiveGlobal api = new ApiHiveGlobal(The5zigAPI.getAPI().getGameProfile().getName());
		playerRank = api.getNetworkTitle();

		try {
			HiveAPI.updateMedals();
			HiveAPI.updateTokens();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		The5zigAPI.getLogger().info("Loaded BeezigConfig.");
		if (Setting.DISCORD_RPC.getValue()) {
			try {
				String OS1 = System.getProperty("os.name").toLowerCase();
				if (OS1.contains("mac")) {
					NativeUtils.loadLibraryFromJar("/libraries/darwin/libdiscord-rpc.dylib");
				} else if (OS1.contains("nix") || OS1.contains("nux") || OS1.indexOf("aix") > 0) {
					NativeUtils.loadLibraryFromJar("/libraries/linux-x86-64/libdiscord-rpc.so");
				} else if (OS1.contains("win")) {
					if (System.getProperty("os.arch").equals("x86")) {
						NativeUtils.loadLibraryFromJar("/libraries/win32-x86/discord-rpc.dll");
					} else {
						NativeUtils.loadLibraryFromJar("/libraries/win32-x86-64/discord-rpc.dll");
					}

				} else {
					NativeUtils.loadLibraryFromJar("/libraries/linux-x86-64/libdiscord-rpc.so");
				}

			} catch (IOException e) {
				System.out.println("Failed to load RPC libraries.");
				DiscordUtils.shouldOperate = false;
			}
		} else {
			DiscordUtils.shouldOperate = false;
		}

		// Instantiate GNT Classes
		new Giant();
		new GNT();
		new GNTM();

		String dailyName = TIMVDay.fromCalendar(Calendar.getInstance()) + ".txt";

		TIMV.setDailyKarmaFileName(dailyName);
		BP.setDailyPointsFileName(dailyName);
		CAI.setDailyPointsFileName(dailyName);
		BED.setDailyPointsFileName(dailyName);
		SKY.setDailyPointsFileName(dailyName);
		HIDE.setDailyPointsFileName(dailyName);
		MIMV.setDailyPointsFileName(dailyName);
		DR.setDailyPointsFileName(dailyName);
		Giant.setDailyPointsFileName(dailyName);
		SGN.setDailyPointsFileName(dailyName);

		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) == 0x1E && cal.get(Calendar.MONTH) == 0xA) {
			NotesManager.HR1cm5z = true; // Hbd
		}

	}

	private void checkForFileExist(File f, boolean directory) {
		if (!f.exists())
			try {
				if (directory) {
					f.mkdir();

				} else {
					f.createNewFile();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void checkOldCsvPath() {
		File oldPath = new File(mcFile + "/games.csv");
		File newPath = new File(mcFile + "/timv/games.csv");
		if (oldPath.exists() && newPath.exists()) {
		} else if (oldPath.exists() && !newPath.exists()) {
			The5zigAPI.getLogger().info("games.csv in 5zigtimv/ directory found! Migrating...");
			checkForFileExist(new File(mcFile + "/timv/"), true);
			try {
				newPath.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Files.move(FileSystems.getDefault().getPath(oldPath.getAbsolutePath()),
						FileSystems.getDefault().getPath(mcFile.getAbsolutePath() + "/timv/"),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			The5zigAPI.getLogger().info("Migration complete!");
		}

	}

	private boolean isStaffChat() {
		if (playerRank.endsWith("Hive Moderator"))
			return true;
		if (playerRank.equalsIgnoreCase("Hive Developer"))
			return true;
		return playerRank.equalsIgnoreCase("Hive Founder and Owner");

	}

	@EventHandler(priority = EventHandler.Priority.HIGH)
	public void onChatSend(ChatSendEvent evt) {

		if (evt.getMessage().startsWith("*") && isStaffChat()) {
			String noStar = evt.getMessage().replaceAll("\\*", "");
			if (noStar.length() == 0)
				return;
			The5zigAPI.getAPI().sendPlayerMessage("/s " + noStar);

			return;
		}

		if (evt.getMessage().startsWith("/") && !evt.getMessage().startsWith("/ ")) {

			if (CommandManager.dispatchCommand(evt.getMessage())) {
				evt.setCancelled(true);
				return;
			}

		}
		if (!MessageOverlayCommand.toggledName.isEmpty() && !evt.getMessage().startsWith("/")) {
			evt.setCancelled(true);
			The5zigAPI.getAPI().sendPlayerMessage("/msg " + MessageOverlayCommand.toggledName + " " + evt.getMessage());
		}
		if (evt.getMessage().toUpperCase().trim().equals("/P")) {
			MessageOverlayCommand.toggledName = "";
		}
		if (evt.getMessage().toUpperCase().startsWith("/RECORDS")
				|| evt.getMessage().toUpperCase().startsWith("/STATS")) {
			String[] args = evt.getMessage().split(" ");
			System.out.println(String.join(",", args));
			if (args.length == 1) {
				if (ActiveGame.is("timv")) {
					if (TIMV.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					TIMV.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("dr")) {
					if (DR.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					DR.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("bed")) {
					if (BED.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					BED.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("gnt") || ActiveGame.is("gntm")) {
					if (Giant.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					Giant.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("hide")) {
					if (HIDE.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					HIDE.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("cai")) {
					if (CAI.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					CAI.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("sky")) {
					if (SKY.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					SKY.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("grav")) {
					if (GRAV.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					GRAV.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("mimv")) {
					if (MIMV.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					MIMV.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				} else if (ActiveGame.is("bp")) {
					if (BP.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					BP.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				}
				else if (ActiveGame.is("sgn")) {
					if (SGN.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					SGN.lastRecords = The5zigAPI.getAPI().getGameProfile().getName();
				}

			} else {
				if (ActiveGame.is("timv")) {
					if (TIMV.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					TIMV.lastRecords = args[1].trim();
				} else if (ActiveGame.is("dr")) {
					if (DR.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					DR.lastRecords = args[1].trim();
				} else if (ActiveGame.is("bed")) {
					if (BED.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					BED.lastRecords = args[1].trim();
				} else if (ActiveGame.is("gnt") || ActiveGame.is("gntm")) {
					if (Giant.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					Giant.lastRecords = args[1].trim();
				} else if (ActiveGame.is("hide")) {
					if (HIDE.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					HIDE.lastRecords = args[1].trim();
				} else if (ActiveGame.is("cai")) {
					if (CAI.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					CAI.lastRecords = args[1].trim();
				} else if (ActiveGame.is("sky")) {
					if (SKY.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					SKY.lastRecords = args[1].trim();
				} else if (ActiveGame.is("grav")) {
					if (GRAV.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					GRAV.lastRecords = args[1].trim();
				} else if (ActiveGame.is("mimv")) {
					if (MIMV.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					MIMV.lastRecords = args[1].trim();
				} else if (ActiveGame.is("bp")) {
					if (BP.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					BP.lastRecords = args[1].trim();
				}
				else if (ActiveGame.is("sgn")) {
					if (SGN.isRecordsRunning) {
						The5zigAPI.getAPI().messagePlayer(Log.error + "Records is already running!");
						evt.setCancelled(true);
						return;
					}
					SGN.lastRecords = args[1].trim();
				}

			}
		}
		if (evt.getMessage().endsWith(" test") && (evt.getMessage().split(" ").length == 2) && ActiveGame.is("TIMV")
				&& Setting.TIMV_USE_TESTREQUESTS.getValue()) {

			int random = ThreadLocalRandom.current().ints(0, TIMV.testRequests.size()).distinct()
					.filter(i -> i != TIMV.lastTestMsg).findFirst().getAsInt();
			TIMV.lastTestMsg = random;
			String player = evt.getMessage().replaceAll(" test", "").trim();
			if (player.equalsIgnoreCase("i'll") || player.equalsIgnoreCase("ill") || player.equalsIgnoreCase("i") || player.equalsIgnoreCase("go"))
				return;
			evt.setCancelled(true);
			The5zigAPI.getAPI().sendPlayerMessage(TIMV.testRequests.get(random).replaceAll("\\{p\\}", player));
		}
	}

	@EventHandler(priority = Priority.HIGHEST)
	public void onDisconnect(ServerQuitEvent evt) {
		NotesManager.notes.clear();
		hasServedNews = false;
		System.out.println("Disconnecting...");
		if (DiscordUtils.shouldOperate && Setting.DISCORD_RPC.getValue())
			club.minnced.discord.rpc.DiscordRPC.INSTANCE.Discord_ClearPresence();
		if (DiscordUtils.callbacksThread != null)
			DiscordUtils.callbacksThread.interrupt();
		if (DiscordUtils.shouldOperate && Setting.DISCORD_RPC.getValue())
			club.minnced.discord.rpc.DiscordRPC.INSTANCE.Discord_Shutdown();

		if (ActiveGame.current() == null || ActiveGame.current().isEmpty())
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					StreakUtils.saveDailyStreak();

					String className = ActiveGame.current().toUpperCase();
					if (className.startsWith("GNT"))
						className = "Giant";
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
	public void onTitle(TitleEvent evt) {
		if (isColorDebug) {
			System.out.println("Title Color Debug [Global]: (" + evt.getTitle() + ") / (" + evt.getSubTitle() + ")");
		}
		if ((evt.getTitle().equals("§r§aplay§r§8.§r§bHiveMC§r§8.§r§acom§r")
				|| evt.getTitle().equals("§aplay§r§8.§r§bHiveMC§r§8.§r§acom§r")) && !hasServedNews) {
			hasServedNews = true;
			NewsServer.serveNews(Pools.newsPool, Pools.newMapsPool, Pools.staffPool);
		}
		// Map fallback
		if (ActiveGame.is("dr") && DR.activeMap == null) {
			String map = ChatColor.stripColor(evt.getTitle());
			if (map.equals("HiveMC.EU"))
				return;
			if (map.equals("play.HiveMC.com"))
				return;
			The5zigAPI.getLogger().info("FALLBACK MAP=" + map);
			DR.activeMap = DR.mapsPool.get(map.toLowerCase());

			new Thread(new Runnable() {
				@Override
				public void run() {
					ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
					if (DR.currentMapPB == null) {
						The5zigAPI.getLogger().info("Loading PB...");
						DR.currentMapPB = api.getPersonalBest(DR.activeMap);
					}
					if (DR.currentMapWR == null) {
						The5zigAPI.getLogger().info("Loading WR...");
						DR.currentMapWR = api.getWorldRecord(DR.activeMap);
					}
					if (DR.currentMapWRHolder == null) {
						The5zigAPI.getLogger().info("Loading WRHolder...");
						DR.currentMapWRHolder = api.getWorldRecordHolder(DR.activeMap);
					}
				}
			}).start();
		}
	}

	@EventHandler
	public void onChat(ChatEvent evt) {

		if (evt.getMessage() != null) {
			if (The5zigAPI.getAPI().getActiveServer() instanceof IHive) {
				if (ZTAMain.isColorDebug)
					The5zigAPI.getLogger().info("Global Color Debug: (" + evt.getMessage() + ")");
				if(ZTAMain.crInteractive && (evt.getMessage().startsWith("§8▏ §aLink§8 ▏ §e") || evt.getMessage().contains("§6Log link generated: §6"))) {
					crInteractive = false;
					new Thread(new Runnable() {
						@Override
						public void run() {
							String chatLog = evt.getMessage().contains("§6Log link generated: §6") ? evt.getMessage().split("§6Log link generated\\: §6")[1] : evt.getMessage().replace("§8▏ §aLink§8 ▏ §ehttp://chatlog.hivemc.com/?logId=", "");
							The5zigAPI.getAPI().messagePlayer(Log.info + "Running chatreport in §binteractive mode§3. Please wait while we fetch the report token. Do NOT click on the link below.");
								checkForNewLR = true;
								lrID = chatLog;
								The5zigAPI.getAPI().sendPlayerMessage("/login report");
							
						}
					}).start();
					
				}
				if(ZTAMain.checkForNewLR && evt.getMessage().equals("§8▍ §e§lHive§6§lMC§8 ▏§a §b§lPlease click §b§lHERE§a to login to our website.")) {
					checkForNewLR = false;
					String id = new String(lrID);
					String reason = new String(lrRS);
					lrID = "";
					lrRS = "";
					String url = ChatComponentUtils.getClickEventValue(evt.getChatComponent().toString());
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								The5zigAPI.getAPI().messagePlayer(Log.info + "Acquiring the token...");
								Connector.acquireReportToken(url);
								The5zigAPI.getAPI().messagePlayer(Log.info + "Submitting the report...");
								if(Connector.sendReport(id, reason)) {
									The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully submitted chatreport.");
								}
								else {
									The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred while attempting to submit the chatreport.");
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					
				}
			}
			if (ChatColor.stripColor(evt.getMessage().trim()).equals("▍ Friends ▏ ✚ Toccata")) {
				NotesManager.tramontoccataStelle();
			}
			if (evt.getMessage().startsWith("§3§lPRIVATE§3│")
					&& evt.getMessage().contains(The5zigAPI.getAPI().getGameProfile().getName() + "§8 » §b")
					&& Setting.PM_PING.getValue()) {

				try {
					if (!NotificationManager.isInGameFocus()) {
						if (Setting.PM_NOTIFICATION.getValue()) {
							String message = evt.getMessage().split("» §b")[1].trim();
							String recipient = ChatColor
									.stripColor(evt.getMessage().replace("§3§lPRIVATE§3│ ", "").split("⇉")[0]).trim();
							NotificationManager.sendNotification(recipient, message);
						}
						The5zigAPI.getAPI().playSound("note.pling", 1f);
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

	}

	@EventHandler
	public void onActionBar(ActionBarEvent bar) {
		// The5zigAPI.getLogger().info(bar.getMessage());
		if ((ActiveGame.is("gnt") || ActiveGame.is("gntm")) && bar.getMessage().contains("❂")) {
			// §6❂ §e12§7 ❘ §c§lDestructible Land§7 ❘ §f0§b Kills
			Giant.gold = Integer
					.parseInt(ChatColor.stripColor(bar.getMessage().split("❘")[0].replaceAll("❂", "")).trim());
		}
	}
}
