package tk.roccodev.beezig.modules.sky;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.SKY;

import java.text.DecimalFormat;

public class KDRChangeItem extends GameModeItem<SKY> {

    public KDRChangeItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);

            return df.format(SKY.gameKdr) + " (" + (SKY.gameKdr - SKY.apiKdr > 0 ? "+" + df.format(SKY.gameKdr - SKY.apiKdr) : df.format(SKY.gameKdr - SKY.apiKdr)) + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.sky.kdchange";}

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (SKY.shouldRender(getGameMode().getState()) && (SKY.apiKdr - SKY.gameKdr != 0));
        } catch (Exception e) {
            return false;
        }
    }

}
