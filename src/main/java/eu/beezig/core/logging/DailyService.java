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

package eu.beezig.core.logging;

import eu.beezig.core.Beezig;
import eu.beezig.core.net.packets.PacketDailyGame;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.hiveapi.wrapper.exception.ProfileNotFoundException;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DailyService {
    private int points;
    private File file;
    private String mode;
    private int place = -1;

    public DailyService(String id, File f) {
        this.mode = id;
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
        try {
            this.points = Integer.parseInt(contents, 10);
        } catch (Exception ex) {
            ExceptionHandler.catchException(ex, "Couldn't load daily points");
        }
        Downloader.getJsonObject(new URL((Beezig.DEBUG ? "http://localhost:8726" : "https://web.beezig.eu") + "/v1/dailies/profile/" + mode + "/" + UUIDUtils.strip(Beezig.user().getId())))
            .thenAcceptAsync(json -> place = (int) json.getLong("place")).exceptionally(e -> {
                if(!(e instanceof ProfileNotFoundException)) ExceptionHandler.catchException(e, "Daily profile load");
                return null;
        });
    }

    public int getPlace() {
        return place;
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

    public void submitGamePoints(int points, String gameId) {
        if(gameId != null) {
            Beezig.get().getAsyncExecutor()
                    .execute(() -> Beezig.get().getNetworkManager().getHandler()
                            .sendPacket(new PacketDailyGame(mode, Long.parseLong(gameId), points)));
        }
    }
}
