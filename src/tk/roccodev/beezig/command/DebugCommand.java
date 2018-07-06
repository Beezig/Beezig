package tk.roccodev.beezig.command;

import tk.roccodev.beezig.utils.soundcloud.TrackPlayer;
import tk.roccodev.beezig.utils.ws.Connector;

public class DebugCommand implements Command {
    public static boolean go = false;

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "bdev";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/bdev"};
    }


    @Override
    public boolean execute(String[] args) {
        //some debug code here v

        new Thread(() -> {

            Connector.client.send("Hello there pls get online people");

        }).start();

        return true;

    }
}

