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

import eu.beezig.core.calc.ps.PlayerStatsCalculator;
import eu.beezig.core.command.Command;
import eu.beezig.core.util.ActiveGame;

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
        PlayerStatsCalculator.calculate(args.length == 0 ? ActiveGame.getID() : args[0], args.length > 1 ? args[1] : "points", null);
        return true;
    }
}
