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
import eu.beezig.core.notes.NotesManager;
import eu.the5zig.mod.The5zigAPI;

public class NotesCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "notes";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/notes"};
    }

    @Override
    public boolean execute(String[] args) {
        if (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("timv")) {
            The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "Notes:");
            for (String s : NotesManager.notes) {
                The5zigAPI.getAPI().messagePlayer("§7 - §b" + s);
            }
            The5zigAPI.getAPI().messagePlayer("\n");
            return true;
        } else {
            return false;
        }


    }


}
