package eu.beezig.core.modules.sky;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.SKY;

public class MapItem extends GameModeItem<SKY> {

    public MapItem() {
        super(SKY.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {
            if (SKY.map == null) return "No Map";
            return SKY.map;
        } catch (Exception e) {
            e.printStackTrace();
            return "No Map";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.map";}


    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("SKY") && SKY.map != null && !SKY.map.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

}
