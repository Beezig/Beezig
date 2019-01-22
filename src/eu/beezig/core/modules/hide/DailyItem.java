package eu.beezig.core.modules.hide;

import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.Log;
import eu.beezig.core.games.HIDE;

public class DailyItem extends GameModeItem<HIDE> {

    public DailyItem() {
        super(HIDE.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(HIDE.dailyPoints) + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getTranslation() { return "beezig.module.daily";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (HIDE.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
