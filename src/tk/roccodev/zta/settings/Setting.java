
package tk.roccodev.zta.settings;

import java.io.IOException;

import eu.the5zig.mod.The5zigAPI;

public enum Setting {

	SHOW_NETWORK_RANK_TITLE(false, "Advanced Records - Show the network-rank title behind username"),
	SHOW_NETWORK_RANK_COLOR(true, "Advanced Records - Color the username/network-rank respective to their network-rank"),
	SHOW_RECORDS_LASTGAME(true, "Advanced Records - Show last time the player played that game"),
	
	TIMV_SHOW_KRR(true, "TIMV Advanced Records - Show Karma/rolepoints"),
	TIMV_SHOW_ACHIEVEMENTS(true, "TIMV Advanced Records - Show achievements"),
	TIMV_SHOW_RANK(true, "TIMV Advanced Records - Show Karma-based rank"),
	TIMV_SHOW_MOSTPOINTS(true, "TIMV Advanced Records - Show Karma record"),
	TIMV_SHOW_TRAITORRATIO(false, "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio (Advanced players)"),
	TIMV_SHOW_MONTHLYRANK(true, "TIMV Advanced Records - Show players' rank on the Monthly Leaderboards by Maxthat"),
	
	DR_SHOW_RANK(true, "DR Advanced Records - Show Point-based rank"),
	DR_SHOW_POINTSPERGAME(true, "DR Advanced Records - Show the average Points per Game"),
	DR_SHOW_RUNNERWINRATE(true, "DR Advanced Records - Show the Winrate as a Runner"),
	DR_SHOW_DEATHSPERGAME(true, "DR Advanced Records - Show the avg. Deaths as Runner"),
	DR_SHOW_ACHIEVEMENTS(true, "DR Advanced Records - Show players' achievements"),
	DR_SHOW_MONTHLYRANK(true, "DR Advanced Records - Show players' rank on the Monthly Leaderboards by Maxthat"),
	
	BED_SHOW_ACHIEVEMENTS(true, "BED Advanced Records - Show achievements"),
	BED_SHOW_RANK(true, "BED Advanced Records - Show Point-based rank");
	
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

