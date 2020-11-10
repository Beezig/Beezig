package eu.beezig.core.speedrun;

import livesplitcore.Segment;
import livesplitcore.Timer;

public class Run {
    private final livesplitcore.Run api;
    private Timer timer;

    public Run() {
        api = new livesplitcore.Run();
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

    public boolean isTimerRunning() {
        return timer != null && timer.currentPhase() == 1; // TimerPhase::Running
    }
}
