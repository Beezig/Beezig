package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;

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


    @Override
    public String getName() {
        return Log.t("beezig.module.game");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof TIMV)) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.karmaCounter != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
