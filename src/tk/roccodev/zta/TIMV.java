package tk.roccodev.zta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.csvreader.CsvWriter;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.TIMVMap;
import tk.roccodev.zta.notes.NotesManager;

public class TIMV extends GameMode{

	public static int karmaCounter;
	public static TIMVMap activeMap;
	public static String lastRecords = "";
	public static int traitorsBefore = 0;
	public static int traitorsDiscovered = 0;
	
	
	//CSV Stuff
	
	public static List<String[]> csvEntries;
	public static String role;
	public static int tPoints;
	public static int dPoints;
	public static int iPoints;
	
	
	
	
	public static void writeCsv(){
		The5zigAPI.getLogger().info("writing");
		// Prevent from writing a line twice
		if(role == null) return;
		if(role.isEmpty()) return;
		The5zigAPI.getLogger().info("writing2");
		String[] entries = {role, karmaCounter + "", activeMap.getDisplayName() };
		CsvWriter writer = null;
		
		boolean alreadyExists = new File(ZTAMain.mcFile.getAbsolutePath() + "/games.csv").exists();
		if(!alreadyExists){
			try {
				new File(ZTAMain.mcFile.getAbsolutePath() + "/games.csv").createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer = new CsvWriter(new FileWriter(ZTAMain.mcFile.getAbsolutePath() + "/games.csv", true), ',');
		
		if (!alreadyExists){
			//Create the header
			writer.write("role");
			writer.write("karma");
			writer.write("map");
			writer.write("points");
			writer.write("i-points");
			writer.write("d-points");
			writer.write("t-points");
			writer.endRecord();
		}
		
		writer.write(role);
		writer.write(karmaCounter + "");
		writer.write(activeMap.getDisplayName());
		writer.write(iPoints + dPoints + tPoints + "");
		writer.write(iPoints + "");
		writer.write(dPoints + "");
		writer.write(tPoints + "");
		writer.endRecord();
			writer.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		role = null;
		resetCounter();
		TIMV.activeMap = null;
		
	}
	
	
	public static void plus20(){
		karmaCounter +=20;
		HiveAPI.karma +=20;
	}
	public static void plus25(){
		karmaCounter +=25;
		HiveAPI.karma +=25;
	}
	public static void plus10(){
		karmaCounter +=10;
		HiveAPI.karma += 10;
		}
	public static void minus20(){
		karmaCounter -=20;
		HiveAPI.karma -=20;
		}
	public static void minus40(){
		karmaCounter -=40;
		HiveAPI.karma -=40;
		}
	
	public static void resetCounter(){
		karmaCounter = 0;
		iPoints = 0;
		tPoints = 0;
		dPoints = 0;
	}
	
	public static void applyPoints(int points){
		switch(role){
		case "Traitor":
			applyPoints(points, "t");
			break;
		case "Innocent":
			applyPoints(points, "i");
			break;
		case "Detective":
			applyPoints(points, "d");
			break;
		}
	}
	
	public static void applyPoints(int points, String role){
		switch(role){
		case "t": 
			tPoints += points;
			break;
		case "i":
			iPoints += points;
			break;
		case "d":
			dPoints += points;
			break;
		}
	}
	
	public static void calculateTraitors(int playersOnline){
		TIMV.traitorsBefore = (int) Math.floor(playersOnline / 4.0); 
	}
	
	public static void reset(TIMV gm){
		
		TIMV.writeCsv();
		TIMV.traitorsBefore = 0;
		TIMV.traitorsDiscovered = 0;
		NotesManager.notes.clear();
		gm.setState(GameState.FINISHED);
		ZTAMain.isTIMV = false;
		
		
	}
	
	@Override
	public String getName(){
		return "Trouble in Mineville";
	}

	public static boolean shouldRender(GameState state){
		
		if(state == GameState.GAME) return true;
		if(state == GameState.PREGAME) return true;
		if(state == GameState.STARTING) return true;
		return false;
	}
	
	
	static class DateFormatter extends Formatter {
	    //
	    // Create a DateFormat to format the logger timestamp.
	    //
	    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	    public String format(LogRecord record) {
	        StringBuilder builder = new StringBuilder(1000);
	        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
	        
	        
	        builder.append("[").append(record.getLevel()).append("] - ");
	        builder.append(formatMessage(record));
	        builder.append("\n");
	        return builder.toString();
	    }

	    public String getHead(Handler h) {
	        return super.getHead(h);
	    }

	    public String getTail(Handler h) {
	        return super.getTail(h);
	    }
	
	}

	
	
}
