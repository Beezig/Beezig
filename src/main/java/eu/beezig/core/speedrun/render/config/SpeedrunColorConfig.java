package eu.beezig.core.speedrun.render.config;

public class SpeedrunColorConfig {
    /**
     * Default color
     */
    private int defaultColor = 0xFFFFFFFF;

    /**
     * The runner is ahead of the comparison and is gaining even more time.
     */
    private int aheadGainingTime = 0xFF00CC36;

    /**
     * The runner is ahead of the comparison, but is losing time.
     */
    private int aheadLosingTime = 0xFF61D17F;

    /**
     * The runner is behind the comparison and is losing even more time.
     */
    private int behindLosingTime = 0xFFCC0000;

    /**
     * The runner is behind the comparison, but is gaining back time.
     */
    private int behindGainingTime = 0xFFD16161;

    /**
     * The runner achieved a best segment. (aka gold split)
     */
    private int bestSegment = 0xFFFFD500;

    /**
     * There's no active attempt.
     */
    private int notRunning = 0xFFABABAB;

    /**
     * The timer is paused.
     */
    private int paused = 0xFF7A7A7A;

    /**
     * The runner achieved a new Personal Best.
     */
    private int personalBest = 0xFF14A5FF;

    /**
     * The background for the current segment
     */
    private int currentSegment = 0x8014A5FF;

    public int getColor(String semantic) {
        switch (semantic) {
            case "Default":
                break;
            case "AheadGainingTime":
                return aheadGainingTime;
            case "AheadLosingTime":
                return aheadLosingTime;
            case "BehindGainingTime":
                return behindGainingTime;
            case "BehindLosingTime":
                return behindLosingTime;
            case "BestSegment":
                return bestSegment;
            case "NotRunning":
                return notRunning;
            case "Paused":
                return paused;
            case "PersonalBest":
                return personalBest;
        }
        return defaultColor;
    }

    public void setAheadGainingTime(int aheadGainingTime) {
        this.aheadGainingTime = aheadGainingTime;
    }

    public void setAheadLosingTime(int aheadLosingTime) {
        this.aheadLosingTime = aheadLosingTime;
    }

    public void setBehindLosingTime(int behindLosingTime) {
        this.behindLosingTime = behindLosingTime;
    }

    public void setBehindGainingTime(int behindGainingTime) {
        this.behindGainingTime = behindGainingTime;
    }

    public void setBestSegment(int bestSegment) {
        this.bestSegment = bestSegment;
    }

    public void setNotRunning(int notRunning) {
        this.notRunning = notRunning;
    }

    public void setPaused(int paused) {
        this.paused = paused;
    }

    public void setPersonalBest(int personalBest) {
        this.personalBest = personalBest;
    }

    public void setDefault(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setCurrentSegment(int currentSegment) {
        this.currentSegment = currentSegment;
    }

    public int getCurrentSegment() {
        return currentSegment;
    }
}
