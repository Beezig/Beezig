package tk.roccodev.beezig.modules.sky;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SKY;

public class DailyItem extends GameModeItem<SKY> {

    public DailyItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(SKY.dailyPoints) + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getName() {
        return Log.t("beezig.module.daily");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof SKY))
                return false;
            return dummy || (SKY.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
