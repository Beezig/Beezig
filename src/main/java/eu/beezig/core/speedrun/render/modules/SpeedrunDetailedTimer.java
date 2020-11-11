package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.the5zig.mod.render.RenderHelper;
import livesplitcore.DetailedTimerComponentState;
import org.lwjgl.opengl.GL11;

public class SpeedrunDetailedTimer extends TimerModule {
    private static final float SCALE_FACTOR_TOTAL = 2f;
    private static final float SCALE_FACTOR_TOTAL_FRACTION = 1.6f;
    private static final float SCALE_FACTOR_CURRENT = 1f;
    private static final float SCALE_FACTOR_CURRENT_FRACTION = 0.6f;

    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        DetailedTimerComponentState state = run.getDetailedTimerState();
        String timerTime = state == null ? "0" : state.timerTime(), timerFraction = state == null ? ".000" : state.timerFraction();
        String segmentTime = state == null ? "0" : state.segmentTimerTime(), segmentFraction = state == null ? ".000" : state.segmentTimerFraction();
        int timerTimeLength = (int) (renderer.getStringWidth(timerTime) * SCALE_FACTOR_TOTAL);
        int timerFractionLength = (int) (renderer.getStringWidth(timerFraction) * SCALE_FACTOR_TOTAL_FRACTION);
        int segmentTimeLength = (int) (renderer.getStringWidth(segmentTime) * SCALE_FACTOR_CURRENT);
        int segmentFractionLength = (int) (renderer.getStringWidth(segmentFraction) * SCALE_FACTOR_CURRENT_FRACTION);
        int segmentY = (int) (y + 9 * SCALE_FACTOR_TOTAL);
        int color = state == null ? 0xFFFFFFFF : run.getColorConfig().getColor(state.timerSemanticColor());

        // Total
        drawScaledString(renderer, timerTime, x + TimerRenderer.MODULE_WIDTH - timerFractionLength - timerTimeLength, y, SCALE_FACTOR_TOTAL, color);
        drawScaledString(renderer, timerFraction, x + TimerRenderer.MODULE_WIDTH - timerFractionLength,
            (int) (y + 9 * (SCALE_FACTOR_TOTAL - SCALE_FACTOR_TOTAL_FRACTION)), SCALE_FACTOR_TOTAL_FRACTION, color);

        // Segment
        drawScaledString(renderer, segmentTime, x + TimerRenderer.MODULE_WIDTH - segmentFractionLength - segmentTimeLength, segmentY, SCALE_FACTOR_CURRENT, color);
        drawScaledString(renderer, segmentFraction, x + TimerRenderer.MODULE_WIDTH - segmentFractionLength,
            (int) (segmentY + 9 * (SCALE_FACTOR_CURRENT - SCALE_FACTOR_CURRENT_FRACTION)), SCALE_FACTOR_CURRENT_FRACTION, color);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    private void drawScaledString(RenderHelper renderHelper, String text, int x, int y, float scale, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 1);
        GL11.glScalef(scale, scale, scale);
        renderHelper.drawString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    @Override
    public int getHeight() {
        return (int) ((9 * SCALE_FACTOR_TOTAL) + (9 * SCALE_FACTOR_CURRENT));
    }
}
