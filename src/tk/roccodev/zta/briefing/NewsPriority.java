package tk.roccodev.zta.briefing;

import java.util.stream.Stream;

public enum NewsPriority {

	LOWEST(0), LOW(1), MEDIUM(2), HIGH(3), HIGHEST(4);
	
	private int value;
	
	NewsPriority(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static NewsPriority getByValue(int value) {
		return Stream.of(values()).filter(n -> n.getValue() == value).findFirst().get();
	}
	
}
