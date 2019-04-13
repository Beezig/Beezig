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
import eu.beezig.core.games.DR;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;

public class DeathrunRecordsCommand implements Command {
    @Override
    public String getName() {
        return "drrecords";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/drbest", "/drrec"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /drbest player map");
            return true;
        }

        new Thread(() -> {
            try {
                String player = args.length <= 1 ? The5zigAPI.getAPI().getGameProfile().getName() : args[0];
                DrStats api = new DrStats(player);
                String mapInput = args.length <= 1 ? args[0] : args[1];
                String map = DR.mapsPool.get(mapInput.replace("_", " ").toLowerCase()).getHiveAPIName();
                The5zigAPI.getAPI().messagePlayer(Log.info + "Kills Record:§b " + api.getMapKills().get(map));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Deaths Record:§b " + api.getMapDeaths().get(map));

            } catch (Exception e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An Error occurred.");
            }
        }).start();


        return true;
    }
}
