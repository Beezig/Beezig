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

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.games.DR;
import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;
import pw.roccodev.beezig.hiveapi.wrapper.speedrun.WorldRecord;

public class WRCommand implements Command {

    public static String getWorldRecord(double time) {


        if (time >= 60) {
            int seconds = (int) (Math.floor(time) % 60);
            double millis = Math.floor(((time - seconds) - 60) * 1000) / 1000;
            int minutes = Math.floorDiv((int) (time - millis), 60);
            if (seconds < 10) {
                return (minutes + ":0" + (seconds + millis));
            }
            return (minutes + ":" + (seconds + millis));
        }
        return "0:" + time;

    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "wr";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/wr"};
    }

    @Override
    public boolean execute(String[] args) {
        if (!(ActiveGame.is("dr"))) return false;
        DR dr = (DR) The5zigAPI.getAPI().getActiveServer().getGameListener().getCurrentGameMode();
        if (args.length == 0 && dr.activeMap != null) {
            new Thread(() -> {
                WorldRecord wr = DrStats.getWorldRecord(dr.activeMap.getSpeedrunID());
                The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §b" + dr.activeMap.getDisplayName() + "§3 is §b" + getWorldRecord(wr.getTime()) + "§3 by §b" + wr.getHolderName());
            }).start();

        } else {
            String mapName = String.join(" ", args);
            DRMap map = DR.mapsPool.get(mapName.toLowerCase());
            new Thread(() -> {
                WorldRecord wr = DrStats.getWorldRecord(map.getSpeedrunID());
                The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §b" + map.getDisplayName() + "§3 is §b" + getWorldRecord(wr.getTime()) + "§3 by §b" + wr.getHolderName());
            }).start();


        }

        return true;
    }


}
