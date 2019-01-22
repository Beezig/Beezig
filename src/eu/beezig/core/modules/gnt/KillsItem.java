package eu.beezig.core.modules.gnt;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.games.Giant;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class KillsItem extends GameModeItem<Giant> {

    public KillsItem() {
        super(Giant.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            if ((boolean) getProperties().getSetting("showtotal").get())
                return Giant.gameKills + " (" + Log.df(Giant.gameKills + Giant.totalKills) + ")";
            return Giant.gameKills;
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.kills";}

    @Override
    public void registerSettings() {
        getProperties().addSetting("showtotal", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.gameKills != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
