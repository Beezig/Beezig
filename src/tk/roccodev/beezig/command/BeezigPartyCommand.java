package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.utils.ws.Connector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeezigPartyCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "bparty";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/bparty"};
    }


    @Override
    public boolean execute(String[] args) {

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (args[0].equalsIgnoreCase("search")) {
            String amount = args[1];
            List<String> argsL = new ArrayList<>(Arrays.asList(args));
            argsL.remove(0);
            argsL.remove(0);
            String mode = String.join(" ", argsL);
            new Thread(() -> Connector.client.send("Looking for Party: " + The5zigAPI.getAPI().getGameProfile().getName() + "§" + mode + "§" + amount.trim())).start();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully requested party.");
        } else if (args[0].equalsIgnoreCase("accept")) {
            new Thread(() -> {
                String player = args[1];
                try {
                    URL url = new URL("https://app-beezigmainserver.wedeploy.io/submitParty?sender=" + The5zigAPI.getAPI().getGameProfile().getName() + "&user=" + player.trim());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn.getResponseCode() == 200) {
                        The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully accepted invite.");
                    } else {
                        The5zigAPI.getAPI().messagePlayer(Log.error + "Invite not found.");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }).start();

        }


        return true;
    }


}
