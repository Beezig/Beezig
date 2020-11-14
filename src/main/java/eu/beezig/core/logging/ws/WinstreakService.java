package eu.beezig.core.logging.ws;

import com.google.gson.annotations.SerializedName;

public class WinstreakService {
    @SerializedName("streak")
    private int current;
    @SerializedName("bestStreak")
    private int best;
    @SuppressWarnings("unused")
    private Long lastReset, bestReset;

    /**
     * This is true if the player has won
     */
    private transient boolean poisoned;

    public void increment() {
        poisoned = true;
        current++;
        if(best < current) best = current;
    }

    public void reset() {
        if(poisoned) return;
        lastReset = System.currentTimeMillis();
        if(best <= current) bestReset = lastReset;
        current = 0;
    }

    public int getCurrent() {
        return current;
    }

    public int getBest() {
        return best;
    }
}
