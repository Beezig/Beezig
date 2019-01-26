package eu.beezig.core.command;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public class ColorDebugCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "colordebug";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/colordebug", "/cdebug"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length != 0) {

            String txt = String.join(" ", args);
            The5zigAPI.getAPI().messagePlayer(Log.info + "§r" + ChatColor.translateAlternateColorCodes('&', txt));

            return true;
        }

        BeezigMain.isColorDebug = !BeezigMain.isColorDebug;

        The5zigAPI.getAPI()
                .messagePlayer(Log.info + "Color debug is now: " + (BeezigMain.isColorDebug ? "§aON" : "§cOFF"));

        return true;

    }

}
