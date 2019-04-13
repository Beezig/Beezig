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
import eu.beezig.core.utils.URLs;
import eu.beezig.core.utils.ws.Connector;
import eu.the5zig.mod.The5zigAPI;

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
                    URL url = new URL(URLs.MAIN_URL + "/submitParty?sender=" + The5zigAPI.getAPI().getGameProfile().getName() + "&user=" + player.trim());
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
