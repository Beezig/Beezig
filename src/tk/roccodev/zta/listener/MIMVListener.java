package tk.roccodev.zta.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import tk.roccodev.zta.hiveapi.stuff.mimv.MIMVRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiMIMV;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

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

					ApiMIMV api = new ApiMIMV(The5zigAPI.getAPI().getGameProfile().getName());
					
					if (sb != null && sb.getTitle().contains("Your MIMV Stats")) {
						int points = sb.getLines().get(ChatColor.AQUA + "Karma");
						APIValues.MIMVpoints = (long) points;
						
						MIMV.rankObject = MIMVRank.getFromDisplay(api.getTitle());
						MIMV.rank = MIMV.rankObject.getTotalDisplay();
						
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
		if(message.startsWith("§8▍ §c§lMurder§8 ▏ §3Voting has ended! §bThe map §f")) {
			The5zigAPI.getLogger().info("Voting ended, parsing map");
			String afterMsg = message.split("§8▍ §c§lMurder§8 ▏ §3Voting has ended! §bThe map ")[1];
			String map = "";
			Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
				map = matcher.group(1);
			}

			MIMV.map = map;

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
		if(subTitle.contains("You will be") && subTitle.contains("this round!")) {
			String role = APIUtils.capitalize(title.replace("§r", "").toLowerCase().trim());
			MIMV.role = role.startsWith("§a") ? "§aCitizen" : (role.startsWith("§c") ? "§cMurderer": "§9Detective");
			DiscordUtils.updatePresence("Investigating in Murder in Mineville", "Playing on " + MIMV.map, "game_mimv");
		}
	}

	@Override
	public void onServerConnect(MIMV gameMode) {
		MIMV.reset(gameMode);
	}

}
