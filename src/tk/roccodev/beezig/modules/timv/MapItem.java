package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVMap;

public class MapItem extends GameModeItem<TIMV> {

    public MapItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        TIMVMap map = TIMV.activeMap;
        if (map == null) return "Unknown map";
        String name = map.getName();
        StringBuilder tr = new StringBuilder();
        tr.append(name);
        if ((boolean) getProperties().getSetting("showenderchests").get()) {

            int t = map.getEnderchests();

            tr.append(" (").append(t).append(")");


        }


        return tr.toString().trim();
    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showenderchests", true);
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.map");
    }

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (!(getGameMode() instanceof TIMV)) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
