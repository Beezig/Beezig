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

package eu.beezig.core.settings;

import eu.beezig.core.BeezigMain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsFetcher {

    public static Properties props;

    public static void loadSettings() throws IOException {
        props = new Properties();
        FileInputStream fis = new FileInputStream(BeezigMain.mcFile.getAbsolutePath() + "/settings.properties");
        props.load(fis);
        fis.close();
        for (Object os : props.keySet()) {

            if (os instanceof String) {
                String s = (String) os;
                try {
                    Setting sett = Setting.valueOf(s.toUpperCase());

                    sett.setValueWithoutSaving(Boolean.valueOf(props.getProperty(s)));


                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        System.out.println("Succesfully loaded settings.");
    }

    public static void saveSetting(Setting sett) throws IOException {
        if (props == null) props = new Properties();
        props.setProperty(sett.name().toUpperCase(), sett.getValue() + "");
        FileOutputStream fos = new FileOutputStream(BeezigMain.mcFile.getAbsolutePath() + "/settings.properties");
        props.store(fos, "");

    }

    public static void saveSettings() throws IOException {
        for (Setting s : Setting.values()) {
            saveSetting(s);
        }
        System.out.println("Succesfully saved settings.");
    }


}
