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
