package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;

public class CheckPingCommand implements Command {

    @Override
    public String getName() {
        return "checkping";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/cping", "/checkping"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length == 1) {
            for (int i = 0; i < The5zigAPI.getAPI().getServerPlayers().size(); i++) {
                if (The5zigAPI.getAPI().getServerPlayers().get(i).getGameProfile().getName().equalsIgnoreCase(args[0])) {
                    int ping = The5zigAPI.getAPI().getServerPlayers().get(i).getPing();
                    The5zigAPI.getAPI().messagePlayer(Log.info + args[0] + "'s Ping is: §b" + ping + "ms");
                    return true;
                }
            }
            The5zigAPI.getAPI().messagePlayer(Log.error + "Player not found. Player has to be connected to your server (Tab).");
        } else The5zigAPI.getAPI().messagePlayer(Log.error + "Usage: /cping [player]");

        return true;
    }
}
