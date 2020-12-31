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

package eu.beezig.core.logging.session;

import eu.beezig.core.Beezig;
import eu.beezig.core.logging.TemporaryPointsManager;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class LogRotation {
    private File[] getApplicableFolders() {
        File sessions = new File(Beezig.get().getBeezigDir(), "sessions");
        File[] subs = sessions.listFiles();
        if (subs == null) return new File[0];
        LocalDate expiry = LocalDate.now(ZoneId.systemDefault()).minus(30, ChronoUnit.DAYS);
        return Arrays.stream(subs).filter(f -> {
            if (!f.isDirectory()) return false;
            return LocalDate.parse(f.getName(), TemporaryPointsManager.dateFormatter).isBefore(expiry);
        }).sorted().toArray(File[]::new);
    }

    public void rotateLogs() throws IOException {
        File[] applicable = getApplicableFolders();
        if (applicable.length == 0) return;
        Beezig.logger.info("Rotating " + applicable.length + " session logs.");
        String fileName = applicable[0].getName() + "." + applicable[applicable.length - 1].getName() + ".tar.gz";
        File archive = new File(Beezig.get().getBeezigDir(), "sessions/" + FilenameUtils.getName(fileName));
        try (FileOutputStream os = new FileOutputStream(archive);
             OutputStream gzip = new GzipCompressorOutputStream(os);
             ArchiveOutputStream tarOut = new TarArchiveOutputStream(gzip)) {
            for (File folder : applicable) {
                File[] subs = folder.listFiles();
                if (subs == null) continue;
                for (File file : subs) {
                    if (!file.isFile()) continue;
                    TarArchiveEntry entry = new TarArchiveEntry(file, folder.getName() + "/" + file.getName());
                    entry.setSize(file.length());
                    tarOut.putArchiveEntry(entry);
                    try (FileInputStream is = new FileInputStream(file)) {
                        IOUtils.copy(is, tarOut);
                    }
                    tarOut.closeArchiveEntry();
                }
                FileUtils.deleteDirectory(folder);
            }
        }
    }
}
