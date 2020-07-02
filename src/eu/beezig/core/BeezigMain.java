/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core;


import eu.beezig.core.advancedrecords.anywhere.AdvancedRecordsAnywhere;
import eu.beezig.core.api.BeezigAPI;
import eu.beezig.core.autovote.AutovoteUtils;
import eu.beezig.core.briefing.NewsServer;
import eu.beezig.core.briefing.Pools;
import eu.beezig.core.briefing.fetcher.NewsFetcher;
import eu.beezig.core.command.*;
import eu.beezig.core.games.*;
import eu.beezig.core.games.logging.hide.HideMapRecords;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.StuffFetcher;
import eu.beezig.core.hiveapi.stuff.grav.GRAVListenerv2;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.beezig.core.modules.arcade.GameItem;
import eu.beezig.core.modules.bed.MonthlyItem;
import eu.beezig.core.modules.bed.*;
import eu.beezig.core.modules.bp.SongItem;
import eu.beezig.core.modules.dr.DailyItem;
import eu.beezig.core.modules.dr.DeathsItem;
import eu.beezig.core.modules.dr.PointsItem;
import eu.beezig.core.modules.dr.*;
import eu.beezig.core.modules.global.MedalsItem;
import eu.beezig.core.modules.global.TokensItem;
import eu.beezig.core.modules.grav.StagesItem;
import eu.beezig.core.modules.timv.*;
import eu.beezig.core.modules.utils.RenderUtils;
import eu.beezig.core.notes.NotesManager;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.settings.SettingsFetcher;
import eu.beezig.core.updater.Updater;
import eu.beezig.core.utils.*;
import eu.beezig.core.utils.acr.Connector;
import eu.beezig.core.utils.autogg.AutoGGListener;
import eu.beezig.core.utils.autogg.Triggers;
import eu.beezig.core.utils.autogg.TriggersFetcher;
import eu.beezig.core.utils.mps.MultiPsStats;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.beezig.core.utils.soundcloud.TrackPlayer;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.beezig.core.utils.tutorial.TutorialManager;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.*;
import eu.the5zig.mod.event.EventHandler.Priority;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.util.internal.ThreadLocalRandom;
import pw.roccodev.beezig.hiveapi.wrapper.GlobalConfiguration;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;
import pw.roccodev.beezig.hiveapi.wrapper.speedrun.WorldRecord;

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
    public static final String BEEZIG_VERSION = "6.2.1";
    public static String VERSION_HASH = "";
    public static String OS;
    public static boolean newUpdate;
    public static boolean disabled;

    public static boolean hasServedNews;
    public static boolean checkForNewLR; // Login Report
    public static boolean crInteractive;
    public static String lrID;
    public static String lrRS;
    public static String lrPL;

    public static File mcFile;
    public static boolean isColorDebug;
    public static boolean hasExpansion;
    public static boolean laby;
    public static NetworkRank playerRank;
    public static String dailyFileName;
    private File labyConfig;

    public static BeezigMain instance;

    public BeezigMain(boolean laby, File config) {
        BeezigMain.laby = laby;
        this.labyConfig = config;
    }

    public BeezigMain() {
    }

    public static int getCustomVersioning() {
        String toParse = BEEZIG_VERSION.replaceAll("\\.", "");
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
        instance = this;
        Updater.setUrl();
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
                disabled = true;
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

        } else if (!laby)
            VERSION_HASH = "Dev";

        try {
            RenderUtils.init();
        } catch (Exception ignored) {
        }

        The5zigAPI.getLogger().info("Loading Beezig");

        //Display.setTitle("Minecraft " + The5zigAPI.getAPI().getMinecraftVersion() + " | Beezig " + BEEZIG_VERSION);

        The5zigAPI.getLogger().info("Version is " + BEEZIG_VERSION + ". Hash is " + VERSION_HASH);
        The5zigAPI.getLogger().info("Using Java version: " + Runtime.class.getPackage().getImplementationVersion());

        The5zigAPI.getAPI().registerServerInstance(this, IHive.class);

        if (disabled) return;

        The5zigAPI.getAPI().registerModuleItem(this, "karma", KarmaItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "karmacounter",
                KarmaCounterItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "timvmap", eu.beezig.core.modules.timv.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bodies", BodiesItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "dbodies", DBodiesItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "timvdailykarma",
                DailyKarmaItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "timvmonthly", eu.beezig.core.modules.timv.MonthlyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "drpoints", PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drmap", eu.beezig.core.modules.dr.MapItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drrole", RoleItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drdeaths", DeathsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drpb", PBItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drwr", WRItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drdaily", DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "drmonthly", eu.beezig.core.modules.dr.MonthlyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "bedpoints", eu.beezig.core.modules.bed.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedresources", ResourcesItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedmap", eu.beezig.core.modules.bed.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedkills", KillsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedgamecounter",
                PointsCounterItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "beddestroyed",
                BedsDestroyedItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "beddeaths", eu.beezig.core.modules.bed.DeathsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedkdrchange", KDRChangeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedteamsleft", TeamsLeftItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedsummoners", SummonersItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "beddaily", eu.beezig.core.modules.bed.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedstreak", WinstreakItem.class, "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bedmonthly", MonthlyItem.class, "serverhivemc");


        The5zigAPI.getAPI().registerModuleItem(this, "globalmedals", MedalsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "globaltokens", TokensItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "hidepoints", eu.beezig.core.modules.hide.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidemap", eu.beezig.core.modules.hide.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidedaily", eu.beezig.core.modules.hide.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidestreak", eu.beezig.core.modules.hide.WinstreakItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "hidemonthly", eu.beezig.core.modules.hide.MonthlyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "skypoints", eu.beezig.core.modules.sky.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skyteam", eu.beezig.core.modules.sky.TeamItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skykills", eu.beezig.core.modules.sky.KillsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skymode", eu.beezig.core.modules.sky.ModeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skykdr", eu.beezig.core.modules.sky.KDRChangeItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skygame", eu.beezig.core.modules.sky.GamePointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skydaily", eu.beezig.core.modules.sky.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skymap", eu.beezig.core.modules.sky.MapItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skystreak", eu.beezig.core.modules.sky.WinstreakItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "skymonthly", eu.beezig.core.modules.sky.MonthlyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "gravpoints", eu.beezig.core.modules.grav.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "gravstages", StagesItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "bppoints", eu.beezig.core.modules.bp.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpcounter", eu.beezig.core.modules.bp.PointsCounterItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpdaily", eu.beezig.core.modules.bp.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpsong", SongItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "bpmonthly", eu.beezig.core.modules.bp.MonthlyItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "sgnpoints", eu.beezig.core.modules.sgn.PointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "sgngame", eu.beezig.core.modules.sgn.GamePointsItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "sgndaily", eu.beezig.core.modules.sgn.DailyItem.class,
                "serverhivemc");
        The5zigAPI.getAPI().registerModuleItem(this, "sgnmap", eu.beezig.core.modules.sgn.MapItem.class,
                "serverhivemc");

        The5zigAPI.getAPI().registerModuleItem(this, "arcadepoints", eu.beezig.core.modules.arcade.PointsItem.class,
                "hivearcade");
        The5zigAPI.getAPI().registerModuleItem(this, "arcadegame", GameItem.class,
                "hivearcade");
        The5zigAPI.getAPI().registerModuleItem(this, "arcademap", eu.beezig.core.modules.arcade.MapItem.class,
                "hivearcade");


        The5zigAPI.getAPI().getPluginManager().registerListener(this, new GRAVListenerv2());
        The5zigAPI.getAPI().getPluginManager().registerListener(this, new AutoGGListener());

        AdvancedRecordsAnywhere.register();
        MultiPsStats.init();
        GlobalConfiguration.setUserAgent(Log.getUserAgent());
        GlobalConfiguration.setMaxthatApiKey("ighGH789fdf5kfHUo");

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
        CommandManager.registerCommand(new ClosestToWRCommand());
        CommandManager.registerCommand(new BeezigCommand());
        CommandManager.registerCommand(new ReportCommand());
        CommandManager.registerCommand(new LeaderboardCommand());
        CommandManager.registerCommand(new RigCommand());
        CommandManager.registerCommand(new UUIDCommand());
        CommandManager.registerCommand(new DeathrunRecordsCommand());
        CommandManager.registerCommand(new VolumeCommand());
        CommandManager.registerCommand(new WinstreakCommand());
        CommandManager.registerCommand(new DailyCommand());
        CommandManager.registerCommand(new AutoGGCommand());
        CommandManager.registerCommand(new UptimeCommand());
        CommandManager.registerCommand(new ChatReportCommand());
        CommandManager.registerCommand(new StatsOverlayCommand());
        CommandManager.registerCommand(new SkipTutorialCommand());
        CommandManager.registerCommand(new BestGameCommand());
        CommandManager.registerCommand(new BeezigPartyCommand());
        CommandManager.registerCommand(new ClaimReportCommand());

        new Thread(() -> {
            try {
                PacketHandler.registerHandlers();
                eu.beezig.core.utils.ws.Connector.connect();
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
            if (laby) {
                mcFile = new File(labyConfig + "/Beezig");
            } else
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

        checkForFileExist(new File(mcFile + "/tutorial.json"), false);
        TutorialManager.init();
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
        HideMapRecords.init();

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

        HivePlayer api = new HivePlayer(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));
        playerRank = NetworkRank.fromDisplay(api.getRank().getHumanName());

        try {
            APIValues.medals = api.getMedals();
            APIValues.tokens = api.getTokens();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        The5zigAPI.getLogger().info("Loaded BeezigConfig.");

        String dailyName = TIMVDay.fromCalendar(Calendar.getInstance()) + '-' + The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "") + ".txt";
        dailyFileName = dailyName;
        TIMV.setDailyKarmaFileName(dailyName);
        BP.setDailyPointsFileName(dailyName);
        BED.setDailyPointsFileName(dailyName);
        SKY.setDailyPointsFileName(dailyName);
        HIDE.setDailyPointsFileName(dailyName);
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

        if (evt.getMessage().startsWith("~") && isStaffChat() && Setting.STAFF_CHAT.getValue()) {
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
        if (ActiveGame.is("TIMV")
                && (evt.getMessage().endsWith(" test") || (evt.getMessage().endsWith(" tset")))
                && (evt.getMessage().split(" ").length == 2)
                && Setting.TIMV_USE_TESTREQUESTS.getValue()) {

            int random = ThreadLocalRandom.current().ints(0, TIMV.testRequests.size()).distinct()
                    .filter(i -> i != TIMV.lastTestMsg).findFirst().getAsInt();
            TIMV.lastTestMsg = random;
            String player = evt.getMessage().replaceAll(" test", "").replaceAll(" tset", "").trim();
            if (player.equalsIgnoreCase("i'll")
                    || player.equalsIgnoreCase("ill")
                    || player.equalsIgnoreCase("i")
                    || player.equalsIgnoreCase("go")
                    || player.equalsIgnoreCase("pls")
                    || player.equalsIgnoreCase("please"))
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
                if (className.startsWith("ARCADE_"))
                    className = "Arcade";
                Class gameModeClass = Class.forName("eu.beezig.core.games." + className);
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
                The5zigAPI.getAPI().messagePlayer(Log.info + "If so, download it from §bhttp://l.rocco.dev/beezigforge");
                The5zigAPI.getAPI().messagePlayer(Log.info + "To suppress this notification, run §b/settings ignore_warnings true");
            }
            SendTutorial.send("hub");
            if (BeezigMain.hasExpansion) SendTutorial.send("hub_forge");
        }
        // Map fallback
        if (ActiveGame.is("dr")) {
            DR dr = (DR) The5zigAPI.getAPI().getActiveServer().getGameListener().getCurrentGameMode();
            if (dr.activeMap != null) return;
            String map = ChatColor.stripColor(evt.getTitle());
            if (map.equals("HiveMC.EU"))
                return;
            if (map.equals("play.HiveMC.com"))
                return;
            The5zigAPI.getLogger().info("FALLBACK MAP=" + map);
            dr.activeMap = DR.mapsPool.get(map.toLowerCase());

            new Thread(() -> {
                DrStats api = new DrStats(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));
                if (dr.currentMapPB == null) {
                    The5zigAPI.getLogger().info("Loading PB...");
                    dr.currentMapPB = PBCommand.parseTime(api.getMapRecords().get(dr.activeMap.getHiveAPIName()));
                }
                WorldRecord wr = null;
                if (dr.currentMapWR == null) {
                    wr = DrStats.getWorldRecord(dr.activeMap.getSpeedrunID());
                    The5zigAPI.getLogger().info("Loading WR...");
                    dr.currentMapWR = WRCommand.getWorldRecord(wr.getTime());
                }
                if (dr.currentMapWRHolder == null) {
                    if (wr == null) wr = DrStats.getWorldRecord(dr.activeMap.getSpeedrunID());
                    The5zigAPI.getLogger().info("Loading WRHolder...");
                    dr.currentMapWRHolder = wr.getHolderName();
                }
            }).start();
        }
    }

    @EventHandler
    public void onTick(TickEvent evt) {
        if (!The5zigAPI.getAPI().isInWorld()) return;
        for (String s : Log.toSendQueue) {
            The5zigAPI.getAPI().messagePlayer(s);
        }
        Log.toSendQueue.clear();

        if (The5zigAPI.getAPI().getSideScoreboard() != null && The5zigAPI.getAPI().getSideScoreboard().getTitle().equals("   §eYour LAB Stats   ") && !ActiveGame.is("lab")) {
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
                if (Setting.PARTY_MEMBERS.getValue()) {
                    String party = ChatComponentUtils.getPartyMembers(evt.getChatComponent().toString());
                    if (party != null) {
                        evt.setCancelled(true);
                        The5zigAPI.getAPI().messagePlayer(evt.getMessage());
                        The5zigAPI.getAPI().messagePlayer("§8▍ §b§lParty§8 ▏ §eMembers: §a" + party);
                        The5zigAPI.getAPI().messagePlayer("");
                    }
                }
                if (hasExpansion && Setting.PARTY_FRIEND.getValue() && evt.getMessage().startsWith("§8▍ §eFriends§8 ▏§a ✚ ")) {
                    String player = evt.getMessage().replace("§8▍ §eFriends§8 ▏§a ✚ ", "");
                    evt.setCancelled(true);
                    BeezigAPI.get().getListener().displayFriendJoin(player);
                }
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

                } else if (BeezigMain.crInteractive && (evt.getMessage().startsWith("§8▏ §aLink§8 ▏ §e") || evt.getMessage().contains("§6Log link generated: §6"))) {
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
                    String id = lrID;
                    String reason = lrRS;
                    String pl = lrPL == null ? "" : lrPL;
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
            if(DiscordUtils.shouldOperate &&
                    evt.getMessage().startsWith("§f                       §a§lWelcome to your Party!")) {
                The5zigAPI.getAPI().messagePlayer("                            §3§lDiscord: §b§l/bparty");
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
}
