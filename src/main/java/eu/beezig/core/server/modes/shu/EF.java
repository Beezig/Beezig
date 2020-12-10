package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class EF extends ShuffleMode {
    private boolean dead;

    @Override
    public void shuffleWin() {
        addPoints(75);
    }

    @Override
    public String getIdentifier() {
        return "ef";
    }

    @Override
    public String getName() {
        return "ElectricFloor";
    }

    public static class EfListener extends SHUListener.ShuffleModeListener<EF> {
        @Override
        public Class<EF> getGameMode() {
            return EF.class;
        }

        @Override
        public boolean matchLobby(String s) {
            return s.matches("eft?");
        }

        @Override
        public void onMatch(EF gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            if(!gameMode.dead && "ef.death".equals(key) && !match.get(0).equals(ServerHive.current().getNick())) gameMode.addPoints(20);
        }

        @Override
        public void onTitle(EF gameMode, String title, String subTitle) {
            if(subTitle != null) {
                if("§cYou died!".equals(subTitle.replace("§r", ""))) gameMode.dead = true;
            }
        }
    }
}
