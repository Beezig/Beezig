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
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.utils.soundcloud.TrackPlayer;
import eu.the5zig.mod.The5zigAPI;

public class VolumeCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "vol";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/volume", "/vol"};
    }


    @Override
    public boolean execute(String[] args) {

        if (!ActiveGame.is("bp")) return false;
        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage /volume [0-100].");
            return true;
        }
        try {
            int vol = Integer.parseInt(args[0]);
            if (vol > 100 || vol < 0) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Volume must be within 0-100!");
                return true;
            }

            TrackPlayer.gainToLoad = vol - 50f;
            TrackPlayer.rawGainToLoad = vol / 100f;
            if (TrackPlayer.player != null) TrackPlayer.player.player.setGain(vol - 50f);
            new Thread(() -> TrackPlayer.saveNewGain(vol)).start();

            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully changed volume.");

        } catch (NumberFormatException e) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Volume must be within 0-100!");
        }

        return true;
    }


}
