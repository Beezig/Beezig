/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.command;

import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.utils.autogg.Triggers;
import eu.the5zig.mod.The5zigAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UptimeCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "uptime";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/uptime"};
    }


    @Override
    public boolean execute(String[] args) {

        The5zigAPI.getAPI().messagePlayer(Log.bar + "\n");
        The5zigAPI.getAPI().messagePlayer(Log.info + (IHive.joined == 0 ? "§3Not on Hive" : "On Hive since §b" + new SimpleDateFormat("HH:mm").format(new Date(IHive.joined))) + "§3.");
        The5zigAPI.getAPI().messagePlayer(Log.info + (Triggers.lastPartyJoined == 0 ? "§3Not in a party" : "In a party since §b" + new SimpleDateFormat("HH:mm").format(new Date(Triggers.lastPartyJoined))) + "§3.");
        The5zigAPI.getAPI().messagePlayer(Log.bar + "\n");

        return true;
    }


}
