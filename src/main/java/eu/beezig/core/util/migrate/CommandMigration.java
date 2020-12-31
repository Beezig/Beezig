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

package eu.beezig.core.util.migrate;

import com.google.common.base.Splitter;
import eu.beezig.core.Beezig;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.server.modes.HIDE;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.server.GameMode;

public class CommandMigration {
    private static final Splitter SPACE = Splitter.on(' ');
    @EventHandler
    public void onChatSend(ChatSendEvent event) {
        if(!ServerHive.isCurrent() || event.getMessage().charAt(0) != '/') return;
        GameMode mode = Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
        String message = SPACE.split(event.getMessage()).iterator().next();
        if("/report".equalsIgnoreCase(message)) sendMessage("/breport");
        else if(mode instanceof DR) {
            if("/wr".equalsIgnoreCase(message)) sendMessage("/drwr");
            else if("/pb".equalsIgnoreCase(message)) sendMessage("/drpb");
        } else if(mode instanceof HIDE) {
            if("/bs".equalsIgnoreCase(message)) sendMessage("/hidebs");
        }
    }

    private void sendMessage(String newCommand) {
        Message.info(Beezig.api().translate("msg.command.migrate", Color.accent() + newCommand + Color.primary()));
    }
}
