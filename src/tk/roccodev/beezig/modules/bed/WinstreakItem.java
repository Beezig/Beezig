package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.BED;

public class WinstreakItem extends GameModeItem<BED> {

    public WinstreakItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            return BED.winstreak;
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getName() {
        return "Winstreak";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (!(getGameMode() instanceof BED)) return false;
            return dummy || (BED.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
