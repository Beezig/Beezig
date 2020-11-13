package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.the5zig.mod.render.RenderHelper;
import org.lwjgl.opengl.GL11;

public class SpeedrunGameInfo extends TimerModule {
    private static final float SCALE_FACTOR = 0.9f;

    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        int color = run.getConfig().getPrefixColor();
        drawScaledCenteredString(renderer, Run.GAME_NAME, x + TimerRenderer.MODULE_WIDTH / 2, y, color);
        drawScaledCenteredString(renderer, run.getHumanMapName() + " - " + Run.CATEGORY, x + TimerRenderer.MODULE_WIDTH / 2, y + 9, color);
    }

    private void drawScaledCenteredString(RenderHelper renderHelper, String text, int x, int y, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 1);
        GL11.glScalef(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        renderHelper.drawCenteredString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    @Override
    public int getHeight() {
        return (int) (9 * SCALE_FACTOR * 2) + 8;
    }
}
