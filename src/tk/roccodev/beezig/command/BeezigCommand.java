package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.CommandManager;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.utils.ws.Connector;

public class BeezigCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "info";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/beezig"};
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");
            The5zigAPI.getAPI().messagePlayer(Log.info + "Running Beezig §bv" + BeezigMain.BEEZIG_VERSION + " ("
                    + (BeezigMain.VERSION_HASH.isEmpty() ? "Stable" : "Beta/" + BeezigMain.VERSION_HASH) + ")");
            The5zigAPI.getAPI().messagePlayer(Log.info + (BeezigMain.newUpdate ? "There is a new version available!"
                    : "You're running the latest version."));
            The5zigAPI.getAPI().messagePlayer(Log.info + "For a list of commands, run §b/beezig commands§3.\n");
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");

        } else if (args[0].equalsIgnoreCase("commands")) {
            The5zigAPI.getAPI().messagePlayer("\n" +
                    "    §7§m                                                                                    "
                            );
            The5zigAPI.getAPI().messagePlayer(Log.info + "Available commands:");
            for (Command cmd : CommandManager.commandExecutors) {
                The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + cmd.getAliases()[0]);
            }
            The5zigAPI.getAPI().messagePlayer(
                    "    §7§m                                                                                    "
                            + "\n");
        } else if (args[0].equalsIgnoreCase("reconnect")) {
            Connector.client.reconnect();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Reconnected.");
        }
        return true;
    }

}
