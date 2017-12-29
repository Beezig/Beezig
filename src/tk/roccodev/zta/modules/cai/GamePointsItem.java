package tk.roccodev.zta.modules.cai;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.CAI;

public class GamePointsItem extends GameModeItem<CAI> {

	public GamePointsItem() {
		super(CAI.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try {
			return CAI.gamePoints;

		} catch (Exception e) {
			e.printStackTrace();
			return "Server error";
		}
	}

	@Override
	public String getName() {
		return "Game";
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof CAI))
				return false;
			if (CAI.gamePoints == 0)
				return false;
			return dummy || (CAI.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
