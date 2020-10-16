package eu.beezig.core.news;

import eu.beezig.core.util.ExceptionHandler;

import java.text.ParseException;

public enum NewsType {
    BEEZIG(NewsUrls.BEEZIG_NEWS, (item, k, v) -> {
        if("pubDate".equals(k)) {
            try {
                item.setPubDate(NewsParser.RSS_DATE_FORMAT.parse(v));
            } catch (ParseException e) {
                ExceptionHandler.catchException(e);
            }
        }
    }),
    STATUS(NewsUrls.BEEZIG_STATUS, (item, k, v) -> {
        if("published".equals(k)) {
            try {
                item.setPubDate(NewsParser.ISO_8601.parse(v));
            } catch (ParseException e) {
                ExceptionHandler.catchException(e);
            }
        }
    });

    private final String url;
    private final RssItemIterator.ItemTransformer parser;

    NewsType(String url, RssItemIterator.ItemTransformer parser) {
        this.parser = parser;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public RssItemIterator.ItemTransformer getParser() {
        return parser;
    }
}
