package tk.roccodev.beezig.modules.sgn;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SGN;

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
    public String getName() {
        return Log.t("beezig.module.map");
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
