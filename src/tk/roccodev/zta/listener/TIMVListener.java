
package tk.roccodev.zta.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.autovote.AutovoteUtils;
import tk.roccodev.zta.games.TIMV;
import tk.roccodev.zta.hiveapi.APIValues;
import tk.roccodev.zta.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiTIMV;
import tk.roccodev.zta.settings.Setting;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tk.roccodev.zta.games.TIMV.traitorTeam;

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
		IHive.genericJoin();
		
		//Should've read the docs ¯\_(ツ)_/¯
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					TIMV.initDailyKarmaWriter();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
				
				if(sb != null && sb.getTitle().trim().equalsIgnoreCase(ChatColor.YELLOW + "Your TIMV Stats")){
					
					int karma = sb.getLines().get(ChatColor.AQUA + "Karma");
					if(karma != 0)
					APIValues.TIMVkarma = (long) karma;
					
					
				}else{
				
					
						
						
							try {
								String ign = The5zigAPI.getAPI().getGameProfile().getName();
								APIValues.TIMVkarma = new ApiTIMV(ign).getKarma();
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
				
			}
				
			ApiTIMV api = new ApiTIMV(The5zigAPI.getAPI().getGameProfile().getName());
			TIMV.rankObject = TIMVRank.getFromDisplay(api.getTitle());
			TIMV.rank = TIMV.rankObject.getTotalDisplay();
				
		}}).start();
	}
	

	@Override
	public boolean onServerChat(TIMV gameMode, String message) {
		// Uncomment this to see the real messages with chatcolor. vv
		// The5zigAPI.getLogger().info("(" + message + ")");
		if(ZTAMain.isColorDebug){
			The5zigAPI.getLogger().info("ColorDebug: " + "(" + message + ")");
		}
		if(message.equals(TIMV.joinMessage)){
			TIMV.reset(gameMode);
		}
		if(message.startsWith("§8▍ §6TIMV§8 ▏ §c§l- 20 Karma") && gameMode != null){
			TIMV.minus20();
			if(TIMV.role.equals("Detective")){
				TIMV.applyPoints(-1, "i");
			}
			else{
				TIMV.applyPoints(-1);
			}
	
		} // Assumption, haven't tested yet
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §c§l- 40 Karma") && gameMode != null){
			TIMV.minus40();
			if(TIMV.role.equals("Traitor")){
				TIMV.applyPoints(-2, "t");
			}else{
			TIMV.applyPoints(-2, "d");
			}
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §a§l+ 20 Karma") && gameMode != null){
			TIMV.plus20();
			if(TIMV.role.equals("Traitor")){
			TIMV.applyPoints(2);
			}else{
				TIMV.applyPoints(1);
			}
		}	
		

		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §a§l+ 10 Karma") && gameMode != null){
			TIMV.plus10();
			TIMV.applyPoints(1);
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §a§l+ 25 Karma") && gameMode != null){
			TIMV.plus25();
			TIMV.applyPoints(2); //+1 Det point
			
		}
		else if(message.equals("               §a§m                §f§l Game Over! §a§m                ") && gameMode != null){
			if(!TIMV.dead){
				TIMV.applyPoints(20);
			}
			traitorTeam.addAll(Collections.nCopies(7, "fin"));
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

		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §3Voting has ended! §bThe map") && gameMode != null){
			String afterMsg = message.split("§8▍ §6TIMV§8 ▏ §3Voting has ended! §bThe map")[1];
			The5zigAPI.getLogger().info(afterMsg);
		// §bSky Lands§6
		// 
			String map = "";
		    
		    Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
		    Matcher matcher = pattern.matcher(afterMsg);
		    while (matcher.find()) {
		        map = matcher.group(1);
		    }
		    The5zigAPI.getLogger().info(map);
		    TIMV.mapStr = map;
		    DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "Playing on " + map, "game_timv");
		    
			TIMV.activeMap = TIMV.mapsPool.get(map.toLowerCase());
			
			
		}

		else if(message.contains("is known to have poisonous water...") && gameMode != null && TIMV.activeMap == null){
			//(         §eFrozen Cargo is known to have poisonous water...)
			String mapmsg = ChatColor.stripColor(message.split(" is known to have poisonous water...")[0]).trim().trim();
			String map = "";
			Pattern pattern = Pattern.compile(mapmsg);
			Matcher matcher = pattern.matcher(mapmsg);
			while (matcher.find()) {
			   map = matcher.group(0);
			}
			The5zigAPI.getLogger().info("FALLBACK: " + map);

			TIMV.activeMap = TIMV.mapsPool.get(map.toLowerCase());
		}
		
		else if(message.contains("'s Stats §6§m                  ") && !message.startsWith("§o ")){
			//"          §6§m                  §f ItsNiklass's Stats §6§m                  "
			//Advanced Records
			TIMV.messagesToSend.add(message);
			The5zigAPI.getLogger().info("found header");
			return true;
		}
		else if(message.startsWith("§3 ") && !message.endsWith(" ")){
			
				TIMV.messagesToSend.add(message);
				The5zigAPI.getLogger().info("found entry");
			
			return true;	
		}
		else if(message.contains(" §ahttp://hivemc.com/player/") && !message.startsWith("§o ")){
			//TODO Coloring
			TIMV.footerToSend.add(message);
			The5zigAPI.getLogger().info("Found Player URL");
			
		return true;	
		}

		else if((message.equals("                      §6§m                  §6§m                  ")&& !message.startsWith("§o "))){
			The5zigAPI.getLogger().info("found footer");
			TIMV.footerToSend.add(message);	
			The5zigAPI.getLogger().info("executed /records");
			if(TIMV.footerToSend.contains("                      §6§m                  §6§m                  ")){
				//Send AdvRec
				new Thread(new Runnable(){
					@Override
					public void run(){
						TIMV.isRecordsRunning = true;
						The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
						try{
						The5zigAPI.getLogger().info(TIMV.lastRecords);
						ApiTIMV api = new ApiTIMV(TIMV.lastRecords);
						TIMVRank rank = null;
						Long rolepoints = Setting.TIMV_SHOW_KRR.getValue() ? api.getRolepoints() : null;
						if(rolepoints == null && Setting.TIMV_SHOW_TRAITORRATIO.getValue()){
							rolepoints = api.getRolepoints();
						}
						Long mostPoints = Setting.TIMV_SHOW_MOSTPOINTS.getValue() ? api.getMostKarma() : null;
						String rankTitle = Setting.SHOW_NETWORK_RANK_TITLE.getValue() ? api.getParentMode().getNetworkTitle() : "";
						ChatColor rankColor = null;
						if(Setting.SHOW_NETWORK_RANK_COLOR.getValue()){
							rankColor = api.getParentMode().getNetworkRankColor();
						}
						long karma = 0;
						long traitorPoints = 0;
						Date lastGame = Setting.SHOW_RECORDS_LASTGAME.getValue() ? api.lastPlayed() : null;
						Integer achievements = Setting.SHOW_RECORDS_ACHIEVEMENTS.getValue() ? api.getAchievements() : null;
						String rankTitleTIMV = Setting.SHOW_RECORDS_RANK.getValue() ? api.getTitle() : null;
						int monthlyRank = (Setting.SHOW_RECORDS_MONTHLYRANK.getValue() &&  api.getLeaderboardsPlacePoints(349) < api.getKarma())? api.getMonthlyRank() : 0;
						if(rankTitleTIMV != null) rank = TIMVRank.getFromDisplay(rankTitleTIMV);
						List<String> messages = new ArrayList<String>();
						messages.addAll(TIMV.messagesToSend);
							Iterator<String> it = messages.iterator();
							for(String s : messages){
								
								if(s.trim().endsWith("'s Stats §6§m")){
								 	//"          §6§m                  §f ItsNiklass's Stats §6§m                  "
								 	//"§6§m                  §f ItsNiklass's Stats §6§m"
								 	The5zigAPI.getLogger().info("Editing Header...");
									StringBuilder sb = new StringBuilder();
									String correctUser = api.getParentMode().getCorrectName();
									if(correctUser.contains("nicked player")) correctUser = "Nicked/Not found";
									sb.append("          §6§m                  §f ");
									The5zigAPI.getLogger().info("Added base...");
									if(rankColor != null) {
										sb.append(rankColor).append(correctUser);
										The5zigAPI.getLogger().info("Added colored user...");
									}
									else{
										sb.append(correctUser);
										The5zigAPI.getLogger().info("Added white user...");
									}
									sb.append("§f's Stats §6§m                  ");
									The5zigAPI.getLogger().info("Added end...");
									The5zigAPI.getAPI().messagePlayer("§o " + sb.toString());
									
									if(rankTitle != null && rankTitle.contains("nicked player")) rankTitle = "Nicked/Not found";
									if(!rankTitle.equals("Nicked/Not found") && !rankTitle.isEmpty()){
											if(rankColor == null) rankColor = ChatColor.WHITE;
											The5zigAPI.getAPI().messagePlayer("§o           " + "§6§m       §6" + " (" + rankColor + rankTitle + "§6) " + "§m       ");
										}
									continue;
								 	}
								
								if(s.startsWith("§3 Karma: §b")){
									StringBuilder sb = new StringBuilder();
									sb.append("§3 Karma: §b");
									karma = Long.parseLong(s.replaceAll("§3 Karma: §b", ""));
									sb.append(karma);
									if(rank != null) sb.append(" (").append(rank.getTotalDisplay());
									if(Setting.TIMV_SHOW_KARMA_TO_NEXT_RANK.getValue() && rank != null){
										sb.append(" / ").append(rank.getKarmaToNextRank((int) karma));
									}
									sb.append("§b)");
									The5zigAPI.getAPI().messagePlayer(sb.toString().trim() + " ");
									continue;
								}
								
								else if(s.startsWith("§3 Traitor Points: §b") && karma > 1000 && Setting.TIMV_SHOW_TRAITORRATIO.getValue()){
									String[] contents = s.split(":");
									traitorPoints = Integer.parseInt(s.replaceAll("§3 Traitor Points: §b", "").trim());
									long rp = rolepoints;
									double tratio = Math.round(((double)traitorPoints / (double)rp) * 1000d) / 10d;
									ChatColor ratioColor = ChatColor.AQUA;
									if(tratio >= TIMV.TRATIO_LIMIT){
										ratioColor= ChatColor.RED;
									}
									The5zigAPI.getAPI().messagePlayer(ChatColor.DARK_AQUA + " Traitor Points: " + ChatColor.AQUA + traitorPoints + " (" + ratioColor + tratio + "%" +  ChatColor.AQUA + ") ");
									continue;
								}
								The5zigAPI.getAPI().messagePlayer(s + " ");
								
							}
						
						


						Double krr = Setting.TIMV_SHOW_KRR.getValue() ? (double)Math.round((double) karma / (double) rolepoints * 100D) / 100D : null;
						
							
						if(mostPoints != null){
							The5zigAPI.getAPI().messagePlayer("§o§3 Most Points: §b" + mostPoints + " ");
						}
						if(achievements != null){
							The5zigAPI.getAPI().messagePlayer("§o§3 Achievements: §b" + achievements + "/64 ");
						}
						if(krr != null){
							The5zigAPI.getAPI().messagePlayer("§o§3 Karma/Rolepoints: §b" + krr + " ");
						}
						if(monthlyRank != 0){					
							The5zigAPI.getAPI().messagePlayer("§o§3 Monthly Leaderboards: §b#" + monthlyRank + " ");
						}
						if(lastGame != null){
							Calendar lastSeen = Calendar.getInstance();
							lastSeen.setTimeInMillis(lastGame.getTime());
						
							The5zigAPI.getAPI().messagePlayer("§o§3 Last Game: §b" + APIUtils.getTimeAgo(lastSeen.getTimeInMillis()) + " ");
						}
						
							
							for(String s : TIMV.footerToSend){
								
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
						
						
						
						TIMV.messagesToSend.clear();
						TIMV.footerToSend.clear();
						TIMV.isRecordsRunning = false;
						
						
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
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							for(String s : TIMV.footerToSend){
								The5zigAPI.getAPI().messagePlayer("§o " + s);
							}
							The5zigAPI.getAPI().messagePlayer("§o " + "                      §6§m                  §6§m                  ");
							TIMV.messagesToSend.clear();
							TIMV.footerToSend.clear();
							TIMV.isRecordsRunning = false;
						}
					
				
				
				
				
			
			
			
					}
				}, "TIMV Advanced Records Fetcher").start(); // Labeling threads
				return true;
			}
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §a§lVote received. §3Your map now has") && Setting.AUTOVOTE.getValue()){
			TIMV.hasVoted = true;
		}
		
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §6§e§e§l6. §f§cRandom map") && !TIMV.hasVoted && Setting.AUTOVOTE.getValue()){
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
					List<String> parsedMaps = new ArrayList<String>();
					parsedMaps.addAll(AutovoteUtils.getMapsForMode("timv"));
					
					
					List<String> votesindex = new ArrayList<String>();
					List<String> finalvoting = new ArrayList<String>();
					
					
					
					for(String s : votesCopy){
						
						String[] data = s.split("\\.");						
						String index = ChatColor.stripColor(data[0]).replaceAll("§8▍ §6TIMV§8 ▏ §6§6§l", "").replaceAll("▍ TIMV ▏", "").trim();
						String[] toConsider = ChatColor.stripColor(data[1]).split("\\[");
						String consider = ChatColor.stripColor(toConsider[0]).trim().replaceAll(" ", "_").toUpperCase();
						
						String votes = toConsider[1].split(" ")[0].trim();
						
						
						The5zigAPI.getLogger().info("trying to match " + consider);
						if(parsedMaps.contains(consider)){
							votesindex.add(votes + "-" + index);
							The5zigAPI.getLogger().info("Added " + consider + " Index #" + index + " with " + votes + " votes");	
						}else{
							The5zigAPI.getLogger().info(consider + " is not a favourite");
						}
						if(index.equals("5")){
							if(votesindex.size() != 0){
								for(String n : votesindex){
									finalvoting.add(n.split("-")[0] + "-" + (10 - Integer.valueOf(n.split("-")[1])));
								}
								int finalindex = (10 - Integer.valueOf(Collections.max(finalvoting).split("-")[1]));
								The5zigAPI.getLogger().info("Voting " + finalindex);
								The5zigAPI.getAPI().sendPlayerMessage("/v " + finalindex);
								
								TIMV.votesToParse.clear();
								TIMV.hasVoted = true;
																										//we can't really get the map name at this point
								The5zigAPI.getAPI().messagePlayer("§8▍ §6TIMV§8 ▏ " + "Automatically voted for map §6#" + finalindex);
								return;
							}
							else if(Setting.AUTOVOTE_RANDOM.getValue()){
								The5zigAPI.getLogger().info("Done, couldn't find matches - Voting Random");
								The5zigAPI.getAPI().sendPlayerMessage("/v 6");
								The5zigAPI.getAPI().messagePlayer("§8▍ §6TIMV§8 ▏ " + "§eAutomatically voted for §cRandom map");
								TIMV.votesToParse.clear();
								TIMV.hasVoted = true;
								//he hasn't but we don't want to check again and again
							return;
							}
						}						
					}	
				}
			}).start();
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §6§e§e§l") && !TIMV.hasVoted && Setting.AUTOVOTE.getValue()){
			TIMV.votesToParse.add(message);		
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §6The body of §4")){
			TIMV.traitorsDiscovered++;
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §6The body of §1")){
			TIMV.detectivesDiscovered++;
		}
		else if(message.startsWith("§8▍ §6TIMV§8 ▏ §6You are now in the spectator lobby")){
			TIMV.dead = true;
			
		}
		else if(message.contains("§f§lYOU ARE ")){
			gameMode.setState(GameState.GAME);
			TIMV.calculateTraitors(The5zigAPI.getAPI().getServerPlayers().size());
			TIMV.calculateDetectives(The5zigAPI.getAPI().getServerPlayers().size());
			The5zigAPI.getLogger().info("(" + message + ")");
			
			String role = "";
			if(message.contains("§a§lINNOCENT§f§l")){
				role = "Innocent";
			}
			else if(message.contains("§c§lA TRAITOR§f§l")){
				role = "Traitor";
			}
			else if(message.contains("§9§lA DETECTIVE§f§l")){ // Assumption
				role = "Detective";
			}
			TIMV.role = role;
			
			Timer timer = new Timer();
			ScoreboardFetcherTask sft = new ScoreboardFetcherTask();
			timer.schedule(sft, 1500);
			
		}
		else if(message.contains("   §4") && traitorTeam.size() < 7 && TIMV.role.equals("Traitor")){
			//§4jordix03, ItsNiklass, Vpnce, BatHex
			//The5zigAPI.getLogger().info(ChatColor.stripColor(message).split(", "));
			traitorTeam.addAll(Arrays.asList(ChatColor.stripColor(message).replaceAll(" ","").split(",")));
			The5zigAPI.getLogger().info(traitorTeam.toString());
		}
		else if(message.equals("                        §c§m                                ") && traitorTeam.size() < 7 && traitorTeam.size() > 0 && TIMV.role.equals("Traitor")){


			new Thread(new Runnable(){
				@Override
				public void run(){

					ArrayList<Long> TraitorKarma = new ArrayList<>();
					for(String name : traitorTeam){
						ApiTIMV api = new ApiTIMV(name);
						TraitorKarma.add(api.getKarma());
					}
					Long avg = APIUtils.average(TraitorKarma.toArray());
					The5zigAPI.getAPI().messagePlayer("                        §c§m                                ");
					The5zigAPI.getAPI().messagePlayer("                           §4Traitor Karma: " + avg);
					The5zigAPI.getAPI().messagePlayer("                        §c§m                                ");

				}
			}).start();
		}
		//glorious
		/*else if(ActiveGame.is("timv") && message.contains("ItsNiklass§8 » ") && !message.contains("§b§lParty§8")){
			if(message.contains("▍ ")){
				//In Lobby
				String[] msg = message.split("▍ ");
				msg[0] = "§eDev§8 ▍ ";
				msg[1] = msg[1].replaceAll("Watson", "Watson ⚝").replaceAll("§a", "§b");
				The5zigAPI.getAPI().messagePlayer(msg[0] + msg[1]);
				return true;
			}
			else {
			//Ingame
			The5zigAPI.getAPI().messagePlayer(message.replaceAll("Watson", "Watson ⚝").replaceAll("§a", "§b"));
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
		}*/
		
		
		return false;
	}

	@Override
	public void onServerConnect(TIMV gameMode) {
		TIMV.reset(gameMode);
		
		
	}
	
	
	
	
	
	@Override
	public void onServerDisconnect(TIMV gameMode) {
		TIMV.reset(gameMode);
	}





	@Override
	public boolean onActionBar(TIMV gameMode, String message) {
		if(!TIMV.actionBarChecked){
			The5zigAPI.getLogger().info(message != null ? message : "lolnull");
			if(message != null && message.contains("▏ §7")){
				String[] data = message.split("▏ §7");
				TIMV.gameID = data[1];
				TIMV.actionBarChecked = true;
			}
		}
		return false;
	}


	



	private class ScoreboardFetcherTask extends TimerTask{

		@Override
		public void run() {
			
			
		}
		
	}
	
	
	

	

}

