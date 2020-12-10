package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class SLAP extends ShuffleMode {
    @Override
    public void shuffleWin() {
        addPoints(200);
    }

    @Override
    public String getIdentifier() {
        return "slap";
    }

    @Override
    public String getName() {
        return "Slaparoo";
    }

    public static class SlapListener extends SHUListener.ShuffleModeListener<SLAP> {
        @Override
        public Class<SLAP> getGameMode() {
            return SLAP.class;
        }

        @Override
        public boolean matchLobby(String s) {
            return "slap".equals(s);
        }

        @Override
        public void onMatch(SLAP gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            if("slap.kill".equals(key)) gameMode.addPoints(10);
        }
    }
}
