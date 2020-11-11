package eu.beezig.core.speedrun;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import livesplitcore.ParseRunResult;
import livesplitcore.Segment;
import livesplitcore.Timer;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;

public class Run {
    private final livesplitcore.Run api;
    private Timer timer;
    private final File splits;

    public Run(String mapName) throws IOException {
        splits = new File(Beezig.get().getBeezigDir(), "dr/splits/" + FilenameUtils.getName(mapName) + ".lss");
        if(!splits.exists()) {
            splits.getParentFile().mkdirs();
            splits.createNewFile();
        }
        try(InputStream stream = Files.newInputStream(splits.toPath())) {
            ParseRunResult result = livesplitcore.Run.parse(stream, splits.getAbsolutePath(), false);
            if(!result.parsedSuccessfully()) throw new IOException("Couldn't parse run");
            api = result.unwrap();
        }
        api.setGameName("Minecraft: The Hive - DeathRun");
        api.setCategoryName("Any%");
    }

    public void loadSegment(String name) {
        api.pushSegment(new Segment(name));
    }

    public void start() {
        if(isTimerRunning()) throw new IllegalStateException("Timer is already running");
        timer = Timer.create(api.copy());
        timer.start();
    }

    public void split() {
        if(!isTimerRunning()) return;
        timer.split();
    }

    /**
     * Resets the ongoing timer, or does nothing if the timer isn't running.
     * @param saveAttempt whether the current attempt should be saved to disk
     */
    public void reset(boolean saveAttempt) {
        if(!isTimerRunning()) return;
        timer.reset(saveAttempt);
    }

    public double getSeconds() {
        if(!isTimerRunning()) return 0D;
        return timer.currentTime().realTime().totalSeconds();
    }

    public void save() {
        if(timer != null) {
            try {
                Files.write(splits.toPath(), Collections.singleton(timer.saveAsLss()), Charset.defaultCharset());
            } catch (IOException e) {
                ExceptionHandler.catchException(e, "Run save");
            }
        }
    }

    public void endNow() {
        if(timer != null) timer.close();
        api.close();
    }

    public boolean isTimerRunning() {
        return timer != null && timer.currentPhase() == 1; // TimerPhase::Running
    }
}
