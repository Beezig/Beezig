package eu.beezig.core.modules.sky;

import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.SKY;

public class ModeItem extends GameModeItem<SKY> {

    public ModeItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {

            return SKY.mode;

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.sky.mode";}


    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            if (SKY.mode == null || SKY.mode.isEmpty())
                return false;
            return dummy || (SKY.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
