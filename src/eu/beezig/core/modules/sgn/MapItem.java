package eu.beezig.core.modules.sgn;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.SGN;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<SGN> {

    public MapItem() {
        super(SGN.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {
            if (SGN.activeMap == null) return "No Map";
            return SGN.activeMap;
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
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("SGN") && SGN.activeMap != null && !SGN.activeMap.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

}
