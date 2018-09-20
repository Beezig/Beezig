package tk.roccodev.beezig.modules.arcade;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Arcade;

public class GameItem extends GameModeItem<Arcade>  {


    public GameItem() {
        super(Arcade.class);
    }


    @Override
    protected Object getValue(boolean b) {
        if(getGameMode().gameDisplay == null) return "Unknown";
        return getGameMode().gameDisplay;
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.gnt.mode");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if(getGameMode() == null) return false;
        return dummy || getGameMode().getState() != GameState.FINISHED;
    }
}
