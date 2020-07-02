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
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

public class SeenCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "seen";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/seen"};
    }

    @Override
    public boolean execute(String[] args) {
        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (args.length == 1) {

            String ign = args[0];
            HivePlayer api = new HivePlayer(ign);
            new Thread(() -> {
                if (api.getStatus().isOnline()) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + api.getUsername() + "§3 is online");
                } else {
                    The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + api.getUsername() + "§3 is offline");
                }
            }).start();

        } else {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /seen [player]");
        }

        return true;
    }


}
