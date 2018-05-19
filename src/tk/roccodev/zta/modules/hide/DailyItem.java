package tk.roccodev.zta.modules.hide;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.HIDE;

public class DailyItem extends GameModeItem<HIDE> {

	public DailyItem() {
		super(HIDE.class);
	}

	@Override
	protected Object getValue(boolean dummy) {

		return HIDE.dailyPoints + " Points";

	}

	@Override
	public String getName() {
		return "Daily";
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof HIDE))
				return false;
			return dummy || (HIDE.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
