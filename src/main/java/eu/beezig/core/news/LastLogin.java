/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.news;

import eu.beezig.core.Beezig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;

public class LastLogin {
    private Instant lastLogin;
    private File file;

    LastLogin() {
        file = new File(Beezig.get().getBeezigDir(), "lastlogin.txt"); // Use the old one from Beezig 6
    }

    public void read() throws IOException {
        if(!file.exists()) {
            lastLogin = Instant.ofEpochMilli(Beezig.DEBUG ? 0 : System.currentTimeMillis());
            return;
        }
        String contents = FileUtils.readFileToString(file, Charset.defaultCharset());
        long time = Long.parseLong(contents, 10);
        lastLogin = Instant.ofEpochMilli(time);
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void update() throws IOException {
        FileUtils.write(file, Long.toString(System.currentTimeMillis(), 10), Charset.defaultCharset(), false);
    }
}
