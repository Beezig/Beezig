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

package eu.beezig.core.updater;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Updater {


    private static String FETCH_URL;

    public static void setUrl() {
        FETCH_URL = "https://v.beezig.eu/" + (BeezigMain.laby ? "laby" : "beezig") + "/";
    }

    public static boolean checkForUpdates() throws Exception {
        URL url = new URL(FETCH_URL + "latest.txt");
        URLConnection conn = url.openConnection();
        conn.addRequestProperty("User-Agent", Log.getUserAgent());
        Scanner sc = new Scanner(conn.getInputStream());
        boolean tr = sc.nextInt() > BeezigMain.getCustomVersioning();
        sc.close();
        conn.getInputStream().close();
        return tr;
    }

    public static boolean isVersionBlacklisted(int ver) throws IOException {
        URL url = new URL(FETCH_URL + "disabled.txt");

        URLConnection conn = url.openConnection();
        conn.addRequestProperty("User-Agent", Log.getUserAgent());
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String strLine;


        while ((strLine = reader.readLine()) != null) {
            int ver1;
            try {
                ver1 = Integer.parseInt(strLine);
            } catch (Exception e) {
                return false;
            }
            if (ver <= ver1) return true;


        }


        reader.close();
        conn.getInputStream().close();
        return false;
    }


}
