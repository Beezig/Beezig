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

package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;

public class BroadcastReplyCommand implements Command {
    @Override
    public String getName() {
        return "br";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/broadcastreply", "/br", "/bcr"};
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length == 0) {
            sendUsage("/br [message]");
            return true;
        }
        String msg = String.join(" ", args);
        Beezig.api().sendPlayerMessage("/msg " + Beezig.get().getAntiSniper().getLastBroadcastSender() + " " + msg);
        return true;
    }
}
