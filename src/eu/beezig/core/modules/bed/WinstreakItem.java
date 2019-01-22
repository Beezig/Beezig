package eu.beezig.core.modules.bed;

import eu.beezig.core.Log;
import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.BED;

public class WinstreakItem extends GameModeItem<BED> {

    public WinstreakItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            boolean best = (boolean) getProperties().getSetting("showbest").get();

            return Log.df(BED.winstreak) + (best ? " (" + Log.df(BED.bestStreak) + ")" : "");

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.winstreak";}

    @Override
    public void registerSettings() {
        getProperties().addSetting("showbest", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            return dummy || (BED.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
