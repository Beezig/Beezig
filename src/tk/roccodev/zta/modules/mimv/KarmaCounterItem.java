package tk.roccodev.zta.modules.mimv;

import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.zta.games.MIMV;

public class KarmaCounterItem extends GameModeItem<MIMV> {

	public KarmaCounterItem() {
		super(MIMV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return MIMV.gamePts;
	}

	@Override
	public String getName() {
		return "Game";
	}

	@Override
	public boolean shouldRender(boolean dummy) {

		try {
			if (!(getGameMode() instanceof MIMV))
				return false;
			return dummy || (MIMV.shouldRender(getGameMode().getState()) && MIMV.gamePts != 0);
		} catch (Exception e) {
			return false;
		}
	}

}
