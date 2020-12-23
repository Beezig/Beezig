package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class IG extends ShuffleMode {
    @Override
    public void shuffleWin() {
        addPoints(200);
    }

    @Override
    public String getIdentifier() {
        return "ig";
    }

    @Override
    public String getName() {
        return "Instagib";
    }

    @Override
    public void addKills(int kills) {
        super.addKills(kills);
        addPoints(kills * 3);
    }

    public static class IgListener extends SHUListener.ShuffleModeListener<IG> {
        @Override
        public Class<IG> getGameMode() {
            return IG.class;
        }

        @Override
        public boolean matchLobby(String s) {
            return "ig".equals(s);
        }

        @Override
        public void onMatch(IG gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            if("ig.kill".equals(key)) gameMode.addKills(1);
            else if("ig.streak".equals(key) && match.get(0).equals(ServerHive.current().getNick()))
                gameMode.addPoints(2 + Integer.parseInt(match.get(1), 10));
        }
    }
}
