package tk.roccodev.zta.games;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.csvreader.CsvWriter;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.hiveapi.HiveAPI;
import tk.roccodev.zta.hiveapi.TIMVMap;
import tk.roccodev.zta.notes.NotesManager;

public class TIMV extends GameMode{

	public static int karmaCounter;
	public static TIMVMap activeMap;
	public static String lastRecords = "";
	public static int traitorsBefore = 0;
	public static int traitorsDiscovered = 0;
	public static int detectivesBefore = 0;
	public static int detectivesDiscovered = 0;
	
	public static String gameID;
	
	
	//Advanced Records
	public static List<String> messagesToSend = new ArrayList<String>();
	public static List<String> footerToSend = new ArrayList<String>();
	public static boolean isRecordsRunning = false;
	
	
	//Autovoting
	public static List<String> votesToParse = new ArrayList<String>();
	
	
	//CSV Stuff
	
	public static List<String[]> csvEntries;
	public static String role;
	public static int tPoints;
	public static int dPoints;
	public static int iPoints;
	public static boolean dead;
	public static boolean hasVoted = false;
	public static long lastRecordKarma;
	
	
	/*
	 * Writes the data into the CSV logger.
	 * 
	 * @return whether the writer has written the file.
	 * 
	 */
	public static boolean writeCsv(){
		The5zigAPI.getLogger().info("writing");
		// Prevent from writing a line twice
		if(role == null) return false;
		if(role.isEmpty()) return false;
		The5zigAPI.getLogger().info("writing2");
		String[] entries = {role, karmaCounter + "", activeMap.getDisplayName() };
		CsvWriter writer = null;
		
		boolean alreadyExists = new File(ZTAMain.mcFile.getAbsolutePath() + "/timv/games.csv").exists();
		if(!alreadyExists){
			try {
				new File(ZTAMain.mcFile.getAbsolutePath() + "/timv/games.csv").createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer = new CsvWriter(new FileWriter(ZTAMain.mcFile.getAbsolutePath() + "/timv/games.csv", true), ',');
		
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
		return true;
	}
	
	
	public static void plus20(){
		karmaCounter +=20;
		HiveAPI.TIMVkarma +=20;
	}
	public static void plus25(){
		karmaCounter +=25;
		HiveAPI.TIMVkarma +=25;
	}
	public static void plus10(){
		karmaCounter +=10;
		HiveAPI.TIMVkarma += 10;
		}
	public static void minus20(){
		karmaCounter -=20;
		HiveAPI.TIMVkarma -=20;
		}
	public static void minus40(){
		karmaCounter -=40;
		HiveAPI.TIMVkarma -=40;
		}
	
	public static void resetCounter(){
		karmaCounter = 0;
		iPoints = 0;
		tPoints = 0;
		dPoints = 0;
		dead = false;
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
	
	public static void calculateDetectives(int playersOnline){
		TIMV.detectivesBefore = (int) Math.floor(playersOnline / 8);
	}
	
	public static void reset(TIMV gm){
		
		if(!TIMV.writeCsv()){
			TIMV.activeMap = null;
			role = null;
			resetCounter();
		}
		TIMV.traitorsBefore = 0;
		TIMV.traitorsDiscovered = 0;
		TIMV.detectivesBefore = 0;
		TIMV.detectivesDiscovered = 0;
		NotesManager.notes.clear();
		TIMV.messagesToSend.clear();
		TIMV.footerToSend.clear();
		TIMV.votesToParse.clear();
		TIMV.isRecordsRunning = false;
		TIMV.hasVoted = false;
		gm.setState(GameState.FINISHED);
		ZTAMain.isTIMV = false;
		The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
		
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
