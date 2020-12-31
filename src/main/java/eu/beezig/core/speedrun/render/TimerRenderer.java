/*
 * Copyright (C) 2017-2021 Beezig Team
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

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
