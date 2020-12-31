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
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.StringUtils;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AutovoteMigration {
    private final Pattern YAML_REGEX = Pattern.compile("(\\w+): \\[(.+)]", Pattern.MULTILINE);

    private Map<String, List<String>> getAutovoteMaps() throws IOException {
        Splitter comma = Splitter.on(", ");
        File file = new File(Beezig.get().getBeezigDir(), "autovote.yml");
        if(!file.exists()) return null;
        String contents = FileUtils.readFileToString(file, Charset.defaultCharset()).replaceAll("\\s{2,}", " ");
        Matcher matcher = YAML_REGEX.matcher(contents);
        Map<String, List<String>> matches = new HashMap<>();
        while(matcher.find()) {
            String key = matcher.group(1).toLowerCase(Locale.ROOT);
            String text = matcher.group(2);
            List<String> maps = comma.splitToList(text).stream().map(s ->
                StringUtils.normalizeMapName(s.replace("{c}", ":").replaceAll("['\"]", ""))).collect(Collectors.toList());
            matches.put(key, maps);
        }
        return matches;
    }

    public void migrate() {
        File newFile = new File(Beezig.get().getBeezigDir(), "autovote.json");
        if(newFile.exists()) return;
        try {
            Map<String, List<String>> parsed = getAutovoteMaps();
            if(parsed == null) return;
            JSONObject json = new JSONObject();
            for(Map.Entry<String, List<String>> entry : parsed.entrySet()) {
                JSONArray arr = new JSONArray();
                arr.addAll(entry.getValue());
                json.put(entry.getKey(), arr);
            }
            eu.beezig.core.util.FileUtils.writeJson(json, newFile);
        } catch (Exception ex) {
            ExceptionHandler.catchException(ex, "Couldn't migrate autovote");
        }
    }
}
