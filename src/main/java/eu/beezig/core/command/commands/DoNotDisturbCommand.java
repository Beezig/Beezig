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

package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.notification.NotificationManager;

public class DoNotDisturbCommand implements Command {
    @Override
    public String getName() {
        return "dnd";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/donotdisturb", "/dnd", "/busy"};
    }

    @Override
    public boolean execute(String[] args) {
        boolean dnd = Beezig.get().getNotificationManager().isDoNotDisturb();
        if(args.length == 0) dnd = !dnd;
        else {
            String value = args[0];
            if("true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value)) dnd = true;
            else if("false".equalsIgnoreCase(value) || "off".equalsIgnoreCase(value)) dnd = false;
        }
        NotificationManager.ActivationCause cause;
        if(args.length > 1 && "process".equals(args[1])) cause = NotificationManager.ActivationCause.PROCESS;
        else cause = NotificationManager.ActivationCause.USER;
        Beezig.get().getNotificationManager().setDoNotDisturb(dnd, cause);
        return true;
    }
}
