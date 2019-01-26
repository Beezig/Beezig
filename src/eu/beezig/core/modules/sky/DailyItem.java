package eu.beezig.core.modules.sky;

import eu.beezig.core.Log;
import eu.beezig.core.games.SKY;
import eu.the5zig.mod.modules.GameModeItem;

public class DailyItem extends GameModeItem<SKY> {

    public DailyItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(SKY.dailyPoints) + " " + Log.t("beezig.module.points");

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
            return dummy || (SKY.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
