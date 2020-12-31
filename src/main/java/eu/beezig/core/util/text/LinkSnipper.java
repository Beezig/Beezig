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

package eu.beezig.core.util.text;

import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkSnipper {
    private static final Pattern URL_REGEX = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)");

    @EventHandler
    public void onChat(ChatEvent event) {
        if(!ServerHive.isCurrent() || !Settings.CHAT_LINKS.get().getBoolean()) return;
        String message = event.getMessage();
        Matcher m = URL_REGEX.matcher(message);
        MessageComponent main = new MessageComponent("");
        int lastEnd = 0;
        try {
            while (m.find()) {
                main.getSiblings().add(new MessageComponent(message.substring(lastEnd, m.start())));
                String link = m.group(0);
                String domain = getDomain(link);
                TextButton btn = new TextButton("Link: " + Color.accent() + domain + Color.primary(), link, Color.primary());
                btn.getStyle().setOnClick(new MessageAction(MessageAction.Action.OPEN_URL, link));
                main.getSiblings().add(btn);
                lastEnd = m.end();
            }
            main.getSiblings().add(new MessageComponent(message.substring(lastEnd)));
        } catch (URISyntaxException e) {
            lastEnd = 0;
            ExceptionHandler.catchException(e, "Couldn't parse URI");
        }
        if (lastEnd != 0) {
            event.setCancelled(true);
            Beezig.api().messagePlayerComponent(main, false);
        }
    }

    private String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String hostname = uri.getHost();
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return null;
    }
}
