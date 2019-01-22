package eu.beezig.core.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.beezig.core.Log;
import eu.beezig.core.games.TIMV;

public class DBodiesItem extends GameModeItem<TIMV> {

    public DBodiesItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return TIMV.detectivesDiscovered + "/" + TIMV.detectivesBefore + " " + Log.t("beezig.str.timv.detectives");

    }

    @Override
    public String getTranslation() { return "beezig.module.timv.dbodies";}


    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (getGameMode().getState() == GameState.GAME);
        } catch (Exception e) {
            return false;
        }
    }

}
