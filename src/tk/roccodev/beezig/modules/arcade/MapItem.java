package tk.roccodev.beezig.modules.arcade;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Arcade;

public class MapItem extends GameModeItem<Arcade>  {


    public MapItem() {
        super(Arcade.class);
    }


    @Override
    protected Object getValue(boolean b) {
        if(b || getGameMode().map == null) return "Unknown";
        return getGameMode().map;
    }

    @Override
    public String getTranslation() { return "beezig.module.map";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if(getGameMode() == null) return false;
            if(!super.shouldRender(dummy)) return false;
            return dummy || (getGameMode().getState() != GameState.FINISHED && getGameMode().map != null);
        } catch(Exception e) {
            return false;
        }
    }
}
