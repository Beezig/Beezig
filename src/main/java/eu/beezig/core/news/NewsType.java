package eu.beezig.core.news;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;

@SuppressWarnings("ImmutableEnumChecker")
public enum NewsType {
    BEEZIG(NewsUrls.BEEZIG_NEWS, FileType.RSS, (item, k, v) -> {
        if("updated".equals(k)) {
            try {
                item.setPubDate(NewsParser.ISO_8601.get().parse(v));
            } catch (Exception e) {
                ExceptionHandler.catchException(e);
            }
        }
        else if("id".equals(k)) item.setLink(v);
        else if("persistent".equals(k)) item.setPersistent(Boolean.parseBoolean(v));
        else if("versions".equals(k)) {
            Matcher matcher = NewsParser.VERSION_EXCLUSIVITY_REGEX.matcher(StringEscapeUtils.unescapeXml(v));
            if(matcher.matches()) {
                String mode = matcher.group(1);
                ComparableVersion version = new ComparableVersion(matcher.group(2));
                int comparison = NewsParser.VERSION.compareTo(version);
                boolean allowed = false;
                if(mode == null) allowed = comparison == 0;
                else switch (mode) {
                    case ">":
                        allowed = comparison > 0;
                        break;
                    case ">=":
                        allowed = comparison >= 0;
                        break;
                    case "<":
                        allowed = comparison < 0;
                        break;
                    case "<=":
                        allowed = comparison <= 0;
                        break;
                    case "":
                    case "=":
                        allowed = comparison == 0;
                        break;
                }
                item.setVersionAllowed(allowed);
            }
        }
    }),
    STATUS(NewsUrls.BEEZIG_STATUS, FileType.RSS, (item, k, v) -> {
        if("published".equals(k)) {
            try {
                item.setPubDate(NewsParser.ISO_8601.get().parse(v));
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
    }),
    HIVE_FORUMS(NewsUrls.HIVE_NEWS, FileType.RSS, (item, k, v) -> {
        if("pubDate".equals(k)) {
            try {
                item.setPubDate(NewsParser.RSS_DATE_FORMAT.get().parse(v));
            } catch (ParseException e) {
                ExceptionHandler.catchException(e);
            }
        } else if("dc:creator".equals(k)) item.setAuthor(v);
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
