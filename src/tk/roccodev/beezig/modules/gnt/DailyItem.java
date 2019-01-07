package tk.roccodev.beezig.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Giant;

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
