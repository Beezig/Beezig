
package tk.roccodev.beezig.settings;

import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;

public enum Setting {

	SHOW_NETWORK_RANK_TITLE(false, "Advanced Records - Show the network-rank title behind username"),
	SHOW_NETWORK_RANK_COLOR(true, "Advanced Records - Color the username/network-rank respective to their network-rank"),
	SHOW_RECORDS_LASTGAME(true, "Advanced Records - Show last time the player played that game"),
	SHOW_RECORDS_MONTHLYRANK(true, "Advanced Records - Show players' rank on the Monthly Leaderboards by Maxthat"),
	SHOW_RECORDS_ACHIEVEMENTS(true, "Advanced Records - Show achievements"),
	SHOW_RECORDS_RANK(true, "Advanced Records - Show point based rank"),
	DISCORD_RPC(true, "Use Discord Rich Presence"),
	AUTOVOTE(true, "Turn the autovote feature on or off"),
	AUTOVOTE_RANDOM(true, "Autovote for random map if no favorites are found"),
	BRIEFING(true, "Opt-in or opt-out for the news briefing"),
	PM_PING(false, "Play a ping sound when a PM is received."),
	PM_NOTIFICATION(false, "Receive a system notification when a PM is received."),
	
	TIMV_SHOW_KRR(true, "TIMV Advanced Records - Show Karma/rolepoints"),
	TIMV_SHOW_MOSTPOINTS(true, "TIMV Advanced Records - Show Karma record"),
	TIMV_SHOW_KARMA_TO_NEXT_RANK(false, "TIMV Advanced Records - Show karma to next rank"),
	TIMV_SHOW_TRAITORRATIO(false, "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio"),
	TIMV_USE_TESTREQUESTS(true, "Replace \" test\" with nicer phrases to avoid HAS"),
	
	DR_SHOW_POINTSPERGAME(true, "DR Advanced Records - Show the avg. points per game"),
	DR_SHOW_RUNNERWINRATE(true, "DR Advanced Records - Show the winrate as a runner"),
	DR_SHOW_DEATHSPERGAME(true, "DR Advanced Records - Show the avg. deaths as runner"),
	DR_SHOW_POINTS_TO_NEXT_RANK(false, "DR Advanced Records - Show points to next rank"),
	DR_SHOW_KILLSPERGAME(true, "DR Advanced Records - Show the avg. kills as death"),
	DR_SHOW_TOTALPB(true, "DR Advanced Records - Show the cumulative amount of personal bests"),
	
	BED_SHOW_POINTS_TO_NEXT_RANK(true, "BED Advanced Records - Show points to next rank"),
	BED_SHOW_ELIMINATIONS_PER_GAME(false, "BED Advanced Records - Show Eliminations per Game"),
	BED_SHOW_BEDS_PER_GAME(true, "BED Advanced Records - Show Beds destroyed per Game"), 
	BED_SHOW_DEATHS_PER_GAME(false, "BED Advanced Records - Show Deaths per Game"), 
	BED_SHOW_KILLS_PER_GAME(false, "BED Advanced Records - Show Kills per Game"), 
	BED_SHOW_POINTS_PER_GAME(true, "BED Advanced Records - Show Points gained per Game"), 
	BED_SHOW_KD(true, "BED Advanced Records - Show Kills/Deaths"), 
	BED_SHOW_WINRATE(true, "BED Advanced Records - Show Winrate"), 
	
	Giant_SHOW_WINRATE(true, "Giant Advanced Records - Show Winrate"),
	Giant_SHOW_KD(true, "Giant Advanced Records - Show Kills/Deaths"), 
	Giant_SHOW_PPG(true, "Giant Advanced Records - Show the average Points per Game"),
	
	HIDE_SHOW_WINRATE(true, "HIDE Advanced Records - Show Winrate"), 
	HIDE_SHOW_SEEKER_KPG(true, "HIDE Advanced Records - Show Kills per Game as Seeker"),
	HIDE_SHOW_HIDER_KPG(false, "HIDE Advanced Records - Show Kills per Game as Hider"),
	HIDE_SHOW_POINTSPG(true, "HIDE Advanced Records - Show Points per Game"),
	HIDE_SHOW_AMOUNT_UNLOCKED(true, "HIDE Advanced Records - Show amount of unlocked blocks"),
	HIDE_SHOW_POINTS_TO_NEXT_RANK(true, "HIDE Advanced Records - Show points to next rank"),
	
	CAI_SHOW_WINRATE(true, "CAI Advanced Records - Show Winrate"), 
	CAI_SHOW_POINTSPG(true, "CAI Advanced Records - Show Points per Game"),
	CAI_SHOW_POINTS_TO_NEXT_RANK(true, "CAI Advanced Records - Show points to next rank"),
	CAI_SHOW_CATCHES_CAUGHT(true, "CAI Advanced Records - Show Catches/Caught Ratio"), 
	CAI_SHOW_CAPTURES_GAME(true, "CAI Advanced Records - Show Captures/Games Ratio"),
	
	SKY_SHOW_POINTS_TO_NEXT_RANK(true, "SKY Advanced Records - Show points to next rank"), 
	SKY_SHOW_WINRATE(true, "SKY Advanced Records - Show Winrate"), 
	SKY_SHOW_KD(true, "SKY Advanced Records - Show Kills/Deaths"),
	SKY_SHOW_KPG(true, "SKY Advanced Records - Show Kills per game"),
	SKY_SHOW_PPG(true, "SKY Advanced Records - Show Points per game"),

	GRAV_SHOW_POINTS_TO_NEXT_RANK(true, "GRAV Advanced Records - Show points to next rank"),
	GRAV_SHOW_FINISHRATE(false, "GRAV Advanced Records - Show Finishrate"),
	GRAV_SHOW_PPG(true, "GRAV Advanced Records - Show Points per game"),
	
	MIMV_SHOW_POINTS_TO_NEXT_RANK(true, "MIMV Advanced Records - Show points to next rank"),
	MIMV_SHOW_WINRATE(true, "MIMV Advanced Records - Show Winrate"), 
	MIMV_SHOW_KD(true, "MIMV Advanced Records - Show Kills/Deaths"),
	MIMV_SHOW_KPG(true, "MIMV Advanced Records - Show Kills per game"),
	MIMV_SHOW_PPG(true, "MIMV Advanced Records - Show Karma per game"),
	
	BP_SHOW_POINTS_TO_NEXT_RANK(true, "BP Advanced Records - Show points to next rank"), 
	BP_SHOW_WINRATE(true, "BP Advanced Records - Show Winrate"), 
	BP_SHOW_PPG(true, "BP Advanced Records - Show Points per game"),
	
	SGN_SHOW_POINTS_TO_NEXT_RANK(true, "SG2 Advanced Records - Show points to next rank"), 
	SGN_SHOW_WINRATE(true, "SG2 Advanced Records - Show Winrate"), 
	SGN_SHOW_PPG(true, "SG2 Advanced Records - Show Points per game"),
	SGN_SHOW_KD(true, "SG2 Advanced Records - Show Kills/Deaths");


	private boolean value;
	private String briefDesc;
	
	Setting(boolean value, String briefDesc){
		this.value = value;
		this.briefDesc = briefDesc;
	}
	
	public boolean getValue(){
		return value;
	}
	
	public String getBriefDescription(){
		return briefDesc;
	}
	
	public void setValue(boolean value){
		this.value = value;
		
			try {
				SettingsFetcher.saveSettings();
			} catch (IOException e) {
				The5zigAPI.getLogger().info("Failed to save Settings");
				e.printStackTrace();
			}
		
	}
	
	public void setValueWithoutSaving(boolean value){
		this.value = value;
		
			
		
	}
	
	
}

