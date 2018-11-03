package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.wrapper.NetworkRank;

public class RealRankCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "realrank";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/realrank", "/rr"};
    }

    @Override
    public boolean execute(String[] args) {
        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (!(args.length == 1)) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /realrank [player]");
            return true;
        }
        The5zigAPI.getAPI().messagePlayer(Log.info + "Connecting to API...");
        new Thread(() -> {
            HivePlayer api = new HivePlayer(args[0], true);
            String ign = api.getUsername();
            String networkRank = api.getRank().getHumanName();
            ChatColor rankColor = NetworkRank.fromDisplay(networkRank).getColor();
            if (NetworkRank.fromDisplay(networkRank).getLevel() >= 50 && NetworkRank.fromDisplay(networkRank).getLevel() < 80) {

                //TODO Never works outside the Hub - the reason being Hive's game plugins & .getDisplayName()
                The5zigAPI.getAPI().messagePlayer("§7▏ §aBeezig§7 ▏ §c§lCouldn't check " + ign + " !");
                The5zigAPI.getAPI().messagePlayer("§7▏ §aBeezig§7 ▏ §7You can't check VIP's or above!");
            } else
                The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + ign + "§3's Rank: " + rankColor + networkRank);
        }).start();
        return true;
    }


}
