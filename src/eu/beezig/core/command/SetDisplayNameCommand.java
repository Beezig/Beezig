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
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;

public class SetDisplayNameCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "setdisplayname";
    }

    @Override
    public String[] getAliases() {

        return new String[]{"/setdisplayname", "/sdn"};
    }

    @Override
    public boolean execute(String[] args) {

        // "/sdn §6HotBoy3294 ItsNiklass"

        if (args.length < 1) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "§3Usage: /sdn <colorLetter+name> <targetPlayer>");
            return true;
        }

        String displayName = args[0];
        String targetPlayer = The5zigAPI.getAPI().getGameProfile().getName();
        if (args.length > 1) {
            targetPlayer = args[1];
        }

        for (NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
            if (npi.getGameProfile().getName().equalsIgnoreCase(targetPlayer)) {
                npi.setDisplayName(ChatColor.translateAlternateColorCodes('&', "§" + displayName));
                The5zigAPI.getAPI().messagePlayer(Log.info + "The name has been updated to §r" + npi.getDisplayName());
            }
        }
        return true;
    }


}
