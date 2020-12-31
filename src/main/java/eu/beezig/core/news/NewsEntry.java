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

/*
 * This file is part of BeezigForge.
 *
 * BeezigForge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BeezigForge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BeezigForge.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.news;

import eu.beezig.core.util.DateUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewsEntry implements Comparable<NewsEntry> {
    private String author, title;
    private Date pubDate;
    private String content, link;
    private Map<String, Object> extra;
    private boolean persistent, isVersionAllowed = true;

    public ForgeNewsEntry toForge() {
        ForgeNewsEntry forge = new ForgeNewsEntry();
        forge.author = author;
        forge.title = title;
        forge.pubDate = pubDate;
        forge.content = content;
        forge.link = link;
        forge.extra = extra;
        forge.persistent = persistent;
        return forge;
    }

    public boolean isVersionAllowed() {
        return isVersionAllowed;
    }

    public void setVersionAllowed(boolean versionAllowed) {
        isVersionAllowed = versionAllowed;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(String key, Object value) {
        if(extra == null) extra = new HashMap<>();
        extra.put(key, value);
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean hasExtra(String key) {
        return extra != null && extra.containsKey(key);
    }

    @Override
    public int compareTo(NewsEntry newsEntry) {
        if(newsEntry == null) return 1;
        if(persistent && newsEntry.persistent) return DateUtils.instantCompare(pubDate, newsEntry.pubDate);
        if(persistent) return 1;
        if(newsEntry.persistent) return -1;
        return DateUtils.instantCompare(pubDate, newsEntry.pubDate);
    }

    public static Comparator<ForgeNewsEntry> compareForge() {
        return (o1, o2) -> {
            if(o1.persistent && o2.persistent) return DateUtils.instantCompare(o1.pubDate, o2.pubDate);
            if(o1.persistent) return 1;
            if(o2.persistent) return -1;
            return DateUtils.instantCompare(o1.pubDate, o2.pubDate);
        };
    }
}
