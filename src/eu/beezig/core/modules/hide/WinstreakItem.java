package eu.beezig.core.modules.hide;

import eu.beezig.core.Log;
import eu.beezig.core.games.HIDE;
import eu.the5zig.mod.modules.GameModeItem;

public class WinstreakItem extends GameModeItem<HIDE> {

    public WinstreakItem() {
        super(HIDE.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            boolean best = (boolean) getProperties().getSetting("showbest").get();

            return Log.df(HIDE.winstreak) + (best ? " (" + Log.df(HIDE.bestStreak) + ")" : "");

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
            return dummy || (HIDE.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
