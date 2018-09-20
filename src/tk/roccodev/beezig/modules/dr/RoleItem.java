package tk.roccodev.beezig.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;


public class RoleItem extends GameModeItem<DR> {

    public RoleItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return DR.role;
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.role");
    }

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
