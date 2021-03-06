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

package eu.beezig.core.notification;

import java.time.Instant;

public class IncomingMessage {
    private String sender;
    private String message;
    private Instant sendDate;
    private NotificationManager.MessageType type;

    public IncomingMessage(NotificationManager.MessageType type, String sender, String message) {
        this(type, sender, message, Instant.now());
    }

    public IncomingMessage(NotificationManager.MessageType type, String sender, String message, Instant sendDate) {
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.sendDate = sendDate;
    }

    public NotificationManager.MessageType getType() {
        return type;
    }

    public Instant getSendDate() {
        return sendDate;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
