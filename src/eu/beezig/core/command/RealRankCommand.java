/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.command;

import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

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
