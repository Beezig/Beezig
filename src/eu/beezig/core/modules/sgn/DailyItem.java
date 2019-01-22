package eu.beezig.core.modules.sgn;

import eu.beezig.core.Log;
import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.SGN;

public class DailyItem extends GameModeItem<SGN> {

    public DailyItem() {
        super(SGN.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(SGN.dailyPoints) + " " + Log.t("beezig.module.points");
    }

    @Override
    public String getTranslation() { return "beezig.module.daily";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (SGN.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
