package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.BED;

public class DailyItem extends GameModeItem<BED> {

	public DailyItem() {
		super(BED.class);
	}

	@Override
	protected Object getValue(boolean dummy) {

		return BED.dailyPoints + " Points";

	}

	@Override
	public String getName() {
		return "Daily";
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof BED))
				return false;
			return dummy || (BED.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
