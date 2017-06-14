package tk.roccodev.zta;

public class ActiveGame {

	private static String current;
	
	public static String current(){
		return current;
	}
	
	public static void set(String s){
		current = s;
	}
	
	public static boolean is(String game){
		return current.toUpperCase().equals(game.toUpperCase());
	}
	
	public static void reset(String game){
		if(is(game)) set("");
	}

}
