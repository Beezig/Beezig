package tk.roccodev.beezig.modules.gnt;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Giant;

public class WinstreakItem extends GameModeItem<Giant> {

    public WinstreakItem() {
        super(Giant.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            boolean best = (boolean) getProperties().getSetting("showbest").get();

            return Log.df(Giant.winstreak) + (best ? " (" + Log.df(Giant.bestStreak) + ")" : "");

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
            return getGameMode() != null;
        } catch (Exception e) {
            return false;
        }
    }

}
