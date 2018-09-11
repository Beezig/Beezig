package org.scijava.nativelib;

import java.io.*;

public interface JniExtractor
{
    File extractJni(final String p0, final String p1) throws IOException;
    
    void extractRegistered() throws IOException;
}
