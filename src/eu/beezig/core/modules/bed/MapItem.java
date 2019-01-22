package eu.beezig.core.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.BED;

public class MapItem extends GameModeItem<BED> {

    public MapItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        if ((boolean) getProperties().getSetting("mode").get()) return BED.activeMap + " (" + BED.mode + ")";
        return BED.activeMap;
    }

    @Override
    public String getTranslation() { return "beezig.module.map";}

    @Override
    public void registerSettings() {
        getProperties().addSetting("mode", true);
    }


    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null) return false;
            return dummy || (BED.shouldRender(getGameMode().getState()) && BED.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
