package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.the5zig.mod.render.RenderHelper;

public class SpeedrunGameInfo extends TimerModule {
    private static final float SCALE_FACTOR = 0.8f;

    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        renderer.drawScaledCenteredString(Run.GAME_NAME, x + TimerRenderer.MODULE_WIDTH / 2, y, SCALE_FACTOR);
        renderer.drawScaledCenteredString(Run.CATEGORY, x + TimerRenderer.MODULE_WIDTH / 2, y + 9, SCALE_FACTOR);
    }

    @Override
    public int getHeight() {
        return 9 * 2;
    }
}
