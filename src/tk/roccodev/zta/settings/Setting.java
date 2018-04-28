
package tk.roccodev.zta.settings;

import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;

public enum Setting {

	SHOW_NETWORK_RANK_TITLE(false, "Advanced Records - Show the network-rank title behind username"),
	SHOW_NETWORK_RANK_COLOR(true, "Advanced Records - Color the username/network-rank respective to their network-rank"),
	SHOW_RECORDS_LASTGAME(true, "Advanced Records - Show last time the player played that game"),
	SHOW_RECORDS_MONTHLYRANK(true, "Advanced Records - Show players' rank on the Monthly Leaderboards by Maxthat"),
	SHOW_RECORDS_ACHIEVEMENTS(true, "Advanced Records - Show achievements"),
	SHOW_RECORDS_RANK(true, "BED Advanced Records - Show point based rank"),
	DISCORD_RPC(true, "Use Discord Rich Presence"),
	AUTOVOTE(true, "Turn the autovote feature on or off"),
	AUTOVOTE_RANDOM(true, "Autovote for random map if no favorites are found"),
	
	TIMV_SHOW_KRR(true, "TIMV Advanced Records - Show Karma/rolepoints"),
	TIMV_SHOW_MOSTPOINTS(true, "TIMV Advanced Records - Show Karma record"),
	TIMV_SHOW_KARMA_TO_NEXT_RANK(false, "TIMV Advanced Records - Show karma to next rank"),
	TIMV_SHOW_TRAITORRATIO(false, "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio"),
	TIMV_USE_TESTREQUESTS(true, "Replace \" test\" with nicer phrases to avoid HAS"),
	
	DR_SHOW_POINTSPERGAME(true, "DR Advanced Records - Show the average Points per Game"),
	DR_SHOW_RUNNERWINRATE(true, "DR Advanced Records - Show the Winrate as a Runner"),
	DR_SHOW_DEATHSPERGAME(true, "DR Advanced Records - Show the avg. Deaths as Runner"),
	DR_SHOW_POINTS_TO_NEXT_RANK(false, "DR Advanced Records - Show points to next rank"),
	
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
	
	SKY_SHOW_POINTS_TO_NEXT_RANK(true, "SKY Advanced Records - Show points to next rank"), 
	SKY_SHOW_WINRATE(true, "SKY Advanced Records - Show Winrate");
	

	
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

