/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.command.commands;

import eu.beezig.core.calc.ps.PlayerStats;
import eu.beezig.core.calc.ps.PlayerStatsCalculator;
import eu.beezig.core.calc.ps.PlayerStatsMode;
import eu.beezig.core.command.Command;
import eu.beezig.core.util.ActiveGame;

import java.util.Locale;

public class PlayerStatsCommand implements Command {
    @Override
    public String getName() {
        return "ps";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/ps", "/playerstats"};
    }

    @Override
    public boolean execute(String[] args) {
        String modeName;
        PlayerStatsMode mode = null;
        String stat = "points";
        if(args.length >= 2) {
            modeName = args[0];
            stat = args[1];
        }
        else if(args.length == 0) modeName = ActiveGame.getID();
        else {
            String param = args[0].toLowerCase(Locale.ROOT);
            PlayerStats ps = new PlayerStats();
            // Set mode to first arg, if mode == null then it's invalid, so we try to parse it as a statistic
            if((mode = ps.getModes().get(param)) == null) {
                modeName = ActiveGame.getID();
                stat = param;
                mode = ps.getModes().get(modeName == null ? null : modeName.toLowerCase(Locale.ROOT));
            } else modeName = param; // If the mode was valid, set the appropriate alias
        }
        if(mode == null)
            mode = modeName == null ? null : new PlayerStats().getModes().get(modeName.toLowerCase(Locale.ROOT));
        PlayerStatsCalculator.calculate(mode, modeName, stat, null);
        return true;
    }
}
