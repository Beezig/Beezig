package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;

public class TeamsLeftItem extends GameModeItem<BED> {

    public TeamsLeftItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return BED.teamsLeft;

    }

    @Override
    public String getName() {
        return Log.t("beezig.module.bed.teamsleft");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof BED)) return false;
            return dummy || (BED.shouldRender(getGameMode().getState()) && BED.teamsLeft != 0);
        } catch (Exception e) {
            return false;
        }
    }
}
