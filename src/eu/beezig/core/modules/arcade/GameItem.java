package eu.beezig.core.modules.arcade;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.beezig.core.games.Arcade;

public class GameItem extends GameModeItem<Arcade>  {


    public GameItem() {
        super(Arcade.class);
    }


    @Override
    protected Object getValue(boolean b) {
        if(b || getGameMode().gameDisplay == null) return "Unknown";
        return getGameMode().gameDisplay;
    }

    @Override
    public String getTranslation() { return "beezig.module.gnt.mode"; }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            if (!super.shouldRender(dummy)) return false;
            return dummy || getGameMode().getState() != GameState.FINISHED;
        } catch(Exception e) {
            return false;
        }
    }
}
