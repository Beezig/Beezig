package eu.beezig.core.modules.bp;

import eu.beezig.core.Log;
import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.BP;

public class DailyItem extends GameModeItem<BP> {

    public DailyItem() {
        super(BP.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return Log.df(BP.dailyPoints) + " " + Log.t("beezig.module.points");


    }


    @Override
    public String getTranslation() { return "beezig.module.daily";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (BP.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
