package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.render.RenderHelper;

public class SpeedrunComparison extends TimerModule {
    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        renderer.drawString(Message.translate("speedrun.setting.comparison"), x, y, run.getConfig().getPrefixColor());
        String display = run.getComparison().translate();
        renderer.drawString(display, x + TimerRenderer.MODULE_WIDTH - renderer.getStringWidth(display), y, run.getConfig().getDefaultColor());
    }

    @Override
    public int getHeight() {
        return 9;
    }
}
