package eu.beezig.core.news;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public enum NewsType {
    BEEZIG(NewsUrls.BEEZIG_NEWS, FileType.RSS, (item, k, v) -> {
        if("updated".equals(k)) {
            try {
                item.setPubDate(NewsParser.ISO_8601.parse(v));
            } catch (ParseException e) {
                ExceptionHandler.catchException(e);
            }
        } else if("id".equals(k)) item.setLink(v);
    }),
    STATUS(NewsUrls.BEEZIG_STATUS, FileType.RSS, (item, k, v) -> {
        if("published".equals(k)) {
            try {
                item.setPubDate(NewsParser.ISO_8601.parse(v));
            } catch (ParseException e) {
                ExceptionHandler.catchException(e);
            }
        }
    }),
    HIVE_MAPS(NewsUrls.LERGIN_MAPS, FileType.JSON, (item, k, v) -> {
        if("author".equals(k)) item.setAuthor(v);
        else if("mapName".equals(k)) item.setTitle(v);
        else if("gameType".equals(k)) item.setExtra("game", v);
        else if("date".equals(k)) item.setPubDate(new Date(Long.parseLong(v, 10)));
        if(item.getContent() == null && item.getAuthor() != null && item.hasExtra("game")) {
            item.setContent(Beezig.api().translate("news.map", Color.primary() + item.getExtra().get("game")
                + Color.accent(), Color.primary() + item.getAuthor()));
        }
    });

    private final FileType type;
    private final String url;
    private final RssItemIterator.ItemTransformer parser;

    NewsType(String url, FileType type, RssItemIterator.ItemTransformer parser) {
        this.parser = parser;
        this.url = url;
        this.type = type;
    }

    public FileType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public RssItemIterator.ItemTransformer getParser() {
        return parser;
    }

    public String translateName() {
        return Message.translate("news.type." + name().toLowerCase(Locale.ROOT));
    }

    public enum FileType {
        JSON, RSS
    }
}
