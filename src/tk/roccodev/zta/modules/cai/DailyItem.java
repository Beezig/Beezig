package tk.roccodev.zta.modules.cai;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.BP;
import tk.roccodev.zta.games.CAI;

public class DailyItem extends GameModeItem<CAI> {

	public DailyItem() {
		super(CAI.class);
	}

	@Override
	protected Object getValue(boolean dummy) {

		return CAI.dailyPoints + " Points";

	}

	@Override
	public String getName() {
		return "Daily";
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof CAI))
				return false;
			return dummy || (CAI.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
