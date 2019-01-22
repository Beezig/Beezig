package eu.beezig.core.modules.gnt;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.games.Giant;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class DailyItem extends GameModeItem<Giant> {

    public DailyItem() {
        super(Giant.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return Log.df(Giant.dailyPoints) + " " + Log.t("beezig.module.points");

    }

    @Override
    public String getTranslation() { return "beezig.module.daily";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")));
        } catch (Exception e) {
            return false;
        }
    }

}
