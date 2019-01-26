package eu.beezig.core.modules.timv;

import eu.beezig.core.Log;
import eu.beezig.core.games.TIMV;
import eu.the5zig.mod.modules.GameModeItem;

public class KarmaCounterItem extends GameModeItem<TIMV> {

    public KarmaCounterItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        StringBuilder sb = new StringBuilder();
        sb.append(TIMV.karmaCounter).append(" ").append(Log.t("beezig.module.timv.karma"));
        if ((boolean) getProperties().getSetting("showrolepoints").get()) {

            if (TIMV.dPoints != 0) {
                sb.append(" / ").append(TIMV.dPoints).append(" ").append(Log.t("beezig.str.timv.dpoints"));
            }
            if (TIMV.iPoints != 0) {
                sb.append(" / ").append(TIMV.iPoints).append(" ").append(Log.t("beezig.str.timv.ipoints"));
            }
            if (TIMV.tPoints != 0) {
                sb.append(" / ").append(TIMV.tPoints).append(" ").append(Log.t("beezig.str.timv.tpoints"));
            }


        }
        return sb.toString().trim();


    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showrolepoints", true);


    }

    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }


    @Override
    public String getTranslation() {
        return "beezig.module.game";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.karmaCounter != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
