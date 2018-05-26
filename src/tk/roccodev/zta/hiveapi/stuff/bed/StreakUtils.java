package tk.roccodev.zta.hiveapi.stuff.bed;

import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.BED;

import java.io.*;
import java.util.Calendar;

public class StreakUtils {

	public static File streakFile;

	public static String dailyStreakName;
    public static PrintWriter dailyStreakWriter;
    
    public static void setDailyStreakFileName(final String newName) {
        dailyStreakName = newName;
    }

    public static void initDailyStreakWriter() throws IOException {
       
        final FileInputStream stream = new FileInputStream(streakFile);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        final String line = reader.readLine();
        if (line == null) {
            initStreakWriterWithZero();
            stream.close();
            return;
        }
        BED.winstreak = Integer.parseInt(line);
        stream.close();
        dailyStreakWriter = new PrintWriter(streakFile, "UTF-8");
    }
    
    private static void initStreakWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        (dailyStreakWriter = new PrintWriter(streakFile, "UTF-8")).println(0);
        dailyStreakWriter.close();
        dailyStreakWriter = new PrintWriter(streakFile, "UTF-8");
    }
    
    public static void saveDailyStreak() {
       dailyStreakWriter.println(BED.winstreak);
        dailyStreakWriter.flush();
        dailyStreakWriter.close();
    }
	
    private static void checkForFileExist(final File f, final boolean directory) {
        if (!f.exists()) {
            try {
                if (directory) {
                    f.mkdir();
                }
                else {
                    f.createNewFile();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getTodayFile() {
        final Calendar cal = Calendar.getInstance();
        final StringBuilder sb = new StringBuilder();
        sb.append(cal.get(1) + "-");
        sb.append(cal.get(2) + 1 + "-");
        sb.append(cal.get(5));
        return sb.toString().trim() + ".txt";
    }
	
	public static void init() {
		streakFile = new File(ZTAMain.mcFile + "/bedwars/streak.txt");
		try {
			initDailyStreakWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
