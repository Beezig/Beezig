package tk.roccodev.zta.modules.grav;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.games.GRAV;
import tk.roccodev.zta.games.HIDE;

public class StagesItem extends GameModeItem<GRAV> {

	public StagesItem() {
		super(GRAV.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try {
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "No Map";
		}
	}

	@Override
	public String getName() {
		return "Stages";
	}

	@Override
	public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
		int lineCount = 0;
		The5zigAPI.getAPI().getRenderHelper().drawString(getPrefix(), x, y);
		lineCount++;

		for (String v : GRAV.toDisplayWithFails.values()) {
			The5zigAPI.getAPI().getRenderHelper().drawString(v.replace("{f}", "0"), x, y + lineCount * 10);
			lineCount++;
		}

	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("grav")
					&& GRAV.toDisplay.size() != 0);
		} catch (Exception e) {
			return false;
		}
	}

}
