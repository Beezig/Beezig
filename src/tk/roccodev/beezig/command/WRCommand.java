package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.DR;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRMap;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiDR;

public class WRCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "wr";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/wr"};
    }

    @Override
    public boolean execute(String[] args) {
        if (!(ActiveGame.is("dr"))) return false;
        if (args.length == 0 && DR.activeMap != null) {
            new Thread(() -> {
                ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
                The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §b" + DR.activeMap.getDisplayName() + "§3 is §b" + api.getWorldRecord(DR.activeMap) + "§3 by §b" + DR.currentMapWRHolder);
            }).start();

        } else {
            String mapName = String.join(" ", args);
            DRMap map = DR.mapsPool.get(mapName.toLowerCase());
            new Thread(() -> {
                ApiDR api = new ApiDR(The5zigAPI.getAPI().getGameProfile().getName());
                The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §b" + map.getDisplayName() + "§3 is §b" + api.getWorldRecord(map) + "§3 by §b" + api.getWorldRecordHolder(map));
            }).start();


        }

        return true;
    }


}
