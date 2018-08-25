package tk.roccodev.beezig.utils.acr;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiHiveGlobal;

public class Connector {

    public static String reportToken = "";
    public static String cookieStr = "";

    public static void acquireReportToken(String loginReportUrl) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(loginReportUrl).openConnection();
        conn.addRequestProperty("User-Agent", Log.getUserAgent());
        conn.connect(); // Returns 302 if success, 200 if error
        StringBuilder cks = new StringBuilder();
        for (String s : conn.getHeaderFields().get("Set-Cookie")) {
            cks.append(s.split(";")[0] + "; ");
        }

        URL rep = new URL("https://report.hivemc.com/");
        HttpURLConnection conn2 = (HttpURLConnection) rep.openConnection();
        conn2.addRequestProperty("User-Agent", Log.getUserAgent());
        cookieStr = cks.toString().trim();
        conn2.setRequestProperty("Cookie", cookieStr);

        Scanner tmp = new Scanner(conn2.getInputStream());
        Scanner s = tmp.useDelimiter("\\A");
        String op = s.hasNext() ? s.next() : "";
        s.close();
        reportToken = op.split("\\_token\\:")[1].split("\\\"")[1];
        tmp.close();
        conn2.disconnect();
        conn.disconnect();

    }

    public static boolean sendReport(String chatReportId, String reason, String player) {
        try {
            URL url = new URL("https://report.hivemc.com/ajax/receive");
            HttpURLConnection conn3 = (HttpURLConnection) url.openConnection();
            conn3.addRequestProperty("User-Agent", Log.getUserAgent());
            conn3.setRequestProperty("Cookie", cookieStr);
            conn3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn3.setRequestProperty("Host", "report.hivemc.com");
            conn3.setRequestProperty("Referer", "http://report.hivemc.com/");
            conn3.setRequestProperty("Origin", "http://report.hivemc.com");
            conn3.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn3.setRequestMethod("POST");

            
            String link = null;
            String uuid = null;
            if(chatReportId.startsWith("https://") || chatReportId.startsWith("http://")) {
            	link = chatReportId;
            	uuid = new ApiHiveGlobal(player).getUUID();
            }
            else {
            	String uuidInfo = "http://api.hivemc.com/v1/chatreport/" + chatReportId;
                uuid = (String) APIUtils.getObject(APIUtils.readURL(new URL(uuidInfo))).get("UUID");
                link = "http://hivemc.com/chatlog/" + chatReportId;
            }
            

            String urlParameters = "category=chat&reason=" + reason + "&comment=Sent%20through%20Beezig&evidence="
                    + URLEncoder.encode(link) + "&UUIDs%5B%5D=" + uuid
                    + "&notify=false&_token=" + reportToken;

            // Send post request
            conn3.setDoOutput(true);
            conn3.setFixedLengthStreamingMode(urlParameters.getBytes(Charset.forName("UTF-8")).length);
            conn3.connect();
            try (java.io.OutputStream os = conn3.getOutputStream()) {
                os.write(urlParameters.getBytes(StandardCharsets.UTF_8));
            }

            if (conn3.getResponseCode() == 200)
                return true;
            return false;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }

    }

}
