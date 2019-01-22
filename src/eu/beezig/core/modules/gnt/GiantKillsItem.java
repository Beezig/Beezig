package eu.beezig.core.modules.gnt;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.Giant;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class GiantKillsItem extends GameModeItem<Giant> {

    public GiantKillsItem() {
        super(Giant.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            return Giant.giantKills;
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.gnt.giantkills";}


    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.giantKills != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
