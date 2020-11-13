package eu.beezig.core.modules.items.dr;

import eu.beezig.core.Beezig;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;

public class ModuleSpeedrunTimer extends GameModeItem<DR> {
    public ModuleSpeedrunTimer() {
        super(DR.class);
    }

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        DR mode = getGameMode();
        if(mode == null || mode.getCurrentRun() == null) return;
        TimerRenderer renderer = mode.getCurrentRun().getRenderer();
        renderer.render(Beezig.api().getRenderHelper(), x, y);
    }

    @Override
    public int getHeight(boolean dummy) {
        DR mode = getGameMode();
        if(mode == null || mode.getCurrentRun() == null) return 0;
        TimerRenderer renderer = mode.getCurrentRun().getRenderer();
        return renderer.getTotalHeight();
    }

    @Override
    protected Object getValue(boolean b) {
        return false;
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if(dummy) return false;
        if(!super.shouldRender(false)) return false;
        DR mode = getGameMode();
        if(mode == null) return false;
        mode.checkForNatives();
        return mode.getCurrentRun() != null;
    }
}
