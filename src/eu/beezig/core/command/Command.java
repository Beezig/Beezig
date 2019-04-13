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

import com.mojang.authlib.GameProfile;

import java.util.List;

public interface Command {


    String getName();

    String[] getAliases();

    /*
     *
     * Executes the command
     *
     * @return !whether the command should be skipped and the server command should be run.
     *
     * Example:
     * "seen" command can only be run in Hive. If the server is not Hive, return false, "seen" server-side will be run.
     *
     */
    boolean execute(String[] args);

    default List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        return null;
    }


}
