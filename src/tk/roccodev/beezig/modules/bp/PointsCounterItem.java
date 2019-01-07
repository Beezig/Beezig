package tk.roccodev.beezig.modules.bp;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BP;

public class PointsCounterItem extends GameModeItem<BP> {

    public PointsCounterItem() {
        super(BP.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return BP.gamePts + " " + Log.t("beezig.module.points");
    }

    @Override
    public String getTranslation() { return "beezig.module.game";}

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
