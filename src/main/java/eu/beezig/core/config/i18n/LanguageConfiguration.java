package eu.beezig.core.config.i18n;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eu.beezig.core.Beezig;
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

public class LanguageConfiguration {
    private static final Pattern FILE_REGEX = Pattern.compile("language_(\\w{2}_\\w{2})\\.properties");
    public static final LanguageSetting NATIVE = new LanguageSetting("MINECRAFT", null);
    static Map<String, LanguageSetting> languages = Maps.newHashMap(ImmutableMap.of(NATIVE.name(), NATIVE));

    public static void load()  {
        URI lang;
        try {
            lang = Beezig.class.getResource("/lang").toURI();
        } catch (URISyntaxException e) {
            Beezig.logger.error("Couldn't find language bundle", e);
            return;
        }
        if(!"jar".equals(lang.getScheme())) return; // Dev env
        try(FileSystem fs = FileSystems.newFileSystem(lang, Collections.emptyMap())) {
            Path path = fs.getPath("/lang");
            Files.walk(path).map(s -> {
                Matcher m = FILE_REGEX.matcher(s.getFileName().toString());
                if(!m.matches()) return null;
                return m.group(1);
            }).filter(Objects::nonNull).map(s -> Locale.forLanguageTag(s.replace("_", "-"))).filter(Objects::nonNull).forEach(locale -> {
                String enumName = StringUtils.normalizeMapName(locale.getDisplayLanguage(Locale.ENGLISH)).toUpperCase(Locale.ROOT);
                languages.put(enumName, new LanguageSetting(enumName, locale));
            });
        } catch (IOException e) {
            Beezig.logger.error("Couldn't find language bundle", e);
        }
    }
}
