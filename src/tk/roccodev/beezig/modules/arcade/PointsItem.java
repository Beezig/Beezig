package tk.roccodev.beezig.modules.arcade;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Arcade;
import tk.roccodev.beezig.hiveapi.APIValues;

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
    public String getName() {
        return Log.t("beezig.module.points");
    }


    @Override
    public boolean shouldRender(boolean dummy) {
        if(getGameMode() == null) return false;
        if(!super.shouldRender(dummy)) return false;
        return dummy || getGameMode().getState() != GameState.FINISHED;
    }

}
