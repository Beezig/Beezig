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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DailyService {
    private int points;
    private File file;

    public DailyService(File f) {
        this.file = f;
    }

    void loadFromFile() throws IOException {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return;
        }
        String contents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        if(contents.isEmpty()) return;
        this.points = Integer.parseInt(contents, 10);
    }

    public void save() throws IOException {
        FileUtils.write(file, Integer.toString(points, 10), StandardCharsets.UTF_8, false);
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
