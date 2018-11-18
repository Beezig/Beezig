package tk.roccodev.beezig.modules.mimv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.MIMV;

public class KarmaCounterItem extends GameModeItem<MIMV> {

    public KarmaCounterItem() {
        super(MIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return MIMV.gamePts;
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.game");
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
