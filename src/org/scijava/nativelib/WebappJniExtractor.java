package org.scijava.nativelib;

import java.io.*;

public class WebappJniExtractor extends BaseJniExtractor
{
    private File nativeDir;
    private File jniSubDir;
    
    public WebappJniExtractor(final String classloaderName) throws IOException {
        (this.nativeDir = new File(System.getProperty("java.library.tmpdir", "tmplib"))).mkdirs();
        if (!this.nativeDir.isDirectory()) {
            throw new IOException("Unable to create native library working directory " + this.nativeDir);
        }
        final long now = System.currentTimeMillis();
        int attempt = 0;
        while (true) {
            final File trialJniSubDir = new File(this.nativeDir, classloaderName + "." + now + "." + attempt);
            if (trialJniSubDir.mkdir()) {
                (this.jniSubDir = trialJniSubDir).deleteOnExit();
                return;
            }
            if (!trialJniSubDir.exists()) {
                throw new IOException("Unable to create native library working directory " + trialJniSubDir);
            }
            ++attempt;
        }
    }
    
    protected void finalize() throws Throwable {
        final File[] files = this.jniSubDir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            files[i].delete();
        }
        this.jniSubDir.delete();
    }
    
    public File getJniDir() {
        return this.jniSubDir;
    }
    
    public File getNativeDir() {
        return this.nativeDir;
    }
}
