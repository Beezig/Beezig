package tk.roccodev.beezig.modules.mimv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.MIMV;

public class DailyItem extends GameModeItem<MIMV> {

    public DailyItem() {
        super(MIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return MIMV.dailyPoints + " " + Log.t("beezig.module.timv.karma");

    }

    @Override
    public String getName() {
        return Log.t("beezig.module.daily");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof MIMV))
                return false;
            return dummy || (MIMV.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
