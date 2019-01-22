package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.beezig.core.games.DR;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;

public class DeathrunRecordsCommand implements Command {
    @Override
    public String getName() {
        return "drrecords";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/drbest", "/drrec"};
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /drbest player map");
            return true;
        }

        new Thread(() -> {
            try {
                String player = args.length <= 1 ? The5zigAPI.getAPI().getGameProfile().getName() : args[0];
                DrStats api = new DrStats(player);
                String mapInput = args.length <= 1 ? args[0] : args[1];
                String map = DR.mapsPool.get(mapInput.replace("_", " ").toLowerCase()).getHiveAPIName();
                The5zigAPI.getAPI().messagePlayer(Log.info + "Kills Record:§b " + api.getMapKills().get(map));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Deaths Record:§b " + api.getMapDeaths().get(map));

            } catch (Exception e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An Error occurred.");
            }
        }).start();


        return true;
    }
}
