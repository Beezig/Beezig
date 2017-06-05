package tk.roccodev.zta.settings;

import java.io.IOException;

public enum Setting {

	SHOW_KRR(true, "Advanced Records - Show Karma/rolepoints"),
	SHOW_ACHIEVEMENTS(true, "Advanced Records - Show achievements"),
	SHOW_RANK(true, "Advanced Records - Show Karma-based rank"),
	SHOW_MOSTPOINTS(true, "Advanced Records - Show Karma record"),
	SHOW_NETWORK_RANK_TITLE(false, "Advanced Records - Show the network-rank title behind username"),
	SHOW_NETWORK_RANK_COLOR(true, "Advanced Records - Color the username/network-rank respective to their network-rank"),
	SHOW_TRAITORRATIO(false, "Advanced Records - Show the Traitor Points / Rolepoints ratio (Advanced players)");
	
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
