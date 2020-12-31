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
import eu.beezig.core.logging.ws.WinstreakService;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.ActiveGame;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;

import java.time.Instant;
import java.util.Locale;

public class WinstreakCommand implements Command {
    @Override
    public String getName() {
        return "streak";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/bstreak", "/bwinstreak"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        String id = ActiveGame.getID();
        if(args.length == 0 && id == null) {
            sendUsage("/bstreak [mode]");
            return true;
        } else if(args.length > 0) id = args[0];
        WinstreakService service = Beezig.get().getWinstreakManager().getService(id.toLowerCase(Locale.ROOT));
        Message.info(Beezig.api().translate("msg.winstreak", Color.accent() + Message.formatNumber(service.getCurrent()) + Color.primary(),
            Color.accent() + Message.formatNumber(service.getBest()) + Color.primary()));
        Instant last = service.getLastReset() == null ? null : Instant.ofEpochMilli(service.getLastReset());
        Instant best = service.getBestReset() == null ? null : Instant.ofEpochMilli(service.getBestReset());
        if(last != null) Message.info(Beezig.api().translate("msg.winstreak.reset", Color.accent() + Message.formatTime(last) + " (" + StringUtils.getTimeAgo(service.getLastReset()) + ")"));
        if(best != null) Message.info(Beezig.api().translate("msg.winstreak.reset.best", Color.accent() + Message.formatTime(best) + " (" + StringUtils.getTimeAgo(service.getBestReset()) + ")"));
        return true;
    }
}
