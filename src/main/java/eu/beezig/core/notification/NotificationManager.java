/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.notification;

import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.server.IPatternResult;
import org.lwjgl.opengl.Display;

import java.util.LinkedList;
import java.util.List;

public class NotificationManager {
    private List<IncomingMessage> ignoredMessages = new LinkedList<>();
    private boolean doNotDisturb;
    private SystemTrayManager tray;

    public NotificationManager() {
        this.tray = new SystemTrayManager();
    }

    public void onMatch(String key, IPatternResult match) {
        if("msg.private".equals(key)) send(MessageType.PRIVATE, match);
        if ("msg.broadcast".equals(key)) send(MessageType.BROADCAST, match);
    }

    private void send(MessageType type, IPatternResult match) {
        String sender = match.get(0);
        String target = match.get(1);
        String message = match.get(2);
        boolean isSenderSelf = sender.equalsIgnoreCase(Beezig.user().getName());
        if(!doNotDisturb && !isSenderSelf && Settings.MSG_PING.get().getBoolean() && !Display.isActive()) {
            tray.sendNotification(new IncomingMessage(type, sender, message));
        }
        if(doNotDisturb) {
            // If target contains a space, the message is a broadcast (PRIVATE| RoccoDev -> 7 friends)
            if(isSenderSelf && !target.contains(" ")) {
                match.ignoreMessage(true);
                return;
            }
            else if(isSenderSelf) return;
            switch((MessageIgnoreLevel) Settings.MSG_DND_MODE.get().getValue()) {
                case IGNORE:
                    ignoredMessages.add(new IncomingMessage(type, sender, message));
                    break;
                case IGNORE_ALERT:
                    ignoredMessages.add(new IncomingMessage(type, sender, message));
                    Beezig.api().sendPlayerMessage("/r " + Settings.MSG_DND_ALERT.get().getString());
                    break;
                case SEPARATE:
                    break;
            }
            match.ignoreMessage(true);
        }
    }

    public void printCachedMessages() {
        if(ignoredMessages.size() == 0) return;
        Message.info(Beezig.api().translate("msg.notify.list",
                Color.accent() + Message.formatNumber(ignoredMessages.size()) + Color.primary()));
        while(!ignoredMessages.isEmpty()) {
            IncomingMessage msg = ignoredMessages.remove(0);
            Beezig.api().messagePlayer(Color.primary() + " - " + Beezig.api().translate(msg.getType() == MessageType.PRIVATE
                            ? "msg.notify.private" : "msg.notify.broadcast",
                    Color.accent() + msg.getSender() + Color.primary(),
                    Color.accent() + Message.date(msg.getSendDate()) + Color.primary(), Color.accent() + msg.getMessage()));
        }
    }

    public void setDoNotDisturb(boolean doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }

    public boolean isDoNotDisturb() {
        return doNotDisturb;
    }

    enum MessageType {
        PRIVATE, BROADCAST
    }
}
