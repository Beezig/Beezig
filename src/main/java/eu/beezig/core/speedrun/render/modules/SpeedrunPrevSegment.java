package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.render.RenderHelper;
import livesplitcore.PreviousSegmentComponentState;

public class SpeedrunPrevSegment extends TimerModule {
    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        PreviousSegmentComponentState state = run.getPreviousSegmentState();
        if(state == null) return;
        renderer.drawString(Message.translate("speedrun.module.previous_segment"), x, y, run.getConfig().getPrefixColor());
        String display = state.time();
        renderer.drawString(display, x + TimerRenderer.MODULE_WIDTH - renderer.getStringWidth(display), y, run.getConfig().getColor(state.semanticColor()));
    }

    @Override
    public int getHeight() {
        return 9;
    }
}