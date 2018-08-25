package tk.roccodev.beezig.modules.bp;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BP;

public class DailyItem extends GameModeItem<BP> {

    public DailyItem() {
        super(BP.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return Log.df(BP.dailyPoints) + " " + Log.t("beezig.module.points");


    }


    @Override
    public String getName() {
        return Log.t("beezig.module.daily");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof BP)) return false;
            return dummy || (BP.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
