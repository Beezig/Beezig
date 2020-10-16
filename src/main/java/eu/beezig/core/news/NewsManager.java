package eu.beezig.core.news;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class NewsManager {
    private final Map<NewsType, Set<NewsEntry>> loadedNews;

    public NewsManager() {
        NewsParser parser = new NewsParser();
        parser.download(NewsType.STATUS);
        this.loadedNews = parser.done();
    }

    public void sendUpdates(Date lastLogin) {
        Map<NewsType, Set<NewsEntry>> unread = getUnreadNews(lastLogin);
        boolean hasUpdates = unread.entrySet().stream().anyMatch(e -> !e.getValue().isEmpty());
        if(!hasUpdates) return;
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.accent() + "§l" + Message.translate("news.title")));
        WorldTask.submit(() -> {
            for (Map.Entry<NewsType, Set<NewsEntry>> entry : unread.entrySet()) {
                if (entry.getValue().isEmpty()) continue;
                Beezig.api().messagePlayer(StringUtils.centerWithSpaces("§l" + entry.getKey().translateName()));
                for (NewsEntry news : entry.getValue()) {
                    Beezig.api().messagePlayer(StringUtils.centerWithSpaces(Color.primary() + "§l" + news.getTitle()));
                    Beezig.api().messagePlayer(Color.accent() + news.getContent());
                }
            }
        });
    }

    public Map<NewsType, Set<NewsEntry>> getLoadedNews() {
        return loadedNews;
    }

    public Map<NewsType, Set<NewsEntry>> getUnreadNews(Date compareTo) {
        return loadedNews.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
            TreeSet<NewsEntry> entries = new TreeSet<>(e.getValue());
            for(Iterator<NewsEntry> iter = entries.descendingIterator(); iter.hasNext();) {
                NewsEntry entry = iter.next();
                if(entry.getPubDate().compareTo(compareTo) < 0) {
                    iter.remove();
                    entries.headSet(entry).clear();
                    break;
                }
            }
            return entries;
        }));
    }
}
