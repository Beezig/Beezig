package eu.beezig.core.speedrun;

import com.sun.jna.Native;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import livesplitcore.LiveSplitCoreNative;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
                URL url = new URL("https://github.com/LiveSplit/livesplit-core/releases/download/v"
                    + LIVESPLIT_VERSION + "/livesplit-core-v" + LIVESPLIT_VERSION + "-" + buildTarget() + ".tar.gz");
                conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("User-Agent", Message.getUserAgent());
                try (GzipCompressorInputStream gzip = new GzipCompressorInputStream(conn.getInputStream());
                     TarArchiveInputStream tar = new TarArchiveInputStream(gzip)) {
                    TarArchiveEntry entry;
                    while ((entry = (TarArchiveEntry) tar.getNextEntry()) != null) {
                        if (!entry.isDirectory()) {
                            String name = entry.getName();
                            if (name.endsWith(".so") || name.endsWith(".dll") || name.endsWith(".dylib")) {
                                int count;
                                byte[] data = new byte[4096];
                                FileOutputStream fos = new FileOutputStream(new File(outDir, entry.getName()), false);
                                try (BufferedOutputStream dest = new BufferedOutputStream(fos, 4096)) {
                                    while ((count = tar.read(data, 0, 4096)) != -1) {
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
}
