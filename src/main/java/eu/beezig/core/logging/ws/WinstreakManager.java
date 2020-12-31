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

package eu.beezig.core.logging.ws;

import com.google.gson.reflect.TypeToken;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class WinstreakManager {
    private final File streaksFile;

    public WinstreakManager(File beezigDir) {
        this.streaksFile = new File(beezigDir, "winstreaks.json");
    }

    public WinstreakService getService(String game) {
        Map<String, Map<String, WinstreakService>> data = readFile();
        String uuid = UUIDUtils.strip(Beezig.user().getId());
        if(data == null) return new WinstreakService();
        Map<String, WinstreakService> games = data.get(uuid);
        if(games == null) return new WinstreakService();
        return games.getOrDefault(game, new WinstreakService());
    }

    public void saveService(String game, WinstreakService service) throws IOException {
        Map<String, Map<String, WinstreakService>> data = readFile();
        if(data == null) data = new LinkedHashMap<>();
        String uuid = UUIDUtils.strip(Beezig.user().getId());
        Map<String, WinstreakService> games = data.computeIfAbsent(uuid, $ -> new LinkedHashMap<>());
        games.put(game, service);
        writeFile(data);
    }

    private Map<String, Map<String, WinstreakService>> readFile() {
        try(Reader reader = Files.newBufferedReader(streaksFile.toPath(), Charset.defaultCharset())) {
            return Beezig.gson.fromJson(reader, new TypeToken<LinkedHashMap<String, LinkedHashMap<String, WinstreakService>>>() {}.getType());
        } catch (IOException e) {
            ExceptionHandler.catchException(e);
            return new LinkedHashMap<>();
        }
    }

    private void writeFile(Map<String, Map<String, WinstreakService>> data) throws IOException {
        try(Writer writer = Files.newBufferedWriter(streaksFile.toPath(), Charset.defaultCharset())) {
            Beezig.gson.toJson(data, writer);
        }
    }
}
