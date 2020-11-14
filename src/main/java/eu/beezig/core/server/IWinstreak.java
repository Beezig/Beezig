package eu.beezig.core.server;

/**
 * Marker interface to load winstreaks data
 */
public interface IWinstreak {
    /**
     * This is required so that implementors don't forget to increment the streak.
     */
    void won();
}
