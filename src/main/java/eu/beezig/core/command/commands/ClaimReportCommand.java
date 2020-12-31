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
import eu.beezig.core.net.packets.PacketReport;

import java.util.Arrays;
import java.util.List;

public class ClaimReportCommand implements Command {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/bclaim", "/brc"};
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length == 0) {
            sendUsage("/bclaim [id] (-h: handle) (-tp [name]: teleport to player)");
            return true;
        }
        int id = Integer.parseInt(args[0], 10);
        List<String> list = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
        Beezig.net().getHandler().sendPacket(PacketReport.claim(id));
        if(list.contains("-h")) Beezig.net().getHandler().sendPacket(PacketReport.handle(id));
        int tpIdx;
        if((tpIdx = list.indexOf("-tp")) != -1) {
            String name = list.get(tpIdx + 1);
            Beezig.api().sendPlayerMessage("/modtp " + name);
        }
        return true;
    }
}
