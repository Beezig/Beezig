package tk.roccodev.beezig.modules.cai;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.CAI;
import tk.roccodev.beezig.modules.utils.RenderUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CooldownsItem extends GameModeItem<CAI> {

    public CooldownsItem() {
        super(CAI.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return "";
    }

    private DecimalFormat df = new DecimalFormat("00");

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        RenderUtils.enableBlend();
        if(CAI.speedCooldown != 0) {

            y += 12;

            long secs = CAI.speedCooldown / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Speed - " + display, x, y, 0);
        }
        if(CAI.invisCooldown != 0) {

            y += 12;

            long secs = CAI.invisCooldown / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Invisibility - " + display, x, y, 8);
        }
        if(CAI.leaderItem0 != 0) {

            y += 12;

            long secs = CAI.leaderItem0 / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Blind Carrier - " + display, x, y, 13);
        }
        if(CAI.leaderItem1 != 0) {

            y += 12;

            long secs = CAI.leaderItem1 / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Emergency Flare - " + display, x, y, 7);
        }
        if(CAI.leaderItem2 != 0) {

            y += 12;

            long secs = CAI.leaderItem2 / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Attempt Escape - " + display, x, y, 10);
        }


    }

    @Override
    public String getName() {
        return ""; // Ignored
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (CAI.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

    private void display(String display, int x, int y, int potion) {
        RenderUtils.color(1f, 1f, 1f, 1f);
        RenderUtils.bindTexture(RenderUtils.INVENTORY_BACKGROUND);
        float scale = 0.7f;
        RenderUtils.pushMatrix();
        RenderUtils.scale(scale, scale, scale);
        RenderUtils.translate(x / scale, (y - 3) / scale, 0f);
        RenderUtils.renderPotionIcon(potion);
        RenderUtils.popMatrix();
        The5zigAPI.getAPI().getRenderHelper().drawString(display, x + 16, y);
    }

}
