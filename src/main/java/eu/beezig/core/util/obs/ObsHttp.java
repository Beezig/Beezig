package eu.beezig.core.util.obs;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ObsHttp {
    private static final String BASE = "http://localhost:8085";

    public static HttpRes sendPost(String urlString, String body, String signature, String appId) throws Exception {
        URL url = new URL(BASE + urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.addRequestProperty("X-OBSC-App", appId);
        conn.addRequestProperty("X-OBSC-Signature", signature);
        return readResponse(body, conn);
    }

    private static HttpRes readResponse(String body, HttpURLConnection conn) throws IOException {
        try(OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
        return new HttpRes(conn.getResponseCode(), IOUtils.toString(conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8));
    }

    public static HttpRes sendPost(String urlString, String body) throws Exception {
        URL url = new URL(BASE + urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Content-Type", "application/json");
        return readResponse(body, conn);
    }

    public static HttpRes sendGet(String urlString) throws Exception {
        URL url = new URL(BASE + urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return new HttpRes(conn.getResponseCode(), IOUtils.toString(conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8));
    }

    public static class HttpRes {
        private final int code;
        private final String body;

        public HttpRes(int code, String body) {
            this.code = code;
            this.body = body;
        }

        public int getCode() {
            return code;
        }

        public String getBody() {
            return body;
        }
    }
}
