package eu.beezig.core.modules.bed;

import eu.beezig.core.Log;
import eu.beezig.core.games.BED;
import eu.the5zig.mod.modules.GameModeItem;

public class DailyItem extends GameModeItem<BED> {

    public DailyItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(BED.dailyPoints) + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getTranslation() {
        return "beezig.module.daily";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (BED.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
