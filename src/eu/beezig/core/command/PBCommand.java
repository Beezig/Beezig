package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.games.DR;
import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.DrStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PBCommand implements Command {

    public static String parseTime(long pb) {
        if (pb >= 60) {
            int seconds = Math.toIntExact(pb) % 60;
            int minutes = Math.floorDiv(Math.toIntExact(pb), 60);
            if (seconds < 10) {
                return (minutes + ":0" + seconds);
            }
            return (minutes + ":" + seconds);
        }
        return "0:" + pb;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "pb";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/pb"};
    }

    @Override
    public boolean execute(String[] args) {
        if (!(ActiveGame.is("dr"))) return false;

        if (args.length == 0 && DR.activeMap != null) {

            String ign = The5zigAPI.getAPI().getGameProfile().getName();

            new Thread(() -> {
                DrStats api = new DrStats(ign);
                HivePlayer parent = api.getPlayer();
                ChatColor color = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                The5zigAPI.getAPI().messagePlayer(Log.info + color + "§3Your Personal Best on map §b" + DR.activeMap.getDisplayName() + "§3 is §b" + parseTime(api.getMapRecords().get(DR.activeMap.getHiveAPIName())));
            }).start();
        } else if (args.length == 1 && DR.activeMap != null) {

            String ign = args[0];

            new Thread(() -> {
                DrStats api = new DrStats(ign);
                HivePlayer parent = api.getPlayer();
                ChatColor color = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                The5zigAPI.getAPI().messagePlayer(Log.info + color + parent.getUsername() + "§3's Personal Best on map §b" + DR.activeMap.getDisplayName() + "§3 is §b" + parseTime(api.getMapRecords().get(DR.activeMap.getHiveAPIName())));
            }).start();

        } else {

            String ign = args[0];
            List<String> argsL = new ArrayList<>(Arrays.asList(args));
            argsL.remove(0);
            DRMap map = DR.mapsPool.get(String.join(" ", argsL).toLowerCase());


            new Thread(() -> {
                DrStats api = new DrStats(ign);
                HivePlayer parent = api.getPlayer();
                ChatColor color = NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor();
                The5zigAPI.getAPI().messagePlayer(Log.info + color + parent.getUsername() + "§3's Personal Best on map §b" + map.getDisplayName() + "§3 is §b" + parseTime(api.getMapRecords().get(map.getHiveAPIName())));
            }).start();

        }

        return true;
    }


}
