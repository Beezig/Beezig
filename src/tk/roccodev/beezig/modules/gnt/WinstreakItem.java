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

            return Giant.winstreak + (best ? " (" + Giant.bestStreak + ")" : "");

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.winstreak");
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showbest", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof Giant))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
