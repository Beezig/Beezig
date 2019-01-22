package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

import java.util.ArrayList;
import java.util.List;

public class MessageOverlayCommand implements Command {

    public static String toggledName = "";

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "msg";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/msg", "/tell"};
    }


    @Override
    public boolean execute(String[] args) {

        if (args.length == 0 && !toggledName.isEmpty()) {
            toggledName = "";
            The5zigAPI.getAPI().messagePlayer(Log.info + "Now sending messages to normal chat.");
            return true;
        }
        if (args.length != 1 || !(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;


        MessageOverlayCommand.toggledName = args[0];
        new Thread(() -> {
            HivePlayer api = new HivePlayer(toggledName);
            The5zigAPI.getAPI().messagePlayer(Log.info + "Now sending messages to " +
                    NetworkRank.fromDisplay(api.getRank().getHumanName()).getColor() + api.getUsername() + "§3.");
        }).start();


        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        return new ArrayList<>();
    }
}
