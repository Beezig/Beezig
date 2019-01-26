package eu.beezig.core.modules.mimv;

import eu.beezig.core.games.MIMV;
import eu.the5zig.mod.modules.GameModeItem;

public class KarmaCounterItem extends GameModeItem<MIMV> {

    public KarmaCounterItem() {
        super(MIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return MIMV.gamePts;
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
            return dummy || (MIMV.shouldRender(getGameMode().getState()) && MIMV.gamePts != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
