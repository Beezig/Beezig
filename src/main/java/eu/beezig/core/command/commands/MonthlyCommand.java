/*
 * Copyright (C) 2017-2021 Beezig Team
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

import eu.beezig.core.calc.lb.MonthlyCalculator;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.ActiveGame;

public class MonthlyCommand implements Command {
    @Override
    public String getName() {
        return "monthly";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/monthly"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        int start = 0, end = 1;
        String mode;
        String player = null;
        if(args.length == 1) {
            if(args[0].matches("\\d+")) start = end = Integer.parseInt(args[0], 10);
            else player = args[0];
            mode = ActiveGame.getID();
        }
        else if(args.length == 2) {
            if(args[1].matches("\\d+")) {
                start = Integer.parseInt(args[0], 10);
                end = Integer.parseInt(args[1], 10);
                mode = ActiveGame.getID();
            }
            else if(args[0].matches("\\d+")) {
                start = end = Integer.parseInt(args[0], 10);
                mode = args[1];
            }
            else {
                player = args[0];
                mode = args[1];
            }
        }
        else if(args.length == 3) {
            start = Integer.parseInt(args[0], 10);
            end = Integer.parseInt(args[1], 10);
            mode = args[2];
        }
        else {
            sendUsage("/monthly [start|player] (end) (mode)\nEx. /monthly 1 timv");
            return true;
        }
        new MonthlyCalculator(mode, player, start, end).run();
        return true;
    }
}
