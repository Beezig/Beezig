package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class CR extends ShuffleMode {
    @Override
    public void shuffleWin() {

    }

    @Override
    public String getIdentifier() {
        return "cr";
    }

    @Override
    public String getName() {
        return "Cranked";
    }

    public static class CRListener extends SHUListener.ShuffleModeListener<CR> {
        @Override
        public Class<CR> getGameMode() {
            return CR.class;
        }

        @Override
        public void onMatch(CR gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            // Unfinished
            if("cr.kill".equals(key)) gameMode.addPoints(3);
        }

        @Override
        public boolean matchLobby(String s) {
            return "cr".equals(s);
        }
    }
}
