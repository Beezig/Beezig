package eu.beezig.core.modules.gnt;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.games.Giant;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class DeathsItem extends GameModeItem<Giant> {

    public DeathsItem() {
        super(Giant.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            if ((boolean) getProperties().getSetting("showtotal").get())
                return Giant.gameDeaths + " (" + Log.df(Giant.gameDeaths + Giant.totalDeaths) + ")";
            return Giant.gameDeaths;
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.deaths";
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showtotal", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.gameDeaths != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
