/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.util;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtils {
    public static JSONObject readJson(File file) throws IOException, ParseException {
        try(BufferedReader buffer = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(buffer);
        }
    }

    public static String readToString(File file) throws IOException {
        try(FileInputStream in = new FileInputStream(file)) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        }
    }

    public static void writeJson(JSONAware json, File out) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(out.toPath(), StandardCharsets.UTF_8)) {
            writer.write(json.toJSONString());
        }
    }
}
