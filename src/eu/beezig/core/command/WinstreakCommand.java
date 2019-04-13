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
import eu.beezig.core.utils.StreakUtils;
import eu.the5zig.mod.The5zigAPI;

public class WinstreakCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "winstreak";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/winstreak", "/streak"};
    }


    @Override
    public boolean execute(String[] args) {

        String mode = args.length > 0 ? args[0] : ActiveGame.current();
        The5zigAPI.getAPI().messagePlayer(StreakUtils.getMessageForCommand(mode.toLowerCase()));
        return true;
    }


}
