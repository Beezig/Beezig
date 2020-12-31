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
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;

public class MessageOverlay {
    private String currentTarget;

    @EventHandler
    public void onSend(ChatSendEvent event) {
        if(currentTarget == null || !ServerHive.isCurrent()) return;
        ServerHive hive = ServerHive.current();
        if(hive.isInPartyChat()) {
            // Ensure target is null after party chat reset
            currentTarget = null;
            return;
        }
        String message = event.getMessage();
        if(message.trim().charAt(0) == '/') return;
        event.setCancelled(true);
        Beezig.api().sendPlayerMessage("/msg " + currentTarget + " " + message);
    }

    public void follow(String target) {
        currentTarget = target;
        Message.info(Beezig.api().translate("msg.msg_overlay.follow", Color.accent() + target + Color.primary()));
    }

    public void reset() {
        currentTarget = null;
        Message.info(Message.translate("msg.msg_overlay.reset"));
    }
}
