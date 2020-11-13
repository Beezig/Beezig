package eu.beezig.core.speedrun;

import com.sun.jna.Native;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import livesplitcore.LiveSplitCoreNative;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SplitLibraryLoader {
    private static final String LIVESPLIT_VERSION = "0.11.0";

    public static boolean loadSpeedrunLibrary(File beezigDir) {
        String nativeDir = new File(beezigDir, "dr/native/").getAbsolutePath();
        String oldProp = System.getProperty("jna.library.path");
        System.setProperty("jna.library.path", nativeDir);
        try {
            Native.loadLibrary("livesplit_core", LiveSplitCoreNative.class);
            System.setProperty("jna.encoding", "UTF-8");
            return true;
        } catch (UnsatisfiedLinkError error) {
            if (Beezig.DEBUG) ExceptionHandler.catchException(error);
            return false;
        } finally {
            if (oldProp != null) System.setProperty("jna.library.path", oldProp);
        }
    }

    private static String buildTarget() {
        String arch = "x86".equals(System.getProperty("os.arch")) ? "i686" : "x86_64";
        String target = SystemUtils.IS_OS_WINDOWS ? "pc-windows-msvc" : (SystemUtils.IS_OS_MAC ? "apple-darwin" : "unknown-linux-gnu");
        return arch + "-" + target;
    }

    public static CompletableFuture<Void> downloadNatives() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        File outDir = new File(Beezig.get().getBeezigDir(), "dr/native");
        if (!outDir.exists()) outDir.mkdirs();
        Beezig.get().getAsyncExecutor().execute(() -> {
            HttpURLConnection conn = null;
            try {
                String noExt = "https://github.com/LiveSplit/livesplit-core/releases/download/v"
                    + LIVESPLIT_VERSION + "/livesplit-core-v" + LIVESPLIT_VERSION + "-" + buildTarget();
                URL url = new URL(noExt + ".tar.gz");
                conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("User-Agent", Message.getUserAgent());
                if (conn.getResponseCode() == 404) {
                    conn.disconnect();
                    conn = (HttpURLConnection) new URL(noExt + ".zip").openConnection();
                    downloadZip(conn.getInputStream(), outDir);
                    future.complete(null);
                    return;
                }
                try (BufferedInputStream buffered = new BufferedInputStream(conn.getInputStream());
                     GzipCompressorInputStream gzip = new GzipCompressorInputStream(buffered);
                     TarArchiveInputStream tar = new TarArchiveInputStream(gzip, 4096)) {
                    TarArchiveEntry entry;
                    while ((entry = (TarArchiveEntry) tar.getNextEntry()) != null) {
                        if (entry.isFile()) {
                            String name = entry.getName();
                            if (name.endsWith(".so") || name.endsWith(".dll") || name.endsWith(".dylib")) {
                                int count;
                                byte[] data = new byte[1024];
                                try (FileOutputStream fos = new FileOutputStream(new File(outDir, FilenameUtils.getName(entry.getName())), false);
                                     BufferedOutputStream dest = new BufferedOutputStream(fos, 1024)) {
                                    while ((count = tar.read(data, 0, 1024)) != -1) {
                                        dest.write(data, 0, count);
                                    }
                                }
                            }
                        }
                    }
                    future.complete(null);
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            } finally {
                if (conn != null) conn.disconnect();
            }
        });
        return future;
    }

    private static void downloadZip(InputStream stream, File outDir) throws IOException {
        try (BufferedInputStream buffered = new BufferedInputStream(stream);
             ZipArchiveInputStream zip = new ZipArchiveInputStream(buffered)) {
            ZipArchiveEntry entry;
            while ((entry = (ZipArchiveEntry) zip.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (name.endsWith(".so") || name.endsWith(".dll") || name.endsWith(".dylib")) {
                        int count;
                        byte[] data = new byte[1024];
                        try (FileOutputStream fos = new FileOutputStream(new File(outDir, FilenameUtils.getName(entry.getName())), false);
                             BufferedOutputStream dest = new BufferedOutputStream(fos, 1024)) {
                            while ((count = zip.read(data, 0, 1024)) != -1) {
                                dest.write(data, 0, count);
                            }
                        }
                    }
                }
            }
        }
    }
}
