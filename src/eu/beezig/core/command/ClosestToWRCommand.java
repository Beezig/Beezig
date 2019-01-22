package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.stuff.dr.ClosestToWR;
import eu.the5zig.mod.The5zigAPI;

public class ClosestToWRCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "closestwr";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/cwr", "/bestmap"};
    }


    @Override
    public boolean execute(String[] args) {

        The5zigAPI.getAPI().messagePlayer(Log.info + "Checking...");

        new Thread(() -> {
            try {
                ClosestToWR.fetch(args.length == 0 ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "") : args[0], args.length > 1);

                The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
            } catch (Exception e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An error occured.");
            }

        }).start();

        return true;
    }


}
