package tk.roccodev.beezig.games.logging;

import com.csvreader.CsvWriter;

import java.io.File;
import java.io.FileWriter;

public class GameLogger {

    private String fileName;
    private FileWriter writer;
    private CsvWriter csv;
    private String[] headers;

    public GameLogger(String fileName) {
        this.fileName = fileName;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }


    public void logGame(String... toLog){
        try {
            File toWrite = new File(fileName);
            boolean writeHeaders = false;
            if (!toWrite.exists()) {
                writeHeaders = true;
                toWrite.createNewFile();
            }
            writer = new FileWriter(toWrite, true);
            csv = new CsvWriter(writer, ',');

            if (writeHeaders) {
                for (String s : headers) csv.write(s);
                csv.endRecord();
            }

            for (String s : toLog) {
                if (s != null) csv.write(s);
                else csv.write("Null string?");
            }
            csv.endRecord();
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            csv.close();
        }
    }

}
