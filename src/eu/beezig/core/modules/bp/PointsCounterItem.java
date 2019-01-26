package eu.beezig.core.modules.bp;

import eu.beezig.core.Log;
import eu.beezig.core.games.BP;
import eu.the5zig.mod.modules.GameModeItem;

public class PointsCounterItem extends GameModeItem<BP> {

    public PointsCounterItem() {
        super(BP.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return BP.gamePts + " " + Log.t("beezig.module.points");
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
            return dummy || (BP.shouldRender(getGameMode().getState()) && BP.gamePts != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
