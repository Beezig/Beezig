package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.MIMV;
import tk.roccodev.zta.hiveapi.APIValues;

public class MIMVListener extends AbstractGameListener<MIMV> {

	@Override
	public Class<MIMV> getGameMode() {
		return MIMV.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("MIMV");
	}

	@Override
	public void onGameModeJoin(MIMV gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("MIMV");
		IHive.genericJoin();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());

					if (sb != null && sb.getTitle().contains("Your MIMV Stats")) {
						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						APIValues.MIMVpoints = (long) points;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	@Override
	public boolean onServerChat(MIMV gameMode, String message) {

		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("MIMV Color Debug: (" + message + ")");
		}

		
		

		return false;

	}

	@Override
	public void onTitle(MIMV gameMode, String title, String subTitle) {
		if (ZTAMain.isColorDebug) {
			The5zigAPI.getLogger().info("MIMV TitleColor Debug: (" +

					title != null ? title
							: "ERR_TITLE_NULL"

									+ " *§* " +

									subTitle != null ? subTitle
											: "ERR_SUBTITLE_NULL"

													+ ")");
		}
	}

	@Override
	public void onServerConnect(MIMV gameMode) {
	//	MIMV.reset(gameMode);
	}

}
