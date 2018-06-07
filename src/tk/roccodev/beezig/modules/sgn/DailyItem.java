package tk.roccodev.beezig.modules.sgn;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.SGN;

public class DailyItem extends GameModeItem<SGN> {

	public DailyItem() {
		super(SGN.class);
	}

	@Override
	protected Object getValue(boolean dummy) {

		return SGN.dailyPoints + " Points";

	}

	@Override
	public String getName() {
		return "Daily";
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof SGN))
				return false;
			return dummy || (SGN.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
