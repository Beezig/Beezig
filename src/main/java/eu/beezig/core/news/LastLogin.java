package eu.beezig.core.news;

import eu.beezig.core.Beezig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public class LastLogin {
    private Date lastLogin;
    private File file;

    LastLogin() {
        file = new File(Beezig.get().getBeezigDir(), "lastlogin.txt"); // Use the old one from Beezig 6
    }

    public void read() throws IOException {
        if(!file.exists()) {
            lastLogin = new Date(0);
            return;
        }
        String contents = FileUtils.readFileToString(file, Charset.defaultCharset());
        long time = Long.parseLong(contents, 10);
        lastLogin = new Date(time);
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void update() throws IOException {
        FileUtils.write(file, Long.toString(System.currentTimeMillis(), 10), Charset.defaultCharset(), false);
    }
}
