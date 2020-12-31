/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.util;

import eu.beezig.core.Beezig;
import eu.beezig.core.Constants;
import eu.beezig.core.Version;
import eu.beezig.core.config.Settings;
import io.sentry.Sentry;

public class ExceptionHandler {
    public static void init() {
        Sentry.init(options -> {
            options.setDsn("https://e3f6f6503714432ba48abc5fad305b20@o458856.ingest.sentry.io/5457112");
            options.setDebug(Beezig.DEBUG);
            options.setRelease(Beezig.getVersionString());
            options.setDist(Constants.VERSION);
            options.setBeforeSend((event, hint) -> {
                if(Beezig.DEBUG || !Settings.TELEMETRY.get().getBoolean()) return null;
                return event;
            });
        });
        Sentry.setTag("version", Beezig.getVersionString());
        Sentry.setTag("os", System.getProperty("os.name"));
        Sentry.setTag("forge", String.valueOf(Beezig.api().isForgeEnvironment()));
        Version forge = Beezig.get().getBeezigForgeVersion(), laby = Beezig.get().getBeezigLabyVersion();
        if(forge != null) Sentry.setTag("version_bforge", forge.getVersionDisplay());
        if(laby != null) Sentry.setTag("version_blaby", laby.getVersionDisplay());
        Sentry.setTag("platform", Beezig.get().isLaby() ? "LabyMod" : "5zig");
    }

    public static void catchException(Throwable ex, String hint) {
        Beezig.logger.error(hint, ex);
        Sentry.captureException(ex, hint);
    }

    public static void stop() {
        Sentry.close();
    }

    public static void catchException(Throwable ex) {
        catchException(ex, "Unhandled exception");
    }
}
