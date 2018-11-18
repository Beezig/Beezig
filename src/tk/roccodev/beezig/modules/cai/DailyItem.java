package tk.roccodev.beezig.modules.cai;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.CAI;

public class DailyItem extends GameModeItem<CAI> {

    public DailyItem() {
        super(CAI.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(CAI.dailyPoints) + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getName() {
        return Log.t("beezig.module.daily");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (CAI.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
