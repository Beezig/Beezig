package eu.beezig.core.command;

import eu.beezig.core.games.*;
import eu.the5zig.mod.The5zigAPI;

public class ReVoteCommand implements Command {
    @Override
    public String getName() {
        return "revote";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/revote", "/rev"};
    }


    @Override
    public boolean execute(String[] args) {

        //better way?
        TIMV.hasVoted = false;
        DR.hasVoted = false;
        BED.hasVoted = false;
        Giant.hasVoted = false;
        CAI.hasVoted = false;
        GRAV.hasVoted = false;
        HIDE.hasVoted = false;
        MIMV.hasVoted = false;
        SKY.hasVoted = false;

        The5zigAPI.getAPI().sendPlayerMessage("/v");
        return true;

    }
}
