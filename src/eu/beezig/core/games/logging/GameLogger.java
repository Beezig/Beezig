/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.games.logging;

import com.csvreader.CsvWriter;

import java.io.File;
import java.io.FileWriter;

public class GameLogger {

    private String fileName;
    private CsvWriter csv;
    private String[] headers;

    public GameLogger(String fileName) {
        this.fileName = fileName;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }


    public void logGame(Object... toLog) {
        try {
            File toWrite = new File(fileName);
            boolean writeHeaders = false;
            if (!toWrite.exists()) {
                writeHeaders = true;
                toWrite.createNewFile();
            }
            FileWriter writer = new FileWriter(toWrite, true);
            csv = new CsvWriter(writer, ',');

            if (writeHeaders) {
                for (String s : headers) csv.write(s);
                csv.endRecord();
            }

            for (Object s : toLog) {
                if (s != null) csv.write(s.toString());
                else csv.write("Null string?");
            }
            csv.endRecord();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            csv.close();
        }
    }

}
