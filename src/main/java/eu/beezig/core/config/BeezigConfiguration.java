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
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeezigConfiguration {
    private HashMap<Settings, Setting> config;
    private File file;

    public void load(File file) throws IOException, ParseException {
        this.file = file;
        if(!file.exists()) {
            config = Stream.of(Settings.values()).map(key -> new HashMap.SimpleEntry<>(key, new Setting(key.getDefaultValue())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, HashMap::new));
            save();
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
        }
    }

    Setting get(Settings key) {
        return config.getOrDefault(key, new Setting(key.getDefaultValue()));
    }

    public void set(Settings key, String newValue) {
        key.get().setValue(castValue(key.getSettingType(), newValue));
    }

    private Object castValue(Class cls, String value) {
        if(cls == Boolean.class) return Boolean.parseBoolean(value);
        if(cls == Integer.class) return Integer.parseInt(value, 10);
        if(cls == Double.class) return Double.parseDouble(value);
        if(cls == Float.class) return Float.parseFloat(value);
        if(cls == Long.class) return Long.parseLong(value, 10);
        else return cls.cast(value);
    }

    public void save() throws IOException {
        JSONObject configJson = new JSONObject();
        for(Map.Entry<Settings, Setting> e : config.entrySet()) {
            configJson.put(e.getKey().name(), e.getValue().getValue());
        }
        try(FileWriter writer = new FileWriter(file)) {
            try(BufferedWriter buffer = new BufferedWriter(writer)) {
                buffer.write(configJson.toJSONString());
            }
        }
    }
}
