package tk.roccodev.zta;







import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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
	public static Logger gameLogger = Logger.getLogger("TIMVGameLogger");
	
	
	public static void startLogger(){
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("ddMMyyHHmmssSS");
		String date = sdf.format(new Date());
		FileHandler fh = null;
		try {
			fh = new FileHandler(ZTAMain.mcFile + "5zigtimv/" + date);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateFormatter mf = new TIMV.DateFormatter();
		
        fh.setFormatter(mf); 
        
		gameLogger.addHandler(fh);
		gameLogger.setUseParentHandlers(false);
		gameLogger.info("Initialized game with " + The5zigAPI.getAPI().getServerPlayers().size() + " players.");
	
	}
	
	public static void stopLogger(){
		gameLogger = null;
	}
	
	public static void logCheckNull(String toLog){
		if(gameLogger != null){
			gameLogger.info(toLog);
		}
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
	}
	
	public static void calculateTraitors(int playersOnline){
		TIMV.traitorsBefore = (int) Math.floor(playersOnline / 4.0); 
	}
	
	public static void reset(TIMV gm){
		resetCounter();
		TIMV.activeMap = null;
		TIMV.traitorsBefore = 0;
		TIMV.traitorsDiscovered = 0;
		NotesManager.notes.clear();
		gm.setState(GameState.FINISHED);
		ZTAMain.isTIMV = false;
		TIMV.stopLogger();
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
