package tk.roccodev.beezig;


import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.*;
import eu.the5zig.mod.event.EventHandler.Priority;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.util.internal.ThreadLocalRandom;
import org.lwjgl.opengl.Display;
import tk.roccodev.beezig.autovote.AutovoteUtils;
import tk.roccodev.beezig.briefing.NewsServer;
import tk.roccodev.beezig.briefing.Pools;
import tk.roccodev.beezig.briefing.fetcher.NewsFetcher;
import tk.roccodev.beezig.command.*;
import tk.roccodev.beezig.games.*;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.StuffFetcher;
import tk.roccodev.beezig.hiveapi.stuff.grav.GRAVListenerv2;
import tk.roccodev.beezig.hiveapi.wrapper.NetworkRank;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiDR;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiHiveGlobal;
import tk.roccodev.beezig.modules.utils.RenderUtils;
import tk.roccodev.beezig.notes.NotesManager;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.settings.SettingsFetcher;
import tk.roccodev.beezig.updater.Updater;
import tk.roccodev.beezig.utils.*;
import tk.roccodev.beezig.utils.acr.Connector;
import tk.roccodev.beezig.utils.autogg.AutoGGListener;
import tk.roccodev.beezig.utils.autogg.Triggers;
import tk.roccodev.beezig.utils.autogg.TriggersFetcher;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;
import tk.roccodev.beezig.utils.soundcloud.TrackPlayer;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Plugin(name = "Beezig", version = BeezigMain.BEEZIG_VERSION)
public class BeezigMain {
    public static final String BEEZIG_VERSION = "4.9.0";
    public static String VERSION_HASH = "";
    public static String OS;
    public static boolean newUpdate;

    public static boolean hasServedNews;
    public static boolean checkForNewLR; // Login Report
    public static boolean crInteractive;
    public static String lrID;
    public static String lrRS;
    public static String lrPL;
    public static List<Class<?>> services = new ArrayList<>();

    public static File mcFile;
    public static boolean isColorDebug = false;
    public static boolean hasExpansion = false;
    public static NetworkRank playerRank = null;

    public static int getCustomVersioning() {
        String v = BeezigMain.class.getAnnotation(Plugin.class).version();
        String toParse = v.replaceAll("\\.", "");
        return Integer.parseInt(toParse);
    }

    public static boolean isStaffChat() {
        return playerRank.getLevel() >= 70;
    }

    public static void refetchMaps() {
        DR.mapsPool = StuffFetcher.getDeathRunMaps();
        TIMV.mapsPool = StuffFetcher.getTroubleInMinevilleMaps();
        GRAV.mapsPool = StuffFetcher.getGravityMaps();
        Triggers.triggers.clear();
        try {
            TriggersFetcher.fetch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventHandler.Priority.LOW)
    public void onLoad(LoadEvent event) {

        IOverlay news = The5zigAPI.getAPI().createOverlay();
        try {
            if (Updater.isVersionBlacklisted(getCustomVersioning())
                    && !BeezigMain.class.getAnnotation(Plugin.class).version().contains("experimental")) {
                new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    The5zigAPI.getLogger().error("Beezig: This version is disabled!");
                    news.displayMessage("Beezig: Version is disabled!", "Please update to the latest version.");
                }).start();
                return; // < one does not simply update beezig
            }
        } catch (IOException e) {
            The5zigAPI.getLogger().info("Failed checking for blacklist");
            e.printStackTrace();
        }
        try {
            if (Updater.checkForUpdates()
                    && !BeezigMain.class.getAnnotation(Plugin.class).version().contains("experimental")) {
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

            } catch (IOException ignored) {

            }
            String[] data = result.split(" ");
            if (data.length != 0 && data[0].equals("experimental")) {
                VERSION_HASH = data[1].substring(0, 7);
            }

        }

        try {
            RenderUtils.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        The5zigAPI.getLogger().info("Loading Beezig");

        Display.setTitle("Minecraft " + The5zigAPI.getAPI().getMinecraftVersion() + " | Beezig " + BEEZIG_VERSION);

        The5zigAPI.getLogger().info("Version is " + BEEZIG_VERSION + ". Hash is " + VERSION_HASH);
        The5zigAPI.getLogger().info("Using Java version: " + Runtime.class.getPackage().getImplementationVersion());

        The5zigAPI.getAPI().registerModuleItem(this, "karma", tk.roccodev.beezig.modules.timv.KarmaItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "karmacounter",
                tk.roccodev.beezig.modules.timv.KarmaCounterItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "timvmap", tk.roccodev.beezig.modules.timv.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bodies", tk.roccodev.beezig.modules.timv.BodiesItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "dbodies", tk.roccodev.beezig.modules.timv.DBodiesItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "timvdailykarma",
                tk.roccodev.beezig.modules.timv.DailyKarmaItem.class, "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "drmap", tk.roccodev.beezig.modules.dr.MapItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drrole", tk.roccodev.beezig.modules.dr.RoleItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drpoints", tk.roccodev.beezig.modules.dr.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drdeaths", tk.roccodev.beezig.modules.dr.DeathsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drpb", tk.roccodev.beezig.modules.dr.PBItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drwr", tk.roccodev.beezig.modules.dr.WRItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drdaily", tk.roccodev.beezig.modules.dr.DailyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "bedpoints", tk.roccodev.beezig.modules.bed.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedresources", tk.roccodev.beezig.modules.bed.ResourcesItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedmap", tk.roccodev.beezig.modules.bed.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedkills", tk.roccodev.beezig.modules.bed.KillsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedgamecounter",
                tk.roccodev.beezig.modules.bed.PointsCounterItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "beddestroyed",
                tk.roccodev.beezig.modules.bed.BedsDestroyedItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "beddeaths", tk.roccodev.beezig.modules.bed.DeathsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedkdrchange", tk.roccodev.beezig.modules.bed.KDRChangeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedteamsleft", tk.roccodev.beezig.modules.bed.TeamsLeftItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedsummoners", tk.roccodev.beezig.modules.bed.SummonersItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "beddaily", tk.roccodev.beezig.modules.bed.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedstreak", tk.roccodev.beezig.modules.bed.WinstreakItem.class, "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "globalmedals", tk.roccodev.beezig.modules.global.MedalsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "globaltokens", tk.roccodev.beezig.modules.global.TokensItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "gntmode", tk.roccodev.beezig.modules.gnt.ModeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntteam", tk.roccodev.beezig.modules.gnt.TeamItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntmap", tk.roccodev.beezig.modules.gnt.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntkills", tk.roccodev.beezig.modules.gnt.KillsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntdeaths", tk.roccodev.beezig.modules.gnt.DeathsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntkdrchange", tk.roccodev.beezig.modules.gnt.KDRChangeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntpoints", tk.roccodev.beezig.modules.gnt.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntgiantkills", tk.roccodev.beezig.modules.gnt.GiantKillsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntgold", tk.roccodev.beezig.modules.gnt.GoldItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntdaily", tk.roccodev.beezig.modules.gnt.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gntstreak", tk.roccodev.beezig.modules.gnt.WinstreakItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "hidemap", tk.roccodev.beezig.modules.hide.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidepoints", tk.roccodev.beezig.modules.hide.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidedaily", tk.roccodev.beezig.modules.hide.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidestreak", tk.roccodev.beezig.modules.hide.WinstreakItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "caimap", tk.roccodev.beezig.modules.cai.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "caipoints", tk.roccodev.beezig.modules.cai.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "caigame", tk.roccodev.beezig.modules.cai.GamePointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "caiteam", tk.roccodev.beezig.modules.cai.TeamItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "caidaily", tk.roccodev.beezig.modules.cai.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "caistreak", tk.roccodev.beezig.modules.cai.WinstreakItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "caicooldowns", tk.roccodev.beezig.modules.cai.CooldownsItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "skypoints", tk.roccodev.beezig.modules.sky.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skyteam", tk.roccodev.beezig.modules.sky.TeamItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skykills", tk.roccodev.beezig.modules.sky.KillsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skymode", tk.roccodev.beezig.modules.sky.ModeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skykdr", tk.roccodev.beezig.modules.sky.KDRChangeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skygame", tk.roccodev.beezig.modules.sky.GamePointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skydaily", tk.roccodev.beezig.modules.sky.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skymap", tk.roccodev.beezig.modules.sky.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skystreak", tk.roccodev.beezig.modules.sky.WinstreakItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "mimvkarma", tk.roccodev.beezig.modules.mimv.KarmaItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "mimvrole", tk.roccodev.beezig.modules.mimv.RoleItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "mimvmap", tk.roccodev.beezig.modules.mimv.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "mimvcounter", tk.roccodev.beezig.modules.mimv.KarmaCounterItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "mimvdaily", tk.roccodev.beezig.modules.mimv.DailyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "gravpoints", tk.roccodev.beezig.modules.grav.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gravstages", tk.roccodev.beezig.modules.grav.StagesItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "bppoints", tk.roccodev.beezig.modules.bp.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpcounter", tk.roccodev.beezig.modules.bp.PointsCounterItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpdaily", tk.roccodev.beezig.modules.bp.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpsong", tk.roccodev.beezig.modules.bp.SongItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "sgnpoints", tk.roccodev.beezig.modules.sgn.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "sgngame", tk.roccodev.beezig.modules.sgn.GamePointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "sgndaily", tk.roccodev.beezig.modules.sgn.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "sgnmap", tk.roccodev.beezig.modules.sgn.MapItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "labpoints", tk.roccodev.beezig.modules.lab.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "labdaily", tk.roccodev.beezig.modules.lab.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "labexperiments", tk.roccodev.beezig.modules.lab.ExperimentsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "lablb", tk.roccodev.beezig.modules.lab.LeaderboardItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "arcadegame", tk.roccodev.beezig.modules.arcade.GameItem.class,
                "hivearcade");
        The5zigAPI.getAPI().registerModuleItem(this, "arcadepoints", tk.roccodev.beezig.modules.arcade.PointsItem.class,
                "hivearcade");
        The5zigAPI.getAPI().registerModuleItem(this, "arcademap", tk.roccodev.beezig.modules.arcade.MapItem.class,
                "hivearcade");


        The5zigAPI.getAPI().registerServerInstance(this, IHive.class);

        The5zigAPI.getAPI().getPluginManager().registerListener(this, new GRAVListenerv2());
        The5zigAPI.getAPI().getPluginManager().registerListener(this, new AutoGGListener());

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
        CommandManager.registerCommand(new BeezigPartyCommand());
        CommandManager.registerCommand(new DeathrunRecordsCommand());
        CommandManager.registerCommand(new VolumeCommand());
        CommandManager.registerCommand(new WinstreakCommand());
        CommandManager.registerCommand(new DailyCommand());
        CommandManager.registerCommand(new AutoGGCommand());
        CommandManager.registerCommand(new UptimeCommand());
        CommandManager.registerCommand(new ChatReportCommand());

        new Thread(() -> {
            try {
                tk.roccodev.beezig.utils.ws.Connector.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        if (The5zigAPI.getAPI().getGameProfile().getId().toString().equals("8b687575-2755-4506-9b37-538b4865f92d")
                || The5zigAPI.getAPI().getGameProfile().getId().toString()
                .equals("bba224a2-0bff-4913-b042-27ca3b60973f")) {
            CommandManager.registerCommand(new RealRankCommand());
            CommandManager.registerCommand(new SeenCommand());
        }

        new Thread(BeezigMain::refetchMaps, "Maps Fetcher").start();

        The5zigAPI.getLogger().info("Loaded BeezigCore");

        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("mac")) {
            BeezigMain.OS = "mac";
        } else if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
            BeezigMain.OS = "unix";
        } else if (OS.contains("win")) {
            BeezigMain.OS = "win";
        }
        // Code source is the path to the jar, parent1 is "plugins", parent2 is "the5zigmod", parent3 is the mc dir.
        try {
            mcFile = new File(new File(BeezigMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile().getParentFile().getPath() + "/5zigtimv");
        } catch (URISyntaxException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }

        if (!mcFile.exists())
            mcFile.mkdir();
        The5zigAPI.getLogger().info("MC Folder is at: " + mcFile.getAbsolutePath());

        File autoggConfig = new File(mcFile + "/autogg.json");
        if (!autoggConfig.exists()) {
            try {
                autoggConfig.createNewFile();
                TriggersFetcher.loadDefaults();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        checkForFileExist(new File(mcFile + "/winstreaks.json"), false);
        checkForFileExist(new File(mcFile + "/timv/"), true);
        checkForFileExist(new File(mcFile + "/timv/dailykarma/"), true);
        checkForFileExist(new File(mcFile + "/timv/testMessages.txt"), false);
        checkForFileExist(new File(mcFile + "/bedwars/"), true);
        checkForFileExist(new File(mcFile + "/bedwars/streak.txt"), false);

        checkForFileExist(new File(mcFile + "/bp/"), true);
        checkForFileExist(new File(mcFile + "/bp/dailyPoints/"), true);

        File jukeboxFile = new File(mcFile + "/bp/jukebox");
        checkForFileExist(jukeboxFile, false);

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

        checkForFileExist(new File(mcFile + "/lab/"), true);
        checkForFileExist(new File(mcFile + "/lab/dailyPoints/"), true);

        StreakUtils.init(mcFile);
        TrackPlayer.loadConfigFile(jukeboxFile);


        new Thread(() -> {
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
                    ArrayList<String> bloccs = Files.readAllLines(Paths.get(f.getPath())).stream().collect(Collectors.toCollection(ArrayList::new));
                    lastLogin = Long.parseLong(bloccs.get(0));

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            List<String> lines = new ArrayList<>(Collections.singletonList(System.currentTimeMillis() + ""));
            try {
                Files.write(Paths.get(f.getPath()), lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                TriggersFetcher.fetch();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Pools.newsPool = NewsFetcher.getApplicableNews(lastLogin);
            Pools.newMapsPool = NewsFetcher.getApplicableNewMaps(lastLogin);
            Pools.staffPool = NewsFetcher.getApplicableStaffUpdates(lastLogin);


        }, "News Fetcher").start();

        try {
            TIMVTest.init();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        checkOldCsvPath();
        The5zigAPI.getLogger().info("Loading BeezigConfig...");
        File settingsFile = new File(BeezigMain.mcFile.getAbsolutePath() + "/settings.properties");
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
        playerRank = NetworkRank.fromDisplay(api.getNetworkTitle());

        try {
            HiveAPI.updateMedals();
            HiveAPI.updateTokens();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        The5zigAPI.getLogger().info("Loaded BeezigConfig.");


        // Instantiate GNT Classes
        new Giant();
        new GNT();
        new GNTM();

        String dailyName = TIMVDay.fromCalendar(Calendar.getInstance()) + '-' + The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "") + ".txt";

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
        LAB.setDailyPointsFileName(dailyName);

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
       if (oldPath.exists() && !newPath.exists()) {
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

    @EventHandler(priority = EventHandler.Priority.HIGH)
    public void onChatSend(ChatSendEvent evt) {

        if (evt.getMessage().startsWith("~") && isStaffChat()) {
            String noStar = evt.getMessage().replace("~", "");
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
            if (AdvancedRecords.isRunning) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Advanced Records is already running.");
                evt.setCancelled(true);
                return;
            }
            String[] args = evt.getMessage().split(" ");
            AdvancedRecords.player = args.length == 1 ? The5zigAPI.getAPI().getGameProfile().getName() : args[1].trim();

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
            DiscordUtils.clearPresence();
        if (DiscordUtils.shouldOperate && Setting.DISCORD_RPC.getValue())
            DiscordUtils.closeClient();

        if (ActiveGame.current() == null || ActiveGame.current().isEmpty())
            return;
        new Thread(() -> {
            try {


                String className = ActiveGame.current().toUpperCase();
                if (className.startsWith("GNT"))
                    className = "Giant";
                if(className.startsWith("ARCADE_"))
                    className = "Arcade";
                Class gameModeClass = Class.forName("tk.roccodev.beezig.games." + className);
                Method resetMethod = gameModeClass.getMethod("reset", gameModeClass);
                resetMethod.invoke(null, gameModeClass.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
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
            if (!BeezigMain.hasExpansion && !Setting.IGNORE_WARNINGS.getValue() && The5zigAPI.getAPI().getMinecraftVersion().equals("1.8.9") && The5zigAPI.getAPI().isForgeEnvironment()) {
                The5zigAPI.getAPI().messagePlayer(Log.info + "We've noticed you're running Forge 1.8.9, but you don't have our Forge Expansion mod. Do you want to install it?");
                The5zigAPI.getAPI().messagePlayer(Log.info + "If so, download it from §bhttp://l.roccodev.pw/beezigforge");
                The5zigAPI.getAPI().messagePlayer(Log.info + "To suppress this notification, run §b/settings ignore_warnings true");
            }
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

            new Thread(() -> {
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
            }).start();
        }
    }

    @EventHandler
    public void onTick(TickEvent evt) {

        if (The5zigAPI.getAPI().isInWorld() && The5zigAPI.getAPI().getSideScoreboard() != null && The5zigAPI.getAPI().getSideScoreboard().getTitle().equals("   §eYour LAB Stats   ") && !ActiveGame.is("lab")) {
            ActiveGame.set("LAB");
            System.out.println("Connected to LAB -Hive");
            DiscordUtils.updatePresence("Experimenting in TheLab", "In Lobby", "game_lab");
            IHive.gameListener.switchLobby("LAB");

        }

    }

    @EventHandler
    public void onChat(ChatEvent evt) {

        if (evt.getMessage() != null) {
            if (The5zigAPI.getAPI().getActiveServer() instanceof IHive) {
                if (BeezigMain.isColorDebug)
                    The5zigAPI.getLogger().info("Global Color Debug: (" + evt.getMessage() + ")");
                if (evt.getMessage().equals("§8▏ §aChatReport§8 ▏ §cSorry, there are no logs for this user.")) {
                    crInteractive = false;
                    lrRS = "";
                    checkForNewLR = false;
                }
                if (BeezigMain.crInteractive && evt.getMessage().contains("http://hivemc.com/chat/log")) {
                    crInteractive = false;
                    new Thread(() -> {
                        String chatLog = "http://" + evt.getMessage().split("http\\://")[1];
                        The5zigAPI.getAPI().messagePlayer(Log.info + "Running chatreport in §binteractive mode§3. Please wait while we fetch the report token. Do NOT click on the link below.");
                        checkForNewLR = true;
                        lrID = chatLog;

                        The5zigAPI.getAPI().sendPlayerMessage("/login report");

                    }).start();

                } else if (BeezigMain.crInteractive && (evt.getMessage().startsWith("§8▏ §aLink§8 ▏ §e") || evt.getMessage().startsWith("§6Log link generated: §6"))) {
                    crInteractive = false;
                    new Thread(() -> {
                        String chatLog = evt.getMessage().contains("§6Log link generated: §6") ? evt.getMessage().split("§6Log link generated\\: §6")[1] : evt.getMessage().replace("§8▏ §aLink§8 ▏ §ehttp://chatlog.hivemc.com/?logId=", "");
                        The5zigAPI.getAPI().messagePlayer(Log.info + "Running chatreport in §binteractive mode§3. Please wait while we fetch the report token. Do NOT click on the link below.");
                        checkForNewLR = true;
                        lrID = chatLog;

                        The5zigAPI.getAPI().sendPlayerMessage("/login report");

                    }).start();

                }
                if (BeezigMain.checkForNewLR && evt.getMessage().equals("§8▍ §e§lHive§6§lMC§8 ▏§a §b§lPlease click §b§lHERE§a to login to our website.")) {
                    checkForNewLR = false;
                    String id = new String(lrID);
                    String reason = new String(lrRS);
                    String pl = new String(lrPL == null ? "" : lrPL);
                    lrID = "";
                    lrRS = "";
                    lrPL = null;
                    String url = ChatComponentUtils.getClickEventValue(evt.getChatComponent().toString());
                    new Thread(() -> {
                        try {
                            The5zigAPI.getAPI().messagePlayer(Log.info + "Acquiring the token...");
                            Connector.acquireReportToken(url);
                            The5zigAPI.getAPI().messagePlayer(Log.info + "Submitting the report...");
                            if (Connector.sendReport(id, reason, pl)) {
                                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully submitted chatreport.");
                            } else {
                                The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred while attempting to submit the chatreport.");
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }).start();

                }
            }
            if (ChatColor.stripColor(evt.getMessage().trim()).equals("▍ Friends ▏ ✚ Toccata") && Setting.TOCCATA.getValue()) {
                NotesManager.tramontoccataStelle();
            }
            if (evt.getMessage().startsWith("§3§lPRIVATE§3│")
                    && evt.getMessage().contains(The5zigAPI.getAPI().getGameProfile().getName() + "§8 » §b")
                    && Setting.PM_PING.getValue()) {

                try {
                    if (NotificationManager.isInGameFocus()) {
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
