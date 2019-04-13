/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.utils.acr;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Connector {

    public static String reportToken = "";
    public static String cookieStr = "";

    public static void acquireReportToken(String loginReportUrl) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(loginReportUrl).openConnection();
        conn.addRequestProperty("User-Agent", Log.getUserAgent());
        conn.connect(); // Returns 302 if success, 200 if error
        StringBuilder cks = new StringBuilder();
        for (String s : conn.getHeaderFields().get("Set-Cookie")) {
            cks.append(s.split(";")[0]).append("; ");
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
            conn3.addRequestProperty("Accept", "*/*");
            conn3.setRequestProperty("Cookie", cookieStr);
            conn3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn3.setRequestProperty("Host", "report.hivemc.com");
            conn3.setRequestProperty("Referer", "http://report.hivemc.com/");
            conn3.setRequestProperty("Origin", "http://report.hivemc.com");
            conn3.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn3.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn3.setRequestProperty("Accept-Language", "it,it-IT;q=0.8,en-US;q=0.5,en;q=0.3");
            conn3.setRequestMethod("POST");


            String link;
            String uuid;
            System.out.println("Sending for ID: " + chatReportId);
            if (chatReportId.startsWith("https://") || chatReportId.startsWith("http://")) {
                link = chatReportId;
                uuid = UsernameToUuid.getUUID(player);
            } else {
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
            System.out.println(conn3.getResponseCode());
            return conn3.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
