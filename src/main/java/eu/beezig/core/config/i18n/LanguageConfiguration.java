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

package eu.beezig.core.config.i18n;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LanguageConfiguration {
    private static final Pattern FILE_REGEX = Pattern.compile("language_(\\w{2}_\\w{2})\\.properties");
    public static final LanguageSetting NATIVE = new LanguageSetting("MINECRAFT", null);
    static Map<String, LanguageSetting> languages = Maps.newHashMap(ImmutableMap.of(NATIVE.name(), NATIVE));

    public static void load()  {
        URI lang;
        try {
            lang = Beezig.class.getResource("/lang").toURI();
        } catch (URISyntaxException e) {
            ExceptionHandler.catchException(e, "Couldn't find language bundle");
            return;
        }
        if(!"jar".equals(lang.getScheme())) return; // Dev env
        try(FileSystem fs = FileSystems.newFileSystem(lang, Collections.emptyMap())) {
            Path path = fs.getPath("/lang");
            try(Stream<Path> stream = Files.walk(path)) {
                stream.map(s -> {
                    Matcher m = FILE_REGEX.matcher(s.getFileName().toString());
                    if(!m.matches()) return null;
                    return m.group(1);
                }).filter(Objects::nonNull).map(s -> Locale.forLanguageTag(s.replace("_", "-"))).filter(Objects::nonNull).forEach(locale -> {
                    String enumName = StringUtils.normalizeMapName(locale.getDisplayLanguage(Locale.ENGLISH)).toUpperCase(Locale.ROOT);
                    languages.put(enumName, new LanguageSetting(enumName, locale));
                });
            }
        } catch (IOException e) {
            ExceptionHandler.catchException(e, "Couldn't find language bundle");
        }
    }
}
