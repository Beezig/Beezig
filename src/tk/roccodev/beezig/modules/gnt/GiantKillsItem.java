package tk.roccodev.beezig.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Giant;

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
    public String getName() {
        return Log.t("beezig.module.gnt.giantkills");
    }


    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.giantKills != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
