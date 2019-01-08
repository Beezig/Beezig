package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;

public class DailyKarmaItem extends GameModeItem<TIMV> {

    public DailyKarmaItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return Log.df(TIMV.dailyKarma) + " " + Log.t("beezig.module.timv.karma");


    }


    @Override
    public String getTranslation() { return "beezig.module.daily";}

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
