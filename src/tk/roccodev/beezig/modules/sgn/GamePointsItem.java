package tk.roccodev.beezig.modules.sgn;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.games.SGN;

public class GamePointsItem extends GameModeItem<SGN> {

	public GamePointsItem() {
		super(SGN.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try {
			return SGN.gamePts;

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
			if (!(getGameMode() instanceof SGN))
				return false;
			if (SGN.gamePts == 0)
				return false;
			return dummy || (SGN.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
