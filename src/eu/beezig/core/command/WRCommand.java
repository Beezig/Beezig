package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.games.DR;
import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;
import pw.roccodev.beezig.hiveapi.wrapper.speedrun.WorldRecord;

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
                WorldRecord wr = DrStats.getWorldRecord(DR.activeMap.getSpeedrunID());
                The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §b" + DR.activeMap.getDisplayName() + "§3 is §b" + getWorldRecord(wr.getTime()) + "§3 by §b" + wr.getHolderName());
            }).start();

        } else {
            String mapName = String.join(" ", args);
            DRMap map = DR.mapsPool.get(mapName.toLowerCase());
            new Thread(() -> {
                WorldRecord wr = DrStats.getWorldRecord(map.getSpeedrunID());
                The5zigAPI.getAPI().messagePlayer(Log.info + "The World Record on map §b" + map.getDisplayName() + "§3 is §b" + getWorldRecord(wr.getTime()) + "§3 by §b" + wr.getHolderName());
            }).start();


        }

        return true;
    }

    public static String getWorldRecord(double time) {


        if (time >= 60) {
            int seconds = (int) (Math.floor(time) % 60);
            double millis = Math.floor(((time - seconds) - 60) * 1000) / 1000;
            int minutes = Math.floorDiv((int) (time - millis), 60);
            if (seconds < 10) {
                return (minutes + ":0" + (seconds + millis));
            }
            return (minutes + ":" + (seconds + millis));
        }
        return "0:" + time;

    }


}
