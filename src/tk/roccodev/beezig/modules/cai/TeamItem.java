package tk.roccodev.beezig.modules.cai;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.games.CAI;

public class TeamItem extends GameModeItem<CAI> {

	public TeamItem() {
		super(CAI.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		try {
			
			boolean color = (boolean) getProperties().getSetting("color").get();
			
			return color ? CAI.team : ChatColor.stripColor(CAI.team);

		} catch (Exception e) {
			e.printStackTrace();
			return "Server error";
		}
	}

	@Override
	public String getName() {
		return "Team";
	}

	
	
	@Override
	public void registerSettings() {
		getProperties().addSetting("color", true);
		super.registerSettings();
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		try {
			if (!(getGameMode() instanceof CAI))
				return false;
			if (CAI.team == null || CAI.team.isEmpty())
				return false;
			return dummy || (CAI.shouldRender(getGameMode().getState()));
		} catch (Exception e) {
			return false;
		}
	}

}
