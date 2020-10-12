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

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReverseCsvReader implements AutoCloseable {
    private ReversedLinesFileReader reader;
    private String delimiter;

    public ReverseCsvReader(File fileIn, String delimiter) throws IOException {
        reader = new ReversedLinesFileReader(fileIn, 4096, StandardCharsets.UTF_8);
        this.delimiter = delimiter;
    }

    public String[] getNextRecord() throws IOException {
        String line = reader.readLine();
        if(line == null) return null;
        return line.split(delimiter);
    }

    @Override
    public void close() {
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
