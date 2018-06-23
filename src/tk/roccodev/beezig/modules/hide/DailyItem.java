package tk.roccodev.beezig.modules.hide;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.HIDE;

public class DailyItem extends GameModeItem<HIDE> {

    public DailyItem() {
        super(HIDE.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return HIDE.dailyPoints + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getName() {
        return Log.t("beezig.module.daily");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof HIDE))
                return false;
            return dummy || (HIDE.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
