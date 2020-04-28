/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeezigConfiguration {
    private HashMap<Settings, Setting> config;

    public void load(File file) {
        if(!file.exists()) {
            config = Stream.of(Settings.values()).map(key -> new HashMap.SimpleEntry<>(key, new Setting(key.getDefaultValue())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, HashMap::new));
            return;
        }
        try(FileReader reader = new FileReader(file)) {
            try(BufferedReader buffer = new BufferedReader(reader)) {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(buffer);
                HashMap<Object, Object> map = (HashMap<Object, Object>)json;
                config = map.entrySet().stream().map(e -> {
                    Settings key = Settings.valueOf(e.getKey().toString());
                    Setting value = new Setting(e.getValue());
                    return new HashMap.SimpleEntry<>(key, value);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, HashMap::new));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Setting get(Settings key) {
        return config.get(key);
    }
}
