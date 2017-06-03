package tk.roccodev.zta.settings;

import java.io.IOException;

public enum Setting {

	SHOW_KRR(true, "Advanced Records - Show Karma/rolepoints"),
	SHOW_ACHIEVEMENTS(true, "Advanced Records - Show achievements"),
	SHOW_RANK(true, "Advanced Records - Show Karma-based rank"),
	SHOW_MOSTPOINTS(true, "Advances Records - Show Karma record");
	
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
