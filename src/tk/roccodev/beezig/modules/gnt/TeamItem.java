package tk.roccodev.beezig.modules.gnt;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.Giant;

public class TeamItem extends GameModeItem<Giant> {

    public TeamItem() {
        super(Giant.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            return Giant.team.replaceAll("§l", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.gnt.team";}


    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && getGameMode().getState() == GameState.GAME);
        } catch (Exception e) {
            return false;
        }
    }

}
