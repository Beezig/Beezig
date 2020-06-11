package eu.beezig.core.server.listeners;

import eu.beezig.core.Beezig;
import eu.beezig.core.server.modes.SKY;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;

public class SKYListener extends AbstractGameListener<SKY> {

    @Override
    public Class<SKY> getGameMode() {
        return SKY.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return "sky".equals(s);
    }

    @Override
    public void onMatch(SKY gameMode, String key, IPatternResult match) {
        if("sky.kill".equals(key)) {
            gameMode.addKills(1);
        }
        else if ("sky.win".equals(key)) {
            gameMode.setWon();
        }
    }
}
