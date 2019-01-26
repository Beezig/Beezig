package eu.beezig.core.modules.timv;

import eu.beezig.core.Log;
import eu.beezig.core.games.TIMV;
import eu.the5zig.mod.modules.GameModeItem;

public class DailyKarmaItem extends GameModeItem<TIMV> {

    public DailyKarmaItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return Log.df(TIMV.dailyKarma) + " " + Log.t("beezig.module.timv.karma");


    }


    @Override
    public String getTranslation() {
        return "beezig.module.daily";
    }

    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
