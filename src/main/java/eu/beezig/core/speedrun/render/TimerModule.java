package eu.beezig.core.speedrun.render;

import eu.beezig.core.speedrun.Run;
import eu.the5zig.mod.render.RenderHelper;

public abstract class TimerModule {
    public abstract void render(RenderHelper renderer, Run run, int x, int y);
    public abstract int getHeight();
}
