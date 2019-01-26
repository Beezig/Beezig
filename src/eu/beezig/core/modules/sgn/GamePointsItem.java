package eu.beezig.core.modules.sgn;

import eu.beezig.core.games.SGN;
import eu.the5zig.mod.modules.GameModeItem;

public class GamePointsItem extends GameModeItem<SGN> {

    public GamePointsItem() {
        super(SGN.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            return SGN.gamePts;

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.game";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            if (SGN.gamePts == 0)
                return false;
            return dummy || (SGN.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
