package tk.roccodev.zta.modules.dr;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.DR;

public class DailyItem extends GameModeItem<DR> {

	public DailyItem() {
		super(DR.class);
	}

	@Override
	protected Object getValue(boolean dummy) {

		return DR.dailyPoints + " Points";

	}

	@Override
	public String getName() {
		return "Daily";
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof DR))
				return false;
			return dummy || (DR.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
