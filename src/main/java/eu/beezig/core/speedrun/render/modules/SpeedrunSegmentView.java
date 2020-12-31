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
import eu.the5zig.mod.render.RenderHelper;
import livesplitcore.SplitComponentBridge;
import livesplitcore.SplitsComponentState;

public class SpeedrunSegmentView extends TimerModule {
    private final static int PADDING = 10;
    private int height;

    @Override
    public void render(RenderHelper renderer, Run run, int x, int y) {
        SplitsComponentState state = run.getSplitsState();
        if(state == null) return;
        long rows = state.len();
        height = 0;
        for(long i = 0; i < rows; i++) {
            long columns = SplitComponentBridge.getColumnsLength(state, i);
            int knownWidth = 0;
            if(state.isCurrentSplit(i)) {
                renderer.drawRect(x, y, x + TimerRenderer.MODULE_WIDTH, y + 10, run.getConfig().getCurrentSegmentColor());
            }
            String name = state.name(i);
            if(name.isEmpty()) continue;
            renderer.drawString(name, x, y, run.getConfig().getPrefixColor());
            for(int column = 0; column < columns; column++) {
                String columnValue = state.columnValue(i, column);
                int color = run.getConfig().getColor(state.columnSemanticColor(i, column));
                int width = renderer.getStringWidth(columnValue);
                renderer.drawString(columnValue, x + TimerRenderer.MODULE_WIDTH - (knownWidth += width) - PADDING * column, y, color);
            }
            y += 10;
            height += 10;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
