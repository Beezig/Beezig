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
import eu.beezig.core.server.ServerHive;

public class MessageOverlayCommand implements Command {
    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/msg", "/message", "/w", "/tell"};
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length > 1 || !ServerHive.isCurrent()) return false;
        if(args.length == 0) Beezig.get().getMessageOverlayManager().reset();
        else Beezig.get().getMessageOverlayManager().follow(args[0]);
        return true;
    }
}
