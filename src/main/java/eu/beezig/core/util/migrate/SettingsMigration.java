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

package eu.beezig.core.util.migrate;

import com.google.common.base.Splitter;
import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.util.ExceptionHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsMigration {
    public void migrate() {
        try {
            Map<String, String> parsed = getOldConfig();
            if(parsed == null) return;
            Map<String, String> dict = getDictionary();
            for(Map.Entry<String, String> entry : parsed.entrySet()) {
                String newKey = dict.get(entry.getKey());
                if(newKey != null) Beezig.cfg().set(Settings.valueOf(newKey), entry.getValue());
            }
            Beezig.cfg().save();
        } catch (Exception ex) {
            ExceptionHandler.catchException(ex, "Couldn't migrate settings");
        }
    }

    private Map<String, String> getOldConfig() throws IOException {
        Splitter equals = Splitter.on('=');
        File file = new File(Beezig.get().getBeezigDir(), "settings.properties");
        if(!file.exists()) return null;
        Map<String, String> matches = new HashMap<>();
        try(BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset())) {
            reader.lines().forEach(s -> {
                if(s.charAt(0) == '#') return;
                List<String> data = equals.splitToList(s);
                matches.put(data.get(0).trim(), data.get(1).trim());
            });
        }
        file.deleteOnExit();
        return matches;
    }

    private Map<String, String> getDictionary() throws IOException {
        Splitter space = Splitter.on(' ');
        Map<String, String> matches = new HashMap<>();
        LineIterator lines = IOUtils.lineIterator(Beezig.class.getResourceAsStream("/migrate-settings.map"), Charset.defaultCharset());
        lines.forEachRemaining(s -> {
            List<String> data = space.splitToList(s);
            matches.put(data.get(0), data.get(1));
        });
        return matches;
    }
}
