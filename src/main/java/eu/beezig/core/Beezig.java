/*
 * Copyright (C) 2019 Beezig Team
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

import eu.beezig.core.config.BeezigConfiguration;
import eu.beezig.core.server.ServerHive;
import eu.the5zig.mod.ModAPI;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Plugin(name = "Beezig", version = "7.0.0")
public class Beezig {
    public static Logger logger = Logger.getLogger("Beezig");
    private static Beezig instance;
    private ModAPI api;
    private ExecutorService asyncExecutor;
    private BeezigConfiguration config;

    @EventHandler
    public void load(LoadEvent event) {
        logger.info("Load started");
        long timeStart = System.currentTimeMillis();

        // Init fields
        instance = this;
        api = The5zigAPI.getAPI();
        asyncExecutor = Executors.newFixedThreadPool(5);

        // Register Hive stuff
        api.registerServerInstance(this, ServerHive.class);

        logger.info("Load ended in " + (System.currentTimeMillis() - timeStart) + " ms.");
    }

    public ExecutorService getAsyncExecutor() {
        return asyncExecutor;
    }

    public BeezigConfiguration getConfig() {
        return config;
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
}
