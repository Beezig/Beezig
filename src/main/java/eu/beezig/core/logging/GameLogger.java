/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.logging;

import com.csvreader.CsvWriter;
import eu.beezig.core.Beezig;

import java.io.File;
import java.io.FileWriter;

public class GameLogger {

    private File file;
    private String[] headers;

    public GameLogger(String modeName) {
        this.file = new File(Beezig.get().getBeezigDir(), String.format("%s/games.csv", modeName));
    }

    public void setHeaders(String... headers) {
        this.headers = headers;
    }

    public void log(Object... toLog) {
        CsvWriter csv = null;
        try {
            boolean writeHeaders = false;
            if (!file.exists()) {
                writeHeaders = true;
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, true);
            csv = new CsvWriter(writer, ',');

            if (writeHeaders) {
                for (String s : headers) csv.write(s);
                csv.endRecord();
            }

            for (Object s : toLog) {
                if (s != null) csv.write(s.toString());
                if(s instanceof Boolean) csv.write((boolean)s ? "Yes" : "No");
                else csv.write("Unknown");
            }
            csv.endRecord();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(csv != null) csv.close();
        }
    }
}
