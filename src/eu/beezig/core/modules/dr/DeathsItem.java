package eu.beezig.core.modules.dr;

import eu.beezig.core.games.DR;
import eu.the5zig.mod.modules.GameModeItem;

public class DeathsItem extends GameModeItem<DR> {

    public DeathsItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return DR.deaths;
    }

    @Override
    public String getTranslation() {
        return "beezig.module.deaths";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (DR.shouldRender(getGameMode().getState()) && DR.role.equals("Runner") && DR.deaths != 0);
        } catch (Exception e) {
            return false;
        }
    }

}