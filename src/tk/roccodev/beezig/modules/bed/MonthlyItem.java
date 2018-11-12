package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;

public class MonthlyItem extends GameModeItem<BED> {

    public MonthlyItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if(dummy) return "No Profile";
        if(!BED.hasLoaded) return "Loading...";
        return "#" + BED.monthly.getPlace() + " ▏ " + BED.monthly.getPoints();

    }

    @Override
    public String getName() {
        return Log.t("beezig.module.monthly");
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if(dummy) return false;
        try {
            if (getGameMode() == null)
                return false;
            if(BED.monthly == null || !BED.hasLoaded) return false;
            return BED.shouldRender(getGameMode().getState());
        } catch (Exception e) {
            return false;
        }
    }

}
