package tk.roccodev.beezig.notes;

import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesManager {

	public static List<String>  notes = new ArrayList<>();

	public static boolean HR1cm5z = false;
	
	
	public static void tramontoccataStelle(){
		
		String[] ℛ = {
				//Non-ASCII characters in an indentifier smh
				"Va, pensiero",
				"Nessun dorma",
				"Vincerò",
				"O mio babbino caro",
				"Veni veni",
				"Cantate Dominum canticum novum",
				"Laus eius in ecclesia sanctorum",
				"Emmanuel captivum solve Israel",
				"Qui gemit in exsilio, privatus Dei Filio",
				"La donna è mobile",
				"Libiaaamo libiaaamo nei lieti caaalici"
		};
		
		Random random = new Random();
		int start = 0;
		int end = ℛ.length - 1;
		
		int index = random.nextInt(end - start) + start;
		
		The5zigAPI.getAPI().sendPlayerMessage("/msg Toccata " + ℛ[index]);
	}
	
	
}
