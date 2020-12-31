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

package eu.beezig.core.modules.items.dr;

import eu.beezig.core.Beezig;
import eu.beezig.core.modules.ICustomRender;
import eu.beezig.core.modules.Modules;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;

public class ModuleSpeedrunTimer extends GameModeItem<DR> implements ICustomRender {
    public ModuleSpeedrunTimer() {
        super(DR.class);
    }

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        if(dummy) return;
        DR mode = getGameMode();
        if(mode == null || mode.getCurrentRun() == null) return;
        TimerRenderer renderer = mode.getCurrentRun().getRenderer();
        renderer.render(Beezig.api().getRenderHelper(), x, y);
    }

    @Override
    public int getHeight(boolean dummy) {
        if(dummy) return 0;
        DR mode = getGameMode();
        if (mode == null || mode.getCurrentRun() == null) return 0;
        TimerRenderer renderer = mode.getCurrentRun().getRenderer();
        return renderer.getTotalHeight();
    }

    @Override
    public String getTranslation() {
        return "Speedrun";
    }

    @Override
    protected Object getValue(boolean b) {
        return false;
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if(dummy || !Modules.render()) return false;
        if(!super.shouldRender(false)) return false;
        DR mode = getGameMode();
        if(mode == null) return false;
        mode.checkForNatives();
        return mode.getCurrentRun() != null;
    }
}
