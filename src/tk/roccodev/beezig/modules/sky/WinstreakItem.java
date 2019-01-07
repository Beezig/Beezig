package tk.roccodev.beezig.modules.sky;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SKY;

public class WinstreakItem extends GameModeItem<SKY> {

    public WinstreakItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            boolean best = (boolean) getProperties().getSetting("showbest").get();

            return Log.df(SKY.winstreak) + (best ? " (" + Log.df(SKY.bestStreak) + ")" : "");

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
            return dummy || (SKY.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
