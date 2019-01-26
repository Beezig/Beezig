package eu.beezig.core.modules.lab;

import eu.beezig.core.Log;
import eu.beezig.core.games.LAB;
import eu.the5zig.mod.modules.GameModeItem;

public class DailyItem extends GameModeItem<LAB> {

    public DailyItem() {
        super(LAB.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return Log.df(LAB.dailyPoints) + " " + Log.t("beezig.module.lab.atoms");


    }


    @Override
    public String getTranslation() {
        return "beezig.module.daily";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (LAB.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
