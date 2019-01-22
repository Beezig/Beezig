package eu.beezig.core.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.DR;

public class PBItem extends GameModeItem<DR> {

    public PBItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (DR.activeMap != null) {
            return DR.currentMapPB;
        } else {
            return "No Personal Best";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.dr.pb";}

    @Override
    public void registerSettings() {
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
