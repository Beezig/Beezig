package tk.roccodev.zta.settings;

import java.io.IOException;

public enum Setting {

	TIMV_SHOW_KRR(true, "TIMV Advanced Records - Show Karma/rolepoints"),
	TIMV_SHOW_ACHIEVEMENTS(true, "TIMV Advanced Records - Show achievements"),
	TIMV_SHOW_RANK(true, "TIMV Advanced Records - Show Karma-based rank"),
	TIMV_SHOW_MOSTPOINTS(true, "TIMV Advanced Records - Show Karma record"),
	SHOW_NETWORK_RANK_TITLE(false, "Advanced Records - Show the network-rank title behind username"),
	SHOW_NETWORK_RANK_COLOR(true, "Advanced Records - Color the username/network-rank respective to their network-rank"),
	TIMV_SHOW_TRAITORRATIO(false, "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio (Advanced players)");
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public void setValueWithoutSaving(boolean value){
		this.value = value;
		
			
		
	}
	
	
}
