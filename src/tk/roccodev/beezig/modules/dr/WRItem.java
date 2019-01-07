package tk.roccodev.beezig.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;

public class WRItem extends GameModeItem<DR> {

    public WRItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (DR.activeMap != null) {
            if ((boolean) getProperties().getSetting("showusername").get()) {
                return DR.currentMapWR + " (" + DR.currentMapWRHolder + ")";
            }
            return DR.currentMapWR;
        } else {
            return "No Record";
        }

    }

    @Override
    public String getTranslation() { return "beezig.module.dr.wr";}

    @Override
    public void registerSettings() {
        getProperties().addSetting("showusername", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (DR.shouldRender(getGameMode().getState()) && DR.activeMap != null && DR.role.equals("Runner"));
        } catch (Exception e) {
            return false;
        }
    }

}
