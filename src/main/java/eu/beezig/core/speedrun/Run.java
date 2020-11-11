package eu.beezig.core.speedrun;

import com.google.common.collect.ImmutableList;
import eu.beezig.core.Beezig;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.beezig.core.speedrun.render.config.SpeedrunColorConfig;
import eu.beezig.core.speedrun.render.modules.*;
import eu.beezig.core.util.ExceptionHandler;
import livesplitcore.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;

public class Run {
    public static final String GAME_NAME = "Minecraft: The Hive - DeathRun";
    public static final String CATEGORY = "Any%";

    private final livesplitcore.Run api;
    private Timer timer;
    private final File splits;
    private final TimerRenderer renderer;
    private SpeedrunColorConfig colorConfig;

    // Components
    private final Layout layout;
    private final GeneralLayoutSettings settings;
    private final DetailedTimerComponent detailedTimerComponent;
    private final SplitsComponent splitsComponent;
    private final PreviousSegmentComponent previousSegmentComponent;
    private final SumOfBestComponent sumOfBestComponent;

    public Run(String mapName, DR.MapData data) throws IOException {
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
        api.setGameName(GAME_NAME);
        api.setCategoryName(CATEGORY);
        for (int i = (int) api.len(); i < data.checkpoints; i++) {
            loadSegment("Checkpoint #" + (i + 1));
        }
        timer = Timer.create(api.copy());
        renderer = new TimerRenderer(this, ImmutableList.of(new SpeedrunGameInfo(), new SpeedrunSegmentView(),
            new SpeedrunDetailedTimer(), new SpeedrunPrevSegment(), new SpeedrunSumOfBest()));
        settings = GeneralLayoutSettings.createDefault();

        // Components
        layout = Layout.defaultLayout();
        detailedTimerComponent = new DetailedTimerComponent();
        splitsComponent = new SplitsComponent();
        previousSegmentComponent = new PreviousSegmentComponent();
        sumOfBestComponent = new SumOfBestComponent();
        colorConfig = new SpeedrunColorConfig();
    }

    public TimerRenderer getRenderer() {
        return renderer;
    }

    public SpeedrunColorConfig getColorConfig() {
        return colorConfig;
    }

    private void loadSegment(String name) {
        api.pushSegment(new Segment(name));
    }

    public void start() {
        if(isTimerRunning()) throw new IllegalStateException("Timer is already running");
        timer.start();
    }

    public void split() {
        if(!isTimerRunning()) return;
        timer.split();
    }

    public void forceEnd(TimeSpanRef gameTime) {
        if(!isTimerRunning()) return;
        for(int i = 0; i < api.len(); i++) timer.skipSplit();
        timer.setGameTime(gameTime);
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

    public DetailedTimerComponentState getDetailedTimerState() {
        if(timer == null || detailedTimerComponent == null) return null;
        return detailedTimerComponent.state(timer, settings);
    }

    public SplitsComponentState getSplitsState() {
        if(timer == null || splitsComponent == null) return null;
        return splitsComponent.state(timer, settings);
    }

    public PreviousSegmentComponentState getPreviousSegmentState() {
        if(timer == null || previousSegmentComponent == null) return null;
        return previousSegmentComponent.state(timer, settings);
    }

    public SumOfBestComponentState getSumOfBestComponent() {
        if(timer == null || sumOfBestComponent == null) return null;
        return sumOfBestComponent.state(timer);
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
