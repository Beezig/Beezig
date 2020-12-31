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

package eu.beezig.core.news;

import eu.beezig.core.util.ExceptionHandler;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Adapted from https://github.com/w3stling/rssreader/blob/8199a7b7aa75b872f587f5c4041c6298c08854c5/src/main/java/com/apptastic/rssreader/RssReader.java
public class RssItemIterator implements Iterator<NewsEntry> {
    private final InputStream is;
    private XMLStreamReader reader;
    private NewsEntry item = null;
    private NewsEntry nextItem;
    private boolean isChannelPart = true;
    private String elementName = null;
    private final StringBuilder textBuilder;
    private final ItemTransformer transformer;

    public RssItemIterator(XMLInputFactory factory, InputStream is, ItemTransformer transformer) {
        this.is = is;
        this.transformer = transformer;
        nextItem = null;
        textBuilder = new StringBuilder();
        try {
            reader = factory.createXMLStreamReader(is);
        }
        catch (XMLStreamException e) {
            ExceptionHandler.catchException(e, "XML process");
        }
    }

    private void peekNext() {
        if (nextItem == null) {
            try {
                nextItem = next();
            }
            catch (NoSuchElementException e) {
                nextItem = null;
            }
        }
    }

    @Override
    public boolean hasNext() {
        peekNext();
        return nextItem != null;
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public NewsEntry next() {
        if (nextItem != null) {
            NewsEntry next = nextItem;
            nextItem = null;
            return next;
        }

        try {
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.CHARACTERS || type == XMLStreamConstants.CDATA) {
                    parseCharacters();
                }
                else if (type == XMLStreamConstants.START_ELEMENT) {
                    parseStartElement();
                    parseAttributes();
                }
                else if (type == XMLStreamConstants.END_ELEMENT) {
                    boolean itemParsed = parseEndElement();

                    if (itemParsed)
                        return item;
                }
            }
        } catch (XMLStreamException e) {
            ExceptionHandler.catchException(e, "XML parse");
        }

        try {
            reader.close();
            is.close();
        } catch (XMLStreamException | IOException e) {
            ExceptionHandler.catchException(e, "XML close");
        }

        throw new NoSuchElementException();
    }

    private void parseStartElement() {
        textBuilder.setLength(0);
        elementName = reader.getLocalName();

        if ("channel".equals(elementName) || "feed".equals(elementName)) {
            isChannelPart = true;
        }
        else if ("item".equals(elementName) || "entry".equals(elementName)) {
            item = new NewsEntry();
            isChannelPart = false;
        }
    }

    private void parseAttributes() {
        if ("link".equals(reader.getLocalName())) {
            String rel = reader.getAttributeValue(null, "rel");
            String link = reader.getAttributeValue(null, "href");
            boolean isAlternate = "alternate".equals(rel);

            if (link != null && isAlternate) {
                if (!isChannelPart) item.setLink(link);
            }
        }
    }

    private boolean parseEndElement() {
        String name = reader.getLocalName();
        String text = textBuilder.toString().trim();
        if (!isChannelPart) parseItemCharacters(elementName, item, text);
        textBuilder.setLength(0);
        return "item".equals(name) || "entry".equals(name);
    }

    private void parseCharacters() {
        String text = reader.getText();

        if (text.trim().isEmpty())
            return;

        textBuilder.append(text);
    }

    private void parseItemCharacters(String elementName, NewsEntry item, String text) {
        if (text.isEmpty())
            return;
        if ("title".equals(elementName))
            item.setTitle(text);
        else if ("description".equals(elementName) || "summary".equals(elementName) || "content".equals(elementName) || "content:encoded".equals(elementName)) {
            String escapedContent = StringEscapeUtils.unescapeHtml4(text).replaceAll("<.*?>", "").replace("\u200b", "");
            item.setContent(escapedContent.replace("\r\n", "\n"));
        }
        else if ("link".equals(elementName))
            item.setLink(text);
        else if ("author".equals(elementName))
            item.setAuthor(text);
        if(transformer != null) transformer.transformItem(item, elementName, text);
    }

    public interface ItemTransformer {
        void transformItem(NewsEntry entry, String key, String value);
    }
}
