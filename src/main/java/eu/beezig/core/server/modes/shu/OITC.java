package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class OITC extends ShuffleMode {
    @Override
    public void shuffleWin() {
        addPoints(200);
    }

    @Override
    public String getIdentifier() {
        return "oitc";
    }

    @Override
    public String getName() {
        return "One in the Chamber";
    }

    @Override
    public void addKills(int kills) {
        super.addKills(kills);
        addPoints(kills * 5);
    }

    public static class OitcListener extends SHUListener.ShuffleModeListener<OITC> {
        @Override
        public Class<OITC> getGameMode() {
            return OITC.class;
        }

        @Override
        public boolean matchLobby(String s) {
            return "oitc".equals(s);
        }

        @Override
        public void onMatch(OITC gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            if("oitc.kill".equals(key)) gameMode.addKills(1);
            else if("oitc.streak".equals(key) && match.get(0).equals(ServerHive.current().getNick()))
                gameMode.addPoints(2 + Integer.parseInt(match.get(1), 10));
        }
    }
}
