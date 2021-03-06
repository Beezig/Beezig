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

package eu.beezig.core.data;

import com.google.common.reflect.TypeToken;
import eu.beezig.core.Beezig;
import eu.beezig.core.automessage.AutoGGManager;
import eu.beezig.core.automessage.AutoGLManager;
import eu.beezig.core.automessage.AutoNewGameManager;
import eu.beezig.core.data.timv.TestMessagesManager;
import eu.beezig.core.util.FileUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import eu.beezig.hiveapi.wrapper.utils.json.JObject;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A manager for Beezig's "data" folder.
 * Upon load, the client attempts to download the "Beezig/hive-data" repository, if the latest commit hash
 * doesn't match the one in the local manifest.
 */
public class BeezigData {
    private File dataFolder;
    private GameTitles titleManager;
    private TestMessagesManager customTestMessages;
    private AutoGGManager autoGGManager;
    private AutoGLManager autoGLManager;
    private AutoNewGameManager autoNewGameManager;

    public BeezigData(File beezigDir) {
        this.dataFolder = new File(beezigDir, "data");
        this.titleManager = new GameTitles(dataFolder);
        this.customTestMessages = new TestMessagesManager();
        Beezig.api().getPluginManager().registerListener(Beezig.get(), customTestMessages);
        this.autoGGManager = new AutoGGManager();
        Beezig.api().getPluginManager().registerListener(Beezig.get(), autoGGManager);
        this.autoGLManager = new AutoGLManager();
        Beezig.api().getPluginManager().registerListener(Beezig.get(), autoGLManager);
        this.autoNewGameManager = new AutoNewGameManager();
        Beezig.api().getPluginManager().registerListener(Beezig.get(), autoNewGameManager);
    }

    public GameTitles getTitleManager() {
        return titleManager;
    }

    public TestMessagesManager getCustomTestMessages() {
        return customTestMessages;
    }

    public AutoGGManager getAutoGGManager() {
        return autoGGManager;
    }

    public AutoGLManager getAutoGLManager() {
        return autoGLManager;
    }

    public AutoNewGameManager getAutoNewGameManager() {
        return autoNewGameManager;
    }

    public <T> T getData(DataPath path, Class<T> marker) throws IOException {
        File f = new File(dataFolder, "hive-data-master/" + path.getPath());
        if(!f.exists()) return null;
        String json = FileUtils.readToString(f);
        return Beezig.gson.fromJson(json, marker);
    }

    public <T> List<T> getDataList(DataPath path, Class<T[]> marker) throws IOException {
        File f = new File(dataFolder, "hive-data-master/" + path.getPath());
        if(!f.exists()) return null;
        String json = FileUtils.readToString(f);
        T[] arr = Beezig.gson.fromJson(json, marker);
        return Arrays.asList(arr);
    }

    public <T> Map<String, T> getDataMap(DataPath path, TypeToken marker) throws IOException {
        File f = new File(dataFolder, "hive-data-master/" + path.getPath());
        if(!f.exists()) return null;
        String json = FileUtils.readToString(f);
        return Beezig.gson.fromJson(json, marker.getType());
    }

    public void tryUpdate(int timeout) {
        CompletableFuture<Void> update = new CompletableFuture<>();
        CompletableFuture<Void> timeoutFuture = new CompletableFuture<>();
        Beezig.get().getAsyncExecutor().execute(() -> {
            try {
                Thread.sleep(timeout * 1000);
                timeoutFuture.completeExceptionally(new InterruptedException("Update took too long"));
            } catch (InterruptedException ignored) {
                Beezig.logger.debug("Interrupting timeout task");
            }
            timeoutFuture.complete(null);
        });
        Beezig.get().getAsyncExecutor().execute(() -> {
            try {
                tryUpdate();
                update.complete(null);
            } catch (Exception e) {
                update.completeExceptionally(e);
            }
        });
        CompletableFuture.anyOf(update, timeoutFuture).join();
    }

    private void tryUpdate() throws Exception {
        Beezig.logger.info("Checking for data updates...");
        String updateSha;
        if((updateSha = checkForUpdates()) == null) {
            Beezig.logger.info("No data update found.");
            return;
        }
        installUpdate(updateSha);
    }

    private String checkForUpdates() throws IOException, ParseException {
        File manifest = new File(dataFolder, "manifest.json");
        JObject remote = Downloader.getJsonObject(new URL(DataUrls.LATEST_COMMIT)).join();
        String newSha = remote.getJObject("commit").getString("id");
        if(!manifest.exists()) {
            manifest.createNewFile();
            return newSha;
        }
        JSONObject json = FileUtils.readJson(manifest);
        String oldSha = (String) json.get("sha");
        if(!oldSha.equals(newSha)) return newSha;
        return null;
    }

    private void installUpdate(String newSha) throws Exception {
        Beezig.logger.info("Downloading data update...");
        URL url = new URL(DataUrls.DOWNLOAD);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("User-Agent", Message.getUserAgent());
        conn.addRequestProperty("Accept", "application/zip");
        File[] oldFiles = dataFolder.listFiles((file, name) -> file.isDirectory() && name.startsWith("hive-data-master"));
        if(oldFiles != null) {
            for(File file : oldFiles) org.apache.commons.io.FileUtils.deleteDirectory(file);
        }
        try(ZipInputStream zip = new ZipInputStream(conn.getInputStream())) {
            uncompressRepo(zip, dataFolder);
        }
        JSONObject manifest = new JSONObject();
        manifest.put("sha", newSha);
        File[] files = dataFolder.listFiles((file, name) -> file.isDirectory() && name.startsWith("hive-data-master"));
        if(files == null || files.length < 1) throw new IOException("Output dir not found");
        org.apache.commons.io.FileUtils.moveDirectory(files[0], new File(dataFolder, "hive-data-master"));
        FileUtils.writeJson(manifest, new File(dataFolder, "manifest.json"));
        titleManager.downloadUpdate();
        Beezig.logger.info("Data updated successfully.");
    }

    private void uncompressRepo(ZipInputStream in, File out) throws IOException {
        ZipEntry entry = in.getNextEntry();
        while(entry != null) {
            validatePath(entry, out);
            File dest = new File(out, FilenameUtils.normalize(entry.getName()));
            if(entry.isDirectory()) dest.mkdirs();
            else {
                dest.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(dest)) {
                    for(int b = in.read(); b != -1; b = in.read()) {
                        fos.write(b);
                    }
                }
            }
            entry = in.getNextEntry();
        }
        in.closeEntry();
    }

    // https://snyk.io/research/zip-slip-vulnerability
    private void validatePath(ZipEntry entry, File out) throws IOException {
        String canonicalDestinationDirPath = out.getCanonicalPath();
        File destinationFile = new File(out, FilenameUtils.normalize(entry.getName()));
        String canonicalDestinationFile = destinationFile.getCanonicalPath();
        if (!canonicalDestinationFile.startsWith(canonicalDestinationDirPath + File.separator)) {
            throw new RuntimeException("Entry is outside of the target dir: " + entry.getName());
        }
    }
}
