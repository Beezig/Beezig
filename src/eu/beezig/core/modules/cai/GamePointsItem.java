package eu.beezig.core.modules.cai;

import eu.beezig.core.games.CAI;
import eu.the5zig.mod.modules.GameModeItem;

public class GamePointsItem extends GameModeItem<CAI> {

    public GamePointsItem() {
        super(CAI.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            return CAI.gamePoints;

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
            if (CAI.gamePoints == 0)
                return false;
            return dummy || (CAI.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
