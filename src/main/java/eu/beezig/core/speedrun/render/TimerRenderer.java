package eu.beezig.core.speedrun.render;

import eu.beezig.core.speedrun.Run;
import eu.the5zig.mod.render.RenderHelper;

import java.util.List;

public class TimerRenderer {
    public static final int MODULE_WIDTH = 150;
    private final List<? extends TimerModule> activeModules;
    private final Run run;

    public TimerRenderer(Run run, List<? extends TimerModule> activeModules) {
        this.activeModules = activeModules;
        this.run = run;
    }

    public void render(RenderHelper renderHelper, int x, int y) {
        renderHelper.drawRect(x, y, x + MODULE_WIDTH, y + getTotalHeight(), 0xFF000000);
        int mutY = y;
        for(TimerModule module : activeModules) {
            module.render(renderHelper, run, x, mutY);
            mutY += module.getHeight();
        }
    }

    public int getTotalHeight() {
        return activeModules.stream().mapToInt(TimerModule::getHeight).sum();
    }
}
