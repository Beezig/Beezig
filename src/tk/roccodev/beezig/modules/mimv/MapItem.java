package tk.roccodev.beezig.modules.mimv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.MIMV;

public class MapItem extends GameModeItem<MIMV> {

    public MapItem() {
        super(MIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return MIMV.map;
    }

    @Override
    public String getTranslation() { return "beezig.module.map";}

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null)
                return false;
            return dummy || (MIMV.shouldRender(getGameMode().getState()) && MIMV.map != null && !MIMV.map.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

}
