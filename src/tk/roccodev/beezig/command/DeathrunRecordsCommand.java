package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiDR;

public class DeathrunRecordsCommand implements Command {
    @Override
    public String getName() {
        return "drrecords";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/drbest", "/drrec"};
    }

    @Override
    public boolean execute(String[] args) {

        new Thread(() -> {
            try {
                String player = args.length <= 1 ? The5zigAPI.getAPI().getGameProfile().getName() : args[0];
                ApiDR api = new ApiDR(player);
                String mapInput = args.length <= 1 ? args[0] : args[1];
                String map = DR.mapsPool.get(mapInput.replace("_", " ").toLowerCase()).getHiveAPIName();
                The5zigAPI.getAPI().messagePlayer(Log.info + "Kills Record:§b " + api.getKillRecords().get(map));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Deaths Record:§b " + api.getDeathRecords().get(map));

            } catch(Exception e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An Error occured.");
                The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /drbest player map");
            }
        }).start();


        return true;
    }
}
