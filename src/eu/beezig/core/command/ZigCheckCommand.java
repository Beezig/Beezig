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

import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ZigCheckCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "zigUser";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/5zig", "/zig"};
    }


    @Override
    public boolean execute(String[] args) {

        if (args.length > 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Checking...");
            if (args[0].equals("-a")) {
                List<String> players = new ArrayList<>();
                new Thread(() -> {
                    int t = The5zigAPI.getAPI().getServerPlayers().size();
                    for (NetworkPlayerInfo i : The5zigAPI.getAPI().getServerPlayers()) {
                        if (check(i.getGameProfile().getId().toString().replace("-", "")))
                            players.add(ChatColor.stripColor(i.getGameProfile().getName()));
                    }
                    The5zigAPI.getAPI().messagePlayer("\n"
                            + "    §7§m                                                                                    ");
                    for (String s : players) {
                        The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + s);
                    }
                    The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "Found §b" + players.size() + "§3 5zig users out of §b" + t + " §3players.");
                    The5zigAPI.getAPI().messagePlayer(
                            "    §7§m                                                                                    "
                                    + "\n");
                }).start();

            } else {
                new Thread(() -> {

                    if (check(UsernameToUuid.getUUID(args[0]))) {
                        The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + args[0] + "§3 is a 5zig user.");

                    } else {
                        The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.AQUA + args[0] + "§c is not a 5zig user.");
                    }
                }).start();
            }

        } else
            The5zigAPI.getAPI().messagePlayer(Log.error + "You need to specify a player to check, or -a to check everyone in the current lobby.");

        return true;
    }


    private boolean check(String user) {
        HttpURLConnection conn = null;
        try {
            String uuid = user.length() == 32 ? user : UsernameToUuid.getUUID(user);
            URL url = new URL("http://textures.5zig.net/checkUser/" + uuid);
            conn = (HttpURLConnection) url.openConnection();
            return conn.getResponseCode() == 200;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }


}
