package tk.roccodev.zta.utils.acr;

public enum ChatReason {

	SPAMCLEAN,
	SPAMDIRTY,
	PORN,
	PLAYERABUSE,
	ADVERTISING,
	IMPERSONATING,
	FOULLANGUAGE,
	TROLLING,
	RACISM,
	DISCRIMINATION,
	DDOS;
	
	public static boolean is(String s) {
		try {
			valueOf(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
}
