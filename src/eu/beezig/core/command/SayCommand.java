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

public class SayCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "servercommand";
    }

    @Override
    public String[] getAliases() {
        // "/s" is a staff command.
        return new String[]{"/servercommand"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(s).append(" ");
            }
            if (args[0].startsWith("/")) {
                The5zigAPI.getAPI().sendPlayerMessage(sb.toString());
            } else {
                The5zigAPI.getAPI().sendPlayerMessage("/" + sb.toString());
            }

        } else {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /servercommand [command]");
        }
        return true;
    }


}
