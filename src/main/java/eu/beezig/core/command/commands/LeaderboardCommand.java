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

import eu.beezig.core.calc.lb.LeaderboardCalculator;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.ActiveGame;

public class LeaderboardCommand implements Command {
    @Override
    public String getName() {
        return "lb";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/leaderboard", "/lb"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        int start, end;
        String mode;
        if(args.length == 1) {
            start = end = Integer.parseInt(args[0], 10);
            mode = ActiveGame.getID();
        }
        else if(args.length == 2) {
            start = Integer.parseInt(args[0], 10);
            if(args[1].matches("\\d+")) {
                end = Integer.parseInt(args[1], 10);
                mode = ActiveGame.getID();
            }
            else {
                end = start;
                mode = args[1];
            }
        }
        else if(args.length == 3) {
            start = Integer.parseInt(args[0], 10);
            end = Integer.parseInt(args[1], 10);
            mode = args[2];
        }
        else {
            sendUsage("/lb [start] (end) (mode)\nEx. /lb 1 timv");
            return true;
        }
        new LeaderboardCalculator(mode, start, end).run();
        return true;
    }
}
