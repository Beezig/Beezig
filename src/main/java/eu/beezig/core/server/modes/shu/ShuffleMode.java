package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.HiveMode;

public abstract class ShuffleMode extends HiveMode {
    /**
     * Called when the player won the Shuffle game
     */
    public abstract void shuffleWin();
}
