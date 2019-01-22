package eu.beezig.core.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import eu.beezig.core.games.BED;

public class TeamsLeftItem extends GameModeItem<BED> {

    public TeamsLeftItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return BED.teamsLeft;

    }

    @Override
    public String getTranslation() { return "beezig.module.bed.teamsleft";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (BED.shouldRender(getGameMode().getState()) && BED.teamsLeft != 0);
        } catch (Exception e) {
            return false;
        }
    }
}
