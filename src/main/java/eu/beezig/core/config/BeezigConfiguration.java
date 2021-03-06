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

package eu.beezig.core.config;

import eu.beezig.core.Beezig;
import eu.beezig.core.api.SettingInfo;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeezigConfiguration {
    private HashMap<Settings, Setting> config;
    private File file;

    public void load(File file) throws IOException, ParseException {
        this.file = file;
        if (!file.exists()) {
            config = Stream.of(Settings.values()).map(key -> new AbstractMap.SimpleEntry<>(key, new Setting(key.getDefaultValue())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, HashMap::new));
            save();
            Color.refreshCache();
            return;
        }
        try (BufferedReader buffer = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(buffer);
            HashMap<Object, Object> map = (HashMap<Object, Object>) json;
            config = map.entrySet().stream().map(e -> {
                try {
                    Settings key = Settings.valueOf(e.getKey().toString());
                    Setting value = new Setting(castValue(key.getSettingType(), (String) e.getValue()));
                    return new AbstractMap.SimpleEntry<>(key, value);
                } catch (Exception ex) {
                    ExceptionHandler.catchException(ex);
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, HashMap::new));
        }
        Color.refreshCache();
    }

    Setting get(Settings key) {
        return config.getOrDefault(key, new Setting(key.getDefaultValue()));
    }

    private Setting getOrPutDefault(Settings key) {
        Setting value = config.get(key);
        if (value == null) {
            Setting def = new Setting(key.getDefaultValue());
            config.put(key, def);
            return def;
        }
        return value;
    }

    public void setAsIs(Settings key, Object newValue) {
        getOrPutDefault(key).setValue(newValue);
        onSettingsChange(key);
    }

    public boolean set(Settings key, String newValue) {
        try {
            Object casted = castValue(key.getSettingType(), newValue);
            if (casted == null) return false;
            getOrPutDefault(key).setValue(casted);
            onSettingsChange(key);
            return true;
        } catch (Exception e) {
            ExceptionHandler.catchException(e);
        }
        return false;
    }

    private Object castValue(Class cls, String value) throws Exception {
        if (cls == Boolean.class) {
            if("on".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)) return true;
            if("off".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value)) return false;
            return Boolean.parseBoolean(value);
        }
        if (cls == Integer.class) return Integer.parseInt(value, 10);
        if (cls == Double.class) return Double.parseDouble(value);
        if (cls == Float.class) return Float.parseFloat(value);
        if (cls == Long.class) return Long.parseLong(value, 10);
        if (Enum.class.isAssignableFrom(cls) || EnumSetting.class.isAssignableFrom(cls)) {
            try {
                Object res = cls.getMethod("valueOf", String.class).invoke(null, value.toUpperCase(Locale.ROOT));
                if(res == null) throw new RuntimeException(new IllegalArgumentException()); // For custom enums
                return res;
            } catch (Exception e) {
                ExceptionHandler.catchException(e);
                if (!(e.getCause() instanceof IllegalArgumentException)) return null;

                Message.error(Beezig.api().translate("error.enum", "§6" + getEnumValues(cls)));
            }
            return null;
        } else return cls.cast(value);
    }

    public String getEnumValues(Class settingClass) throws ReflectiveOperationException {
        Object[] valuesRaw = (Object[]) settingClass.getMethod("values").invoke(null);
        Method name = valuesRaw[0].getClass().getMethod("name");
        return Stream.of(valuesRaw).map(o -> {
            try {
                return (String) name.invoke(o);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }).collect(Collectors.joining(", "));
    }

    public void save() throws IOException {
        JSONObject configJson = new JSONObject();
        for (Map.Entry<Settings, Setting> e : config.entrySet()) {
            configJson.put(e.getKey().name(), e.getValue().toString());
        }
        try (BufferedWriter buffer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            buffer.write(configJson.toJSONString());
        }
    }

    private void onSettingsChange(Settings key) {
        if(key == Settings.COLOR_ACCENT || key == Settings.COLOR_PRIMARY) Color.refreshCache();
        if(key == Settings.ADVREC_MODE) {
            if(ServerHive.isCurrent()) {
                GameMode mode = Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
                if(mode instanceof HiveMode) ((HiveMode) mode).getAdvancedRecords().refreshMode();
            }
        }
        if(key == Settings.LANGUAGE) {
            if(!Beezig.get().isLaby()) {
                Message.error(Message.translate("error.setting.language.platform"));
                return;
            }
            Message.info(Message.translate("msg.setting.language.restart"));
        }
    }

    public Map<String, List<SettingInfo>> toForge() {
        Map<String, List<SettingInfo>> result = new LinkedHashMap<>();
        for(Settings setting : Settings.values()) {
            if(setting.getCategory() == Settings.Category.HIDDEN) continue;
            SettingInfo info = new SettingInfo();
            info.key = setting.name();
            info.name = setting.getName();
            info.desc = setting.getDescription();
            info.value = setting.get().getValue();
            result.compute(setting.getCategory().getName(), (k, v) -> {
               if(v == null) v = new ArrayList<>();
               v.add(info);
               return v;
            });
        }
        return result;
    }
}
