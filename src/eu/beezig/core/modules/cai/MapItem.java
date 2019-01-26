package eu.beezig.core.modules.cai;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.CAI;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<CAI> {

    public MapItem() {
        super(CAI.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {
            if (CAI.activeMap == null) return "No Map";
            return CAI.activeMap;
        } catch (Exception e) {
            e.printStackTrace();
            return "No Map";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.map";
    }


    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("CAI") && CAI.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
