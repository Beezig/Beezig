package eu.beezig.core.util;

import eu.beezig.core.Beezig;
import eu.beezig.core.Constants;
import eu.beezig.core.Version;
import eu.beezig.core.config.Settings;
import io.sentry.Sentry;
import io.sentry.context.Context;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.ExceptionInterface;

public class ExceptionHandler {
    public static void configure() {
        Sentry.init("https://e3f6f6503714432ba48abc5fad305b20@o458856.ingest.sentry.io/5457112");
        Context ctx = Sentry.getContext();
        ctx.addTag("version_group", Constants.VERSION);
        ctx.addTag("version", Beezig.getVersionString());
        ctx.addTag("os", System.getProperty("os.name"));
        ctx.addTag("forge", String.valueOf(Beezig.api().isForgeEnvironment()));
        Version forge = Beezig.get().getBeezigForgeVersion(), laby = Beezig.get().getBeezigLabyVersion();
        if(forge != null) ctx.addTag("version_bforge", forge.getVersionDisplay());
        if(laby != null) ctx.addTag("version_blaby", laby.getVersionDisplay());
        ctx.addTag("platform", Beezig.get().isLaby() ? "LabyMod" : "5zig");
    }

    public static void stop() {
       Sentry.close();
    }

    public static void catchException(Throwable throwable, String prefix) {
        Beezig.logger.error(prefix, throwable);
        if(!Beezig.DEBUG && Settings.TELEMETRY.get().getBoolean()) {
            EventBuilder builder = new EventBuilder().withMessage(prefix + ": " + throwable.getMessage())
                .withLevel(Event.Level.ERROR)
                .withSentryInterface(new ExceptionInterface(throwable));
            Sentry.capture(builder);
        }
    }

    public static void catchException(Throwable throwable) {
        catchException(throwable, "Unhandled exception");
    }
}
