package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.the5zig.mod.render.RenderHelper;
import livesplitcore.PossibleTimeSaveComponentState;

public class SpeedrunPossibleTimeSave extends TimerModule {
    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        PossibleTimeSaveComponentState state = run.getPossibleTimeSaveState();
        if(state == null) return;
        renderer.drawString("Possible Time Save", x, y);
        String display = state.time();
        renderer.drawString(display, x + TimerRenderer.MODULE_WIDTH - renderer.getStringWidth(display), y);
    }

    @Override
    public int getHeight() {
        return 9;
    }
}
