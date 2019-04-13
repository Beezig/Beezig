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
import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.AdvancedRecords;
import eu.beezig.core.advancedrecords.anywhere.AdvancedRecordsAnywhere;
import eu.the5zig.mod.The5zigAPI;

import java.util.List;

public class StatsOverlayCommand implements Command {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/records", "/stats"};
    }

    @Override
    public boolean execute(String[] args) {
        if (AdvancedRecords.isRunning) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Advanced Records is already running.");
            return true;
        }
        if (args.length == 2) {
            AdvancedRecordsAnywhere.run(args[0].trim(), args[1].trim());
            return true;
        } else AdvancedRecords.player = args.length == 0
                ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "")
                : args[0].trim();
        return false;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        return null;
    }
}
