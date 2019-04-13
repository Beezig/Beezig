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

package eu.beezig.core;

import eu.beezig.core.command.Command;
import eu.the5zig.mod.The5zigAPI;

import java.util.*;

public class CommandManager {

    public static Set<Command> commandExecutors = new HashSet<>();


    /*
     * Dispatches a command.
     *
     * @return whether the command has been found
     *
     *
     */
    public static boolean dispatchCommand(String str) {

        String[] data = str.split(" ");
        String alias = data[0];
        Command cmdFound = null;
        for (Command cmd : commandExecutors) {
            for (String s : cmd.getAliases()) {
                if (s.equalsIgnoreCase(alias)) {
                    cmdFound = cmd;
                    break;
                }
            }
        }
        if (cmdFound == null) return false;

        List<String> dataList = new ArrayList<>(Arrays.asList(data));
        dataList.remove(0); //Remove alias


        try {
            if (!cmdFound.execute(dataList.toArray(new String[0]))) {
                return false; //Skip the command
            }

        } catch (Exception e) {
            e.printStackTrace();
            The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred while attempting to perform this command.");
        }
        return true;
    }

    public static void registerCommand(Command cmd) {
        commandExecutors.add(cmd);
    }

}
