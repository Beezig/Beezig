package eu.beezig.core.modules.dr;

import eu.beezig.core.Log;
import eu.beezig.core.games.DR;
import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<DR> {

    public MapItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        DRMap map = DR.activeMap;
        if (map == null) return "Unknown map";
        String name = map.getDisplayName();
        StringBuilder tr = new StringBuilder();
        tr.append(name);
        if ((boolean) getProperties().getSetting("showcheckpoints").get() && "Runner".equals(DR.role)) {
            int totalc = map.getCheckpoints();
            tr.append(" (").append(DR.checkpoints).append("/").append(totalc).append(" ").append(Log.t("beezig.str.dr.checkpoints")).append(")");
        }
        return tr.toString().trim();
    }

    @Override
    public String getTranslation() {
        return "beezig.module.map";
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showcheckpoints", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null) return false;
            return dummy || (DR.shouldRender(getGameMode().getState()) && DR.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
