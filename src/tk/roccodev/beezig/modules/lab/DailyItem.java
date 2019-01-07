package tk.roccodev.beezig.modules.lab;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.LAB;

public class DailyItem extends GameModeItem<LAB> {

    public DailyItem() {
        super(LAB.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return Log.df(LAB.dailyPoints) + " " + Log.t("beezig.module.lab.atoms");


    }


    @Override
    public String getTranslation() { return "beezig.module.daily";}

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
