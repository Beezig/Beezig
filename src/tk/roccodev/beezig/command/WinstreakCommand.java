package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.utils.StreakUtils;

public class WinstreakCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "winstreak";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/winstreak", "/streak"};
    }


    @Override
    public boolean execute(String[] args) {

        String mode = args.length > 0 ? args[0] : ActiveGame.current();
        The5zigAPI.getAPI().messagePlayer(StreakUtils.getMessageForCommand(mode.toLowerCase()));
        return true;
    }


}
