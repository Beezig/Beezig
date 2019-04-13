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
import eu.beezig.core.hiveapi.stuff.dr.ClosestToWR;
import eu.the5zig.mod.The5zigAPI;

public class ClosestToWRCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "closestwr";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/cwr", "/bestmap"};
    }


    @Override
    public boolean execute(String[] args) {

        The5zigAPI.getAPI().messagePlayer(Log.info + "Checking...");

        new Thread(() -> {
            try {
                ClosestToWR.fetch(args.length == 0 ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "") : args[0], args.length > 1);

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            } catch (Exception e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An error occured.");
            }

        }).start();

        return true;
    }


}
