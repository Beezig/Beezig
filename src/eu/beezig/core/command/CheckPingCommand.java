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

import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;

public class CheckPingCommand implements Command {

    @Override
    public String getName() {
        return "checkping";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/cping", "/checkping"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length == 1) {
            for (int i = 0; i < The5zigAPI.getAPI().getServerPlayers().size(); i++) {
                if (The5zigAPI.getAPI().getServerPlayers().get(i).getGameProfile().getName().equalsIgnoreCase(args[0])) {
                    int ping = The5zigAPI.getAPI().getServerPlayers().get(i).getPing();
                    The5zigAPI.getAPI().messagePlayer(Log.info + args[0] + "'s Ping is: §b" + ping + "ms");
                    return true;
                }
            }
            The5zigAPI.getAPI().messagePlayer(Log.error + "Player not found. Player has to be connected to your server (Tab).");
        } else The5zigAPI.getAPI().messagePlayer(Log.error + "Usage: /cping [player]");

        return true;
    }
}
