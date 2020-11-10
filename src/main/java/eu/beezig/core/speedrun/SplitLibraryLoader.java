package eu.beezig.core.speedrun;

import com.sun.jna.Native;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import livesplitcore.LiveSplitCoreNative;

import java.io.File;

public class SplitLibraryLoader {
    public static boolean loadSpeedrunLibrary(File beezigDir) {
        String nativeDir = new File(beezigDir, "dr/native/").getAbsolutePath();
        String oldProp = System.getProperty("jna.library.path");
        System.setProperty("jna.library.path", nativeDir);
        try {
            Native.loadLibrary("livesplit_core", LiveSplitCoreNative.class);
            return true;
        } catch (UnsatisfiedLinkError error) {
            if(Beezig.DEBUG) ExceptionHandler.catchException(error);
            return false;
        } finally {
            if(oldProp != null) System.setProperty("jna.library.path", oldProp);
        }
    }
}
