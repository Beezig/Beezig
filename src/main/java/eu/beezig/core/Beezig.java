/*
 * Copyright (C) 2017-2020 Beezig Team
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

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import eu.beezig.core.command.CommandManager;
import eu.beezig.core.config.BeezigConfiguration;
import eu.beezig.core.data.BeezigData;
import eu.beezig.core.modules.Modules;
import eu.beezig.core.net.BeezigNetManager;
import eu.beezig.core.notification.NotificationManager;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.DirectoryMigration;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.HiveWrapper;
import eu.the5zig.mod.ModAPI;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.plugin.Plugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Plugin(name = "Beezig", version = Constants.VERSION)
public class Beezig {
    public static Logger logger;
    public static Gson gson = new Gson();
    public static boolean DEBUG = false;
    private static Beezig instance;

    private ModAPI api;
    private ExecutorService asyncExecutor;
    private BeezigConfiguration config;
    private File beezigDir;
    private BeezigData data;
    private BeezigNetManager networkManager;
    private NotificationManager notificationManager;
    private boolean laby;

    public Beezig(boolean laby, File labyDir) {
        this.laby = laby;
        if(labyDir != null)
            this.beezigDir = new File(labyDir, "Beezig");
    }

    public Beezig() {
        this(false, null);
    }

    @EventHandler
    public void load(LoadEvent event) {
        setupLogger();
        logger.info("Load started");
        long timeStart = System.currentTimeMillis();

        // Init fields
        instance = this;
        api = The5zigAPI.getAPI();
        asyncExecutor = Executors.newFixedThreadPool(5);
        HiveWrapper.setAsyncExecutor(asyncExecutor);
        HiveWrapper.setUserAgent(Message.getUserAgent());

        // Init configuration
        try {
            if(beezigDir == null) {
                File minecraftDir = new File(Beezig.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                        .getParentFile().getParentFile().getParentFile();
                beezigDir = new File(minecraftDir, "Beezig");
                DirectoryMigration.migrateIfNeeded(new File(minecraftDir, "5zigtimv"), beezigDir);
                if (!beezigDir.exists() && !beezigDir.mkdir())
                    throw new RuntimeException("Could not create config dir.");
            }
            config = new BeezigConfiguration();
            config.load(new File(beezigDir, "config.json"));
        } catch(Exception e) {
            logger.error("Could not create config directory! Aborting load.");
            e.printStackTrace();
            return;
        }

        data = new BeezigData(beezigDir);
        try {
            data.tryUpdate();
        } catch (Exception e) {
            logger.error("Couldn't update data!");
            e.printStackTrace();
        }

        // Register Hive stuff
        api.registerServerInstance(this, ServerHive.class);
        Modules.register(this, api);
        CommandManager.init(this);
        notificationManager = new NotificationManager();

        networkManager = new BeezigNetManager();
        networkManager.connect();

        logger.info(String.format("Load complete in %d ms.", System.currentTimeMillis() - timeStart));
    }

    private void setupLogger() {
        logger = LogManager.getLogger("Beezig");
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        LoggerConfig cfg = ctx.getConfiguration().getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        if("true".equals(System.getProperty("beezig.debug"))) {
            DEBUG = true;
            cfg.setLevel(Level.DEBUG);
        }
        ctx.updateLoggers();
        logger.debug("Debug is active.");
    }

    public ExecutorService getAsyncExecutor() {
        return asyncExecutor;
    }

    public BeezigConfiguration getConfig() {
        return config;
    }

    public BeezigData getData() {
        return data;
    }

    public boolean isLaby() {
        return laby;
    }

    public File getBeezigDir() {
        return beezigDir;
    }

    public BeezigNetManager getNetworkManager() {
        return networkManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static Beezig get() {
        return instance;
    }

    public static BeezigConfiguration cfg() {
        return instance.config;
    }

    public static ModAPI api() {
        return instance.api;
    }

    public static GameProfile user() {
        return instance.api.getGameProfile();
    }

    public static BeezigNetManager net() {
        return instance.networkManager;
    }
}
