
package tk.roccodev.zta.listener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.autovote.watisdis;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.TIMVMap;
import tk.roccodev.zta.hiveapi.TIMVRank;
import tk.roccodev.zta.settings.Setting;

public class TIMVListener extends AbstractGameListener<TIMV>{

	@Override
	public Class<TIMV> getGameMode() {
		// TODO Auto-generated method stub
		return TIMV.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		
		return lobby.equals("TIMV");
		
	}
	
	
	
	@Override
	public void onGameModeJoin(TIMV gameMode){
		gameMode.setState(GameState.STARTING);
		ActiveGame.set("TIMV");
		Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
		TIMV.rank = TIMVRank.getFromDisplay(HiveAPI.TIMVgetRank(The5zigAPI.getAPI().getGameProfile().getName())).getDisplay();
		//Freeze? No colors because closing bracket cannot be colored correctly. The5zigAPI.getAPI().getGameProfile().getModulesColor() would be a solution ?
		if(sb != null) The5zigAPI.getLogger().info(sb.getTitle());
		if(sb != null && sb.getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Your TIMV Stats")){
			
			int karma = sb.getLines().get(ChatColor.AQUA + "Karma");
			if(karma != 0)
				HiveAPI.TIMVkarma = (long) karma;
			
			
		}else{
		
			
			
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						Thread.sleep(200); // Wait for server resources to load 
						HiveAPI.TIMVupdateKarma();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			
			
		
		
		}
		
		
	}
	

	@Override
	public boolean onServerChat(TIMV gameMode, String message) {
		// Uncomment this to see the real messages with chatcolor. vv
		// The5zigAPI.getLogger().info("(" + message + ")");
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("ColorDebug: " + "(" + message + ")");
		}
		if(message.equals("§8▍ §3TIMV§8 ▏ §6Welcome to Trouble in Mineville!")){
			gameMode.setState(GameState.STARTING);
			ActiveGame.set("TIMV");
			The5zigAPI.getLogger().info("DEBUG = Joined TIMV");
			Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
			
			if(sb != null && sb.getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Your TIMV Stats")){
				
				int karma = sb.getLines().get(ChatColor.AQUA + "Karma");
				if(karma != 0)
				HiveAPI.TIMVkarma = (long) karma;
				
				
			}else{
			
				Runnable rnn = new Runnable(){
					
					@Override
					public void run(){
						try {
							Thread.sleep(200); // Wait for server resources to load
							HiveAPI.TIMVupdateKarma();
						} catch (Exception e) {
							// TODO Auto-generated catch block
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
			
		}
		else if(message.contains("§cLost §e20§c karma") && gameMode != null){
			TIMV.minus20();
			if(TIMV.role.equals("Detective")){
				TIMV.applyPoints(-1, "i");
			}
			else{
				TIMV.applyPoints(-1);
			}
	
		}
		else if(message.contains("§cLost §e40§c karma") && gameMode != null){
			TIMV.minus40();
			if(TIMV.role.equals("Traitor")){
				TIMV.applyPoints(-2, "t");
			}else{
			TIMV.applyPoints(-2, "d");
			}
		}
		else if(message.contains("§aAwarded §e20§a karma") && gameMode != null){
			TIMV.plus20();
			if(TIMV.role.equals("Traitor")){
			TIMV.applyPoints(2);
			}else{
				TIMV.applyPoints(1);
			}
		}	
		

		else if(message.contains("§aAwarded §e10§a karma") && gameMode != null){
			TIMV.plus10();
			TIMV.applyPoints(1);
		}
		else if(message.contains("§aAwarded §e25§a karma") && gameMode != null){
			TIMV.plus25();
			TIMV.applyPoints(2); //+1 Det point
			
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6The game has ended.") && gameMode != null){
			//Where does 'Game Over' appear?
			if(!TIMV.dead){
				TIMV.applyPoints(20);
			}
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						TimeUnit.SECONDS.sleep(5);
						The5zigAPI.getAPI().messagePlayer(Log.info + "§6TIMV GameID: §c" + ChatColor.stripColor(TIMV.gameID) + " §6 > §chttp://hivemc.com/trouble-in-mineville/game/" + ChatColor.stripColor(TIMV.gameID) );			
						TIMV.reset(gameMode);
					} catch (Exception e) {
						e.printStackTrace();
					}				
				}
			}).start();
			
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6Voting has ended! The map") && gameMode != null){
			String afterMsg = message.split("§8▍ §3TIMV§8 ▏ §6Voting has ended! The map")[1];
			The5zigAPI.getLogger().info(afterMsg);
		// §bSky Lands§6
		// 
			String map = "";
		    
		    Pattern pattern = Pattern.compile(Pattern.quote("§b") + "(.*?)" + Pattern.quote("§6"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    The5zigAPI.getLogger().info(map);
		    TIMVMap map1 = TIMVMap.getFromDisplay(map);
		    
		    TIMV.activeMap = map1;
			
		}
		//Map Fallback (Joined after voting ended.)
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6Map :") && gameMode != null && TIMV.activeMap == null){
			String afterMsg = message.split("§8▍ §3TIMV§8 ▏ §6Map : §b")[1];
			// §8▍ §3TIMV§8 ▏ §6Map : §bCastle
			The5zigAPI.getLogger().info("FALLBACK: " + afterMsg);
			String map = "";
			
			// TODO I don't understand this regex pattern thing; please remove the unnecessary parts or make this nice v
			Pattern pattern = Pattern.compile(afterMsg);
			Matcher matcher = pattern.matcher(afterMsg);
			while (matcher.find()) {
			   map = matcher.group(0);
			}
			The5zigAPI.getLogger().info("FALLBACK: " + map);
			TIMVMap map1 = TIMVMap.getFromDisplay(map);
			    
			TIMV.activeMap = map1;
	
		}
		else if(message.equals("§7==============§aTIMV Stats§7==============")){
			//Advanced Records
			if(message.endsWith("AR§e§l")){
				The5zigAPI.getAPI().messagePlayer(message.replaceAll("AR§e§l", ""));
				return true;
			}
				TIMV.messagesToSend.add(message);
			
			return true;
		}
		else if(message.equals("§bThis data is §elive data.") && !message.endsWith(" ")){
			
				TIMV.footerToSend.add(message);
			
			return true;
		}
		else if(message.startsWith(ChatColor.AQUA + "") && !message.endsWith(" ")){
				if(message.startsWith("§bAs you're an experienced player, we're") || 
					message.startsWith("§bConstable ") || 
					message.startsWith("§bTracer ") ||
					message.startsWith("§bDirector ")){
					//It was sucking in all the chat messages by people with this rank until one did /records lmao
						return false;
				}		
				TIMV.messagesToSend.add(message);
	
			return true;
			
			
		}
		
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6Vote received.")){
			TIMV.hasVoted = true;
		}
		
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6§6§l6.§f§6 §4Random map§6") && !TIMV.hasVoted){
			/*
			 * 
			 * Multi-threading to avoid lag on older machines
			 * 
			 */
			
			new Thread(new Runnable(){
				@Override
				public void run(){
					List<String> votesCopy = new ArrayList<String>();
					votesCopy.addAll(TIMV.votesToParse);
					List<String> mapNames = new ArrayList<String>();
					List<TIMVMap> parsedMaps = new ArrayList<TIMVMap>();
					for(String s1 : AutovoteUtils.getMapsForMode("timv")){
						TIMVMap map1 = TIMVMap.valueOf(s1);	
						if(map1 == null) continue;
						parsedMaps.add(map1);
						The5zigAPI.getLogger().info("Parsed " + map1);
					}	
					
					for(String s : votesCopy){
						
						String[] data = s.split("\\.");
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §3TIMV§8 ▏ §6§6§l", "").replaceAll("▍ TIMV ▏", "").trim();
						String toConsider = data[1];
						String[] data2 = ChatColor.stripColor(toConsider).split("\\(");
						String consider = data2[0].trim();
						TIMVMap map = TIMVMap.getFromDisplay(consider);
						if(map == null){
							The5zigAPI.getAPI().messagePlayer(Log.error + "Error while autovoting: map not found for " + consider);
							return;
						}
						The5zigAPI.getLogger().info("trying to match " + map);			
						if(parsedMaps.contains(map)){
							The5zigAPI.getAPI().sendPlayerMessage("/vote " + index);
							TIMV.votesToParse.clear();
							TIMV.hasVoted = true;
							The5zigAPI.getAPI().messagePlayer(Log.info + "Automatically voted for §6" + map.getDisplayName());
							return;
						}
						else{
							The5zigAPI.getLogger().info("no matches in parsedMaps (yet)");
						}
						if(index.equals("5")){
							The5zigAPI.getAPI().sendPlayerMessage("/vote 6");
							TIMV.votesToParse.clear();
							TIMV.hasVoted = true;
							The5zigAPI.getAPI().messagePlayer(Log.info + "Automatically voted for §4Random map");
							return;
						}
					}	
				}
			}).start();
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6§6§l") && !TIMV.hasVoted){
			TIMV.votesToParse.add(message);		
		}
		else if(message.equals("§7=====================================") && !message.endsWith(" ")){ //Bar
			if(TIMV.footerToSend.contains("§bThis data is §elive data.")){
				//Advanced Records - send
				new Thread(new Runnable(){
					@Override
					public void run(){
						TIMV.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try{
						TIMVRank rank = null;
						Long rolepoints = Setting.TIMV_SHOW_KRR.getValue() ? HiveAPI.TIMVgetRolepoints(TIMV.lastRecords) : null;
						if(rolepoints == null && Setting.TIMV_SHOW_TRAITORRATIO.getValue()){
							rolepoints = HiveAPI.TIMVgetRolepoints(TIMV.lastRecords);
						}
						Long mostPoints = Setting.TIMV_SHOW_MOSTPOINTS.getValue() ? HiveAPI.TIMVgetKarmaPerGame(TIMV.lastRecords) : null;
						String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? HiveAPI.getNetworkRank(TIMV.lastRecords) : "";
						ChatColor rankColor = null;
						if(Setting.SHOW_NETWORK_RANK_COLOR.getValue()){
							if(rankTitle.isEmpty()){
								rankColor = HiveAPI.getRankColorFromIgn(TIMV.lastRecords);
							}
							else{
								rankColor = HiveAPI.getRankColor(rankTitle);
							}
						}
						long karma = 0;
						long traitorPoints = 0;
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? HiveAPI.lastGame(TIMV.lastRecords, "TIMV") : null;
						Integer achievements = Setting.TIMV_SHOW_ACHIEVEMENTS.getValue() ? HiveAPI.TIMVgetAchievements(TIMV.lastRecords) : null;
						String rankTitleTIMV = Setting.TIMV_SHOW_RANK.getValue() ? HiveAPI.TIMVgetRank(TIMV.lastRecords) : null;
						int monthlyRank = (Setting.TIMV_SHOW_MONTHLYRANK.getValue() &&  HiveAPI.getLeaderboardsPlacePoints(349, "TIMV") < HiveAPI.TIMVgetKarma(TIMV.lastRecords))? HiveAPI.getMonthlyLeaderboardsRank(TIMV.lastRecords, "TIMV") : 0;
						if(rankTitleTIMV != null) rank = TIMVRank.getFromDisplay(rankTitleTIMV);
						List<String> messages = new ArrayList<String>();
						messages.addAll(TIMV.messagesToSend);
							Iterator<String> it = messages.iterator();
							for(String s : messages){
								
								
								
								if(s.startsWith("§bKarma: §e")){
									StringBuilder sb = new StringBuilder();
									sb.append("§bKarma: §e");
									karma = Long.parseLong(s.replaceAll("§bKarma: §e", ""));
									sb.append(karma);
									if(rank != null) sb.append(" (" + rank.getTotalDisplay() + "§e)");
									The5zigAPI.getAPI().messagePlayer(sb.toString().trim() + " ");
									continue;
								}
								else if(s.startsWith("§bUsername: §e")){
									StringBuilder sb = new StringBuilder();
									String username = ChatColor.stripColor(s.replaceAll("§bUsername: §e", ""));
									String correctUser = HiveAPI.getName(username);
									if(correctUser.contains("nicked player")) correctUser = "Nicked/Not found";
									sb.append("§bUsername: ");
									if(rankColor != null) {
										sb.append(rankColor + correctUser);
									}
									else{
										sb.append("§e" + correctUser);
									}
									if(rankTitle != null && rankTitle.contains("nicked player")) rankTitle = "Nicked/Not found";
									if(!rankTitle.isEmpty()){
										if(rankColor == null) rankColor = ChatColor.WHITE;
										sb.append("§e (" + rankColor + rankTitle + "§e)");
									}
									The5zigAPI.getAPI().messagePlayer(sb.toString().trim() + " ");
									continue;
								}
								else if(s.startsWith("§bTraitor Points: §e") && karma > 1000 && Setting.TIMV_SHOW_TRAITORRATIO.getValue()){
									String[] contents = s.split(":");
									traitorPoints = Integer.parseInt(s.replaceAll("§bTraitor Points: §e", "").trim());
									long rp = rolepoints;
									double tratio = Math.round(((double)traitorPoints / (double)rp) * 1000d) / 10d;
									ChatColor ratioColor = ChatColor.YELLOW;
									if(tratio >= 38.0){
										ratioColor= ChatColor.RED;
									}
									The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "Traitor Points: " + ChatColor.YELLOW + traitorPoints + " (" + ratioColor + tratio + "%" +  ChatColor.YELLOW + ") ");
									continue;
								}
								The5zigAPI.getAPI().messagePlayer(s + " ");
								
							}
						
						


						Double krr = Setting.TIMV_SHOW_KRR.getValue() ? (double)Math.round((double) karma / (double) rolepoints * 100D) / 100D : null;
						
							
						if(mostPoints != null){
							The5zigAPI.getAPI().messagePlayer("§bMost Points: §e" + mostPoints + " ");
						}
						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§bAchievements: §e" + achievements + "/41 ");
						}
						if(krr != null){
							The5zigAPI.getAPI().messagePlayer("§bKarma/Rolepoints: §e" + krr + " ");
						}
						if(monthlyRank != 0){					
							The5zigAPI.getAPI().messagePlayer("§bMonthly Leaderboards: §e#" + monthlyRank + " ");
						}
						if(lastGame != null){
							Calendar lastSeen = Calendar.getInstance();;
							lastSeen.setTimeInMillis(HiveAPI.lastGame(TIMV.lastRecords, "TIMV").getTime());
						
							The5zigAPI.getAPI().messagePlayer("§bLast Game: §e" + HiveAPI.getTimeAgo(lastSeen.getTimeInMillis()) + " ");
						}
						
							
							for(String s : TIMV.footerToSend){
								
								The5zigAPI.getAPI().messagePlayer(s + " ");
							}
						
						
						
						TIMV.messagesToSend.clear();
						TIMV.footerToSend.clear();
						TIMV.isRecordsRunning = false;
						The5zigAPI.getAPI().messagePlayer("§7===================================== ");
						
						}catch(Exception e){
							e.printStackTrace();
							if(e.getCause() instanceof FileNotFoundException){
								The5zigAPI.getAPI().messagePlayer(Log.error + "Player nicked or not found.");
								TIMV.messagesToSend.clear();
								TIMV.footerToSend.clear();
								TIMV.isRecordsRunning = false;
								return;
							}
							The5zigAPI.getAPI().messagePlayer(Log.error + "Oops, looks like something went wrong while fetching the records, so you will receive the normal one!");
							
							for(String s : TIMV.messagesToSend){
								The5zigAPI.getAPI().messagePlayer(s + " ");
							}
							for(String s : TIMV.footerToSend){
								The5zigAPI.getAPI().messagePlayer(s + " ");
							}
							The5zigAPI.getAPI().messagePlayer("§7===================================== ");
							TIMV.messagesToSend.clear();
							TIMV.footerToSend.clear();
							TIMV.isRecordsRunning = false;
							return;
						}
					}
				}).start();
				return true;
				
				
			}
			else{
				
					TIMV.footerToSend.add(message);
				
				return true;
			}
			
			
		}
		
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6The body of §4")){
			TIMV.traitorsDiscovered++;
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6The body of §1")){
			TIMV.detectivesDiscovered++;
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §6You are now in the spectator lobby")){
			TIMV.dead = true;
			
		}
		else if(message.startsWith("§8▍ §3TIMV§8 ▏ §7You are a")){
			gameMode.setState(GameState.GAME);
			TIMV.calculateTraitors(The5zigAPI.getAPI().getServerPlayers().size());
			TIMV.calculateDetectives(The5zigAPI.getAPI().getServerPlayers().size());
			The5zigAPI.getLogger().info("(" + message + ")");
			
			String role = "";
			if(message.contains("§aINNOCENT§7")){
				role = "Innocent";
			}
			else if(message.contains("§cTRAITOR§7")){
				role = "Traitor";
			}
			else if(message.contains("§9DETECTIVE§7")){
				role = "Detective";
			}
			TIMV.role = role;
			
			Timer timer = new Timer();
			ScoreboardFetcherTask sft = new ScoreboardFetcherTask();
			timer.schedule(sft, 1500);
			
		}
		//glorious
		else if(ActiveGame.is("timv") && message.contains("ItsNiklass§8 » ") && !message.contains("§b§lParty§8")){
			if(message.contains("▍ ")){
				//In Lobby
				String[] msg = message.split("▍ ");
				msg[0] = "§e1337§8 ▍ ";
				msg[1] = msg[1].replaceAll("Watson", "Dev").replaceAll("§a", "§7");
				The5zigAPI.getAPI().messagePlayer(msg[0] + msg[1]);
				return true;
			}
			else {
			//Ingame
			The5zigAPI.getAPI().messagePlayer(message.replaceAll("Watson", "Dev").replaceAll("§a", "§7"));
			return true;
			}
		}
		else if(ActiveGame.is("timv") && message.contains("RoccoDev§8 » ") && !message.contains("§b§lParty§8")){
			//y tho
			if(message.contains("▍ ")){
				//dank memez o/
			
				String[] msg = message.split("▍ ");
				msg[0] = "§e1337§8 ▍ ";
				msg[1] = msg[1].replaceAll(watisdis.wat, "Dev").replaceAll("§a", "§7");
				The5zigAPI.getAPI().messagePlayer(msg[0] + msg[1]);
				return true;
			}
			else {
			// gimme moar memes plz
			The5zigAPI.getAPI().messagePlayer(message.replaceAll(watisdis.wat, "Dev").replaceAll("§a", "§7"));
			return true;
			}
		}
		
		
		return false;
	}

	@Override
	public void onServerConnect(TIMV gameMode) {
		TIMV.reset(gameMode);
		
		
	}
	
	
	
	private class ScoreboardFetcherTask extends TimerTask{

		@Override
		public void run() {
			for(Map.Entry<String, Integer> e : The5zigAPI.getAPI().getSideScoreboard().getLines().entrySet()){
				if(e.getValue().intValue() == 3){
					TIMV.gameID = e.getKey();
				}
			}
			
		}
		
	}
	
	
	

	

}

