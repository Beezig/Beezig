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
        Map<String, WinstreakService> games = data.get(uuid);
        if(games == null) return new WinstreakService();
        return games.getOrDefault(game, new WinstreakService());
    }

    public void saveService(String game, WinstreakService service) throws IOException {
        Map<String, Map<String, WinstreakService>> data = readFile();
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
