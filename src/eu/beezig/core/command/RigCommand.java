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

import java.util.concurrent.ThreadLocalRandom;

public class RigCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "rig";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/rig"};
    }


    @Override
    public boolean execute(String[] args) {


        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (ThreadLocalRandom.current().nextDouble(1, 101) < 36D) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "I can feel it! It's the §bbest moment§3 to open a crate! Good luck.");
        } else {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Nah... maybe next time?");
        }

        return true;
    }


}
