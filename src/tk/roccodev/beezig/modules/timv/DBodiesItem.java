package tk.roccodev.beezig.modules.timv;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;

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
