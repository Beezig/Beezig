package eu.beezig.core.news;

import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;

import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class NewsParser {
    public static final SimpleDateFormat RSS_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    public static final SimpleDateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US);
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
            parse(conn.getInputStream(), parser);
        } catch (Exception e) {
            ExceptionHandler.catchException(e, "XML parse");
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private void parse(InputStream in, NewsType type) {
        for (RssItemIterator iter = new RssItemIterator(xmlFactory, in, type.getParser()); iter.hasNext(); ) {
            NewsEntry item = iter.next();
            if (item != null) this.entries.compute(type, ($, v) -> {
                if (v == null) v = new TreeSet<>();
                v.add(item);
                return v;
            });
        }
    }

    public Map<NewsType, Set<NewsEntry>> done() {
        return entries;
    }
}
