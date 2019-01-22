package eu.beezig.core.command;

import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;

import java.util.concurrent.ThreadLocalRandom;

public class RigCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "rig";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/rig"};
    }


    @Override
    public boolean execute(String[] args) {


        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (ThreadLocalRandom.current().nextDouble(1, 101) < 36D) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "I can feel it! It's the §bbest moment§3 to open a crate! Good luck.");
        } else {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Nah... maybe next time?");
        }

        return true;
    }


}
