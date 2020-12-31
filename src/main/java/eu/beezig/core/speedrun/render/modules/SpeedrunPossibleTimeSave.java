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

package eu.beezig.core.speedrun.render.modules;

import eu.beezig.core.speedrun.Run;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.TimerRenderer;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.render.RenderHelper;
import livesplitcore.PossibleTimeSaveComponentState;

public class SpeedrunPossibleTimeSave extends TimerModule {
    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        PossibleTimeSaveComponentState state = run.getPossibleTimeSaveState();
        if(state == null) return;
        renderer.drawString(Message.translate("speedrun.module.possible_time_save"), x, y, run.getConfig().getPrefixColor());
        String display = state.time();
        renderer.drawString(display, x + TimerRenderer.MODULE_WIDTH - renderer.getStringWidth(display), y, run.getConfig().getDefaultColor());
    }

    @Override
    public int getHeight() {
        return 9;
    }
}
