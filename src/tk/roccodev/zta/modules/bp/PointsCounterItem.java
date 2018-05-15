package tk.roccodev.zta.modules.bp;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.BP;

public class PointsCounterItem extends GameModeItem<BP> {

	public PointsCounterItem() {
		super(BP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return BP.gamePts;
	}

	@Override
	public String getName() {
		return "Game";
	}

	@Override
	public boolean shouldRender(boolean dummy) {

		try {
			if (!(getGameMode() instanceof BP))
				return false;
			return dummy || (BP.shouldRender(getGameMode().getState()) && BP.gamePts != 0);
		} catch (Exception e) {
			return false;
		}
	}

}
