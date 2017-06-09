package tk.roccodev.zta.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.DRMap;
import tk.roccodev.zta.hiveapi.DRRank;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.settings.Setting;

public class DRListener extends AbstractGameListener<DR>{

	@Override
	public Class<DR> getGameMode() {
		// TODO Auto-generated method stub
		return DR.class;
	}

	@Override
	public boolean matchLobby(String lobby) {		
		return lobby.equals("DR");		
	}

	@Override
	public void onGameModeJoin(DR gameMode){
		gameMode.setState(GameState.STARTING);
		ZTAMain.isDR = true;
		Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
		if(sb != null) The5zigAPI.getLogger().info(sb.getTitle());
		if(sb != null && sb.getTitle().contains("Your DR Stats")){
			int points = sb.getLines().get(ChatColor.AQUA + "Points");	
			HiveAPI.DRpoints = (long) points;		
		}else{
		try {
			//HiveAPI.updateKarma();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
	}
	
	@Override
	public boolean onServerChat(DR gameMode, String message) {
		// Uncomment this to see the real messages with chatcolor. vv
		// The5zigAPI.getLogger().info("COLOR = (" + message + ")");
		
		if(message.startsWith("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map") && gameMode != null){
			String afterMsg = message.split("§8▍ §cDeathRun§8 ▏ §3Voting has ended! §bThe map")[1];
			The5zigAPI.getLogger().info(afterMsg);
			String map = "";
		    
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    DRMap map1 = DRMap.getFromDisplay(map);	    
		    DR.activeMap = map1;			
		}
		
		else if(message.contains(ChatColor.stripColor("You are a ")) && gameMode != null){
			String afterMsg = message.split(ChatColor.stripColor("You are a "))[1];	
			switch(afterMsg){
				case "DEATH!": DR.role = "Death";
					break;
				case "RUNNER!": DR.role = "Runner";
						if(DR.activeMap != null){
							The5zigAPI.getLogger().info("Loading PB...");
							DR.currentMapPB = HiveAPI.DRgetPB(The5zigAPI.getAPI().getGameProfile().getName(), DR.activeMap);
							The5zigAPI.getLogger().info("Loading WR...");
							DR.currentMapWR = HiveAPI.DRgetWR(DR.activeMap);
						}
					break;
			}
		}
		else if(message.startsWith("§8▍ §eTokens§8 ▏ §7You earned §f") && ZTAMain.isDR && DR.role == "Runner") {
			// I don't care about double tokens weekends Rocco :^)
			if(!(DR.checkpoints == DR.activeMap.getCheckpoints())){
					DR.checkpoints++;
				}
			}
		else if(message.equals("§8▍ §cDeathRun§8 ▏ §cYou have been returned to your last checkpoint!") && ZTAMain.isDR && DR.role == "Runner") {
				DR.deaths++;	
			}
		else if(message.contains("§6 (") && message.contains("§6)") && message.contains(The5zigAPI.getAPI().getGameProfile().getName()) && ZTAMain.isDR && DR.role == "Death") {
				DR.kills++;	
			}
		
		else if(!message.contains("§6§6") && message.contains(" Stats §6§m") && (Setting.SHOW_NETWORK_RANK_COLOR.getValue() || Setting.SHOW_NETWORK_RANK_TITLE.getValue())){
			Thread t = new Thread(new Runnable(){
				@Override
				public void run(){
					String correctName = HiveAPI.getName(DR.lastRecords);
					if(correctName.contains("nicked player")){
						correctName = "Nicked/Not found";
					}
				 	String rank = HiveAPI.getNetworkRank(DR.lastRecords);
				 	if(rank.contains("nicked player")){
						rank = "Nicked/Not found";
					}
				 	ChatColor rankColor = HiveAPI.getRankColor(rank);
			 	
				 	//"          §6§m                  §f ItsNiklass's Stats §6§m                  "
				 	// Not sure if this the best way to do this v
				 	if(Setting.SHOW_NETWORK_RANK_COLOR.getValue() && Setting.SHOW_NETWORK_RANK_TITLE.getValue()){
				 		The5zigAPI.getAPI().messagePlayer(ChatColor.BLACK + "" + ChatColor.GOLD + "          " + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + " " + rankColor + correctName + ChatColor.RESET + "'s Stats " + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "                  ");
				 		The5zigAPI.getAPI().messagePlayer(ChatColor.BLACK + "" + ChatColor.GOLD + "          " + ChatColor.STRIKETHROUGH + "       " + ChatColor.RESET + ChatColor.GOLD + " (" + rankColor + rank + ChatColor.GOLD + ") " +  ChatColor.STRIKETHROUGH + "       ");
				 	}
				 	else if(Setting.SHOW_NETWORK_RANK_COLOR.getValue() && !Setting.SHOW_NETWORK_RANK_TITLE.getValue()){
				 		The5zigAPI.getAPI().messagePlayer(ChatColor.BLACK + "" + ChatColor.GOLD + "          " + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + " " + rankColor + correctName + ChatColor.RESET + "'s Stats " + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "                  ");
					}
				 	else if(!Setting.SHOW_NETWORK_RANK_COLOR.getValue() && Setting.SHOW_NETWORK_RANK_TITLE.getValue()){
				 		The5zigAPI.getAPI().messagePlayer(ChatColor.BLACK + "" + ChatColor.GOLD + "          " + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + " " + correctName + "'s Stats " + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "                  ");
				 		The5zigAPI.getAPI().messagePlayer(ChatColor.BLACK + "" + ChatColor.GOLD + "          " + ChatColor.STRIKETHROUGH + "       " + ChatColor.RESET + ChatColor.GOLD + " (" + ChatColor.RESET + rank + ChatColor.GOLD + ") " +  ChatColor.STRIKETHROUGH + "       ");
				 	}
				}
			});
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
		
		else if(message.contains(ChatColor.DARK_AQUA + " Points:") && !Setting.DR_SHOW_RANK.getValue()){
			String[] contents = message.split(":");
			long points = Long.valueOf(ChatColor.stripColor(contents[1].trim()));
			DR.lastRecordPoints = points;
		}

		else if(message.startsWith(ChatColor.DARK_AQUA + " Points:") && Setting.DR_SHOW_RANK.getValue()){
			String[] contents = message.split(":");
			String points1 = ChatColor.stripColor(contents[1].trim());
			int points = Integer.valueOf(points1);
			DR.lastRecordPoints = points;
			String title1 = HiveAPI.DRgetRank(DR.lastRecords);
			DRRank rank = DRRank.getFromDisplay(title1);
			String title = rank.getTotalDisplay();
			The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Points: " + ChatColor.AQUA + points + " (" + title +  ChatColor.AQUA + ")");
			
			return true;
		}
		else if(message.contains(ChatColor.DARK_AQUA + " Wins:")){
			
			//Better /records
			
			Runnable rnn = new Runnable(){
				@Override
				public void run(){
			try{
			
				The5zigAPI.getLogger().info("Running Better Records...");
				if(Setting.DR_SHOW_POINTSPERGAME.getValue()){
					double ppg = Math.round(((double)DR.lastRecordPoints / (double)HiveAPI.DRgetGames(DR.lastRecords)) * 10d) / 10d;
					The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Points/Game: " + ChatColor.AQUA + ppg);
				}
				if(Setting.DR_SHOW_RUNNERWINRATE.getValue()){
					int rwr = (int) (Math.floor(((double)HiveAPI.DRgetRunnerWins(DR.lastRecords) / (double)HiveAPI.DRgetRunnerGamesPlayed(DR.lastRecords)) * 1000d) / 10d);
					The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Winrate (Runner): " + ChatColor.AQUA + rwr + "%");
				}
				if(Setting.DR_SHOW_DEATHSPERGAME.getValue()){
					double dpg = (double) (Math.floor(((double)HiveAPI.DRgetDeaths(DR.lastRecords) / (double)HiveAPI.DRgetRunnerGamesPlayed(DR.lastRecords)) * 10d) / 10d);
					The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Deaths per Game: " + ChatColor.AQUA + dpg);
				}
				if(Setting.DR_SHOW_ACHIEVEMENTS.getValue()){
					int ach = HiveAPI.DRgetAchievements(DR.lastRecords);
					The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Achievements: " + ChatColor.AQUA + ach + "/35");
				}
				if(Setting.SHOW_RECORDS_LASTGAME.getValue()){
					The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Last Game: " + ChatColor.AQUA + HiveAPI.lastGame(DR.lastRecords, "DR"));
				}
			}
			catch(Exception e){
				e.printStackTrace();
				}
			}
			};
			
			ExecutorService exec = Executors.newCachedThreadPool();
			exec.submit(rnn);
			exec.shutdown();
			try {
				exec.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			}
			else if(message.contains(ChatColor.stripColor("You are a "))){
				gameMode.setState(GameState.GAME);
		 }
		return false;
	}

	@Override
	public void onServerConnect(DR gameMode) {
		The5zigAPI.getLogger().info("Resetting! (DR)");
		DR.reset(gameMode);
	}

}
