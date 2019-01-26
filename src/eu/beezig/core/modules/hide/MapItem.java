package eu.beezig.core.modules.hide;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.HIDE;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<HIDE> {

    public MapItem() {
        super(HIDE.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            boolean mostKills = (boolean) getProperties().getSetting("showrecord").get();


            return HIDE.activeMap + (mostKills ? " (" + HIDE.mostKills + ")" : "");
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
    public void registerSettings() {
        getProperties().addSetting("showrecord", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("hide") && HIDE.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
