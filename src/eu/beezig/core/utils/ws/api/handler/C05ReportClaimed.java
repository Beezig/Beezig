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

package eu.beezig.core.utils.ws.api.handler;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class C05ReportClaimed extends PacketHandler {
    public static Set<String> claimedReports = new HashSet<>();

    @Override
    public int getOpcode() {
        return PacketOpcodes.C_REPORT_CLAIMED;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {
        JSONObject data = (JSONObject) packetIn.get("data");

        long mode = (long) data.get("mode");
        String player = data.get("player").toString();
        if(claimedReports.contains(player)) return;

        addToSet(player);

        switch(Math.toIntExact(mode)) {
            case 0:
                The5zigAPI.getAPI().messagePlayer(Log.info + "Your report against §b" + player + "§3 was claimed by" +
                        " a moderator.");
                break;
            case 1:
                if(!BeezigMain.isStaffChat()) return;
                The5zigAPI.getAPI().messagePlayer(Log.info + "The report against §b" + player + "§3 was claimed.");
                break;
        }
    }

    private void addToSet(String player) {
        if(claimedReports.size() > 200) claimedReports.clear();
        claimedReports.add(player);
    }
}
