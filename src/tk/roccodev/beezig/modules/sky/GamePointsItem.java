package tk.roccodev.beezig.modules.sky;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SKY;

public class GamePointsItem extends GameModeItem<SKY> {

    public GamePointsItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            return SKY.gamePoints;

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.game");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof SKY))
                return false;
            if (SKY.gamePoints == 0)
                return false;
            return dummy || (SKY.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
