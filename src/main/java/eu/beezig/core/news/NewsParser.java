package eu.beezig.core.news;

import eu.beezig.core.Constants;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.stream.XMLInputFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class NewsParser {
    public static final ThreadLocal<SimpleDateFormat> RSS_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US));
    public static final ThreadLocal<SimpleDateFormat> ISO_8601 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US));
    public static final Pattern VERSION_EXCLUSIVITY_REGEX = Pattern.compile("([><]=?)?(.+)");
    public static final ComparableVersion VERSION = new ComparableVersion(Constants.VERSION);
    private final XMLInputFactory xmlFactory;
    private final Map<NewsType, Set<NewsEntry>> entries = new EnumMap<>(NewsType.class);

    public NewsParser() {
        xmlFactory = XMLInputFactory.newInstance();
        xmlFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
    }

    public void download(NewsType parser) {
        URL url;
        try {
            url = new URL(parser.getUrl());
        } catch (MalformedURLException e) {
            ExceptionHandler.catchException(e);
            return;
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent", Message.getUserAgent());
            if(conn.getResponseCode() != 200) throw new RuntimeException("HTTP error while fetching news");
            parse(conn.getInputStream(), parser);
        } catch (Exception e) {
            ExceptionHandler.catchException(e, "XML parse");
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private void parse(InputStream in, NewsType type) {
        if(type.getType() == NewsType.FileType.RSS) {
            for (RssItemIterator iter = new RssItemIterator(xmlFactory, in, type.getParser()); iter.hasNext(); ) {
                NewsEntry item = iter.next();
                if (item != null && item.isVersionAllowed()) {
                    this.entries.compute(type, ($, v) -> {
                        if (v == null) v = new TreeSet<>();
                        v.add(item);
                        return v;
                    });
                }
            }
        } else if(type.getType() == NewsType.FileType.JSON) {
            RssItemIterator.ItemTransformer transformer = type.getParser();
            JSONParser parser = new JSONParser();
            try(InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                JSONArray array = (JSONArray) parser.parse(reader);
                for(Object o : array) {
                    JSONObject json = (JSONObject) o;
                    NewsEntry entry = new NewsEntry();
                    for(Object o1 : json.entrySet()) {
                        Map.Entry<String, Object> item = (Map.Entry<String, Object>) o1;
                        if(transformer != null) transformer.transformItem(entry, item.getKey(), item.getValue().toString());
                    }
                    if(entry.isVersionAllowed()) {
                        this.entries.compute(type, ($, v) -> {
                            if (v == null) v = new TreeSet<>();
                            v.add(entry);
                            return v;
                        });
                    }
                }
            } catch (IOException | ParseException e) {
                ExceptionHandler.catchException(e, "JSON parse");
            }
        }
    }

    public Map<NewsType, Set<NewsEntry>> done() {
        return entries;
    }
}
