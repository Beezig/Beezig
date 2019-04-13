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

package eu.beezig.core.autovote;

import eu.beezig.core.BeezigMain;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AutovoteUtils {


    private static Yaml yml = new Yaml();
    private static HashMap<String, Object> ymlObject;


    public static List<String> getMapsForMode(String mode) {
        List<String> tr = new ArrayList<>();
        if (get(mode) == null)
            return tr;

        tr.addAll(((List<String>) get(mode)).stream().map(s -> s.replaceAll("\\^c\\^", ":")).collect(Collectors.toList()));
        return tr;
    }


    public static void load() {
        InputStream input = null;
        try {
            input = new FileInputStream(new File(BeezigMain.mcFile + "/autovote.yml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ymlObject = (HashMap<String, Object>) yml.load(input);
        if (ymlObject == null) {
            ymlObject = new HashMap<>();
            dump();
        }


        try {
            input.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void set(String key, Object value) {
        if (value instanceof Collection) value = ((Collection<?>) value).stream().map(o -> {
            if (!(o instanceof String)) return o;

            return ((String) o).replaceAll(":", "\\^c\\^");

        }).collect(Collectors.toList());


        ymlObject.put(key, value);
    }

    public static Object get(String key) {
        if (!ymlObject.containsKey(key)) return new ArrayList<String>();
        return ymlObject.get(key);
    }


    public static void dump() {
        FileWriter wr = null;
        try {
            wr = new FileWriter(new File(BeezigMain.mcFile + "/autovote.yml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        yml.dump(ymlObject, wr);


        try {
            wr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
