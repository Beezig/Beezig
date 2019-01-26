package eu.beezig.core.modules.arcade;

import eu.beezig.core.Log;
import eu.beezig.core.games.Arcade;
import eu.beezig.core.hiveapi.APIValues;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;

public class PointsItem extends GameModeItem<Arcade> {

    public PointsItem() {
        super(Arcade.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            return Log.df(APIValues.ArcadePoints);

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.points";
    }


    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            if (!super.shouldRender(dummy)) return false;
            return dummy || getGameMode().getState() != GameState.FINISHED;
        } catch (Exception e) {
            return false;
        }
    }

}
