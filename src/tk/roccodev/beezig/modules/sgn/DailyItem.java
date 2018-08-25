package tk.roccodev.beezig.modules.sgn;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SGN;

public class DailyItem extends GameModeItem<SGN> {

    public DailyItem() {
        super(SGN.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(SGN.dailyPoints) + " " + Log.t("beezig.module.points");
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.daily");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof SGN))
                return false;
            return dummy || (SGN.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
