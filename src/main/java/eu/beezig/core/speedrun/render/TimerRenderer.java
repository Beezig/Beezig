package eu.beezig.core.speedrun.render;

import eu.beezig.core.speedrun.Run;
import eu.the5zig.mod.render.RenderHelper;
import org.lwjgl.opengl.GL11;

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
        if(run == null) return;
        float scale = run.getConfig() == null ? 1f : run.getConfig().getScale() / 100f;
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 1);
        GL11.glScalef(scale, scale, scale);
        renderHelper.drawRect(0, 0, MODULE_WIDTH, getUnscaledHeight(), run.getConfig().getBackgroundColor());
        int mutY = 0;
        synchronized (run.getStateLock()) {
            for (TimerModule module : activeModules) {
                module.render(renderHelper, run, 0, mutY);
                mutY += module.getHeight();
            }
        }
        GL11.glPopMatrix();
    }

    private int getUnscaledHeight() {
        return activeModules.stream().mapToInt(TimerModule::getHeight).sum();
    }

    public int getTotalHeight() {
        float scale = run == null || run.getConfig() == null ? 1f : run.getConfig().getScale() / 100f;
        return (int) (getUnscaledHeight() * scale);
    }
}
