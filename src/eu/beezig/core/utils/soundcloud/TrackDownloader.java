package eu.beezig.core.utils.soundcloud;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TrackDownloader {

    public static BufferedInputStream trackStream(String trackId) throws IOException {
        URL trackUrl = new URL("http://api.soundcloud.com/tracks/" + trackId + "/stream?client_id=b91f180fff0ec5cd1d4b0dcab8d59967");
        HttpURLConnection conn = (HttpURLConnection) trackUrl.openConnection();
        Map<String, List<String>> map = conn.getHeaderFields();

        String redirect = map.get("Location").get(0);
        URL newUrl = new URL(redirect);

        return new BufferedInputStream(newUrl.openStream());

    }


}
