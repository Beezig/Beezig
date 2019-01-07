package tk.roccodev.beezig.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;

public class DailyItem extends GameModeItem<DR> {

    public DailyItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(DR.dailyPoints) + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getTranslation() { return "beezig.module.daily";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (DR.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
