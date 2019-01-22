package eu.beezig.core.modules.timv;

import eu.beezig.core.games.TIMV;
import eu.beezig.core.hiveapi.stuff.timv.TIMVMap;
import eu.the5zig.mod.modules.GameModeItem;

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

            boolean current = (boolean) getProperties().getSetting("showcurrent").get();
            int t = map.getEnderchests();

            tr.append(" (").append(t);
            if(current){

                if ((TIMV.activeMap.getName().equalsIgnoreCase("Precinct") || TIMV.activeMap.getName().equalsIgnoreCase("Azure Island")) && TIMV.currentEnderchests != 0) {
                    //remove unaccessible echest from tracker
                    tr.append(" | ").append(TIMV.currentEnderchests - 1);
                } else tr.append(" | ").append(TIMV.currentEnderchests);
            }
            tr.append(")");
        }

        return tr.toString().trim();
    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showenderchests", true);
        getProperties().addSetting("showcurrent", true);
    }

    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }

    @Override
    public String getTranslation() { return "beezig.module.map";}

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
