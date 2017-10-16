package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.CAI;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiCAI;

public class CAIListener extends AbstractGameListener<CAI> {

	@Override
	public Class<CAI> getGameMode() {
		return CAI.class;
	}

	@Override
	public boolean matchLobby(String arg0) {
		return arg0.equals("CAI");
	}

	@Override
	public void onGameModeJoin(CAI gameMode) {

		gameMode.setState(GameState.STARTING);
		ActiveGame.set("CAI");
		IHive.genericJoin();
		
		new Thread(new Runnable(){

			@Override
			public void run(){
				try {

					Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
					The5zigAPI.getLogger().info(sb.getTitle());
					
					if(sb != null && sb.getTitle().contains("Your CAI Stats")){
						int points = sb.getLines().get(ChatColor.AQUA + "Points");
						APIValues.CAIpoints = (long) points;
					}

					CAI.rank = CAIRank.getFromDisplay(new ApiCAI(The5zigAPI.getAPI().getGameProfile().getName()).getTitle()).getTotalDisplay();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	
	}



	@Override
	public boolean onServerChat(CAI gameMode, String message) {

		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("CAI Color Debug: (" + message + ")");
		}
		return false;
	}

	@Override
	public void onTitle(CAI gameMode, String title, String subTitle) {
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("CAI TitleColor Debug: (" +

					title != null ? title : "ERR_TITLE_NULL"

						+ " *§* " +


					subTitle != null ? subTitle : "ERR_SUBTITLE_NULL"

						+ ")"
					);
		}
	}

	@Override
	public void onServerConnect(CAI gameMode) {
		CAI.reset(gameMode);
	}

}
