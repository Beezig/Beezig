package eu.beezig.core.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.DR;


public class RoleItem extends GameModeItem<DR> {

    public RoleItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return DR.role;
    }

    @Override
    public String getTranslation() { return "beezig.module.role";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (DR.shouldRender(getGameMode().getState()) && DR.role != null);
        } catch (Exception e) {
            return false;
        }
    }

}
