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
                renderer.drawRect(x, y, x + TimerRenderer.MODULE_WIDTH, y + 10, run.getColorConfig().getCurrentSegment());
            }
            renderer.drawString(state.name(i), x, y);
            for(int column = 0; column < columns; column++) {
                String columnValue = state.columnValue(i, column);
                int color = run.getColorConfig().getColor(state.columnSemanticColor(i, column));
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
