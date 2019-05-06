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

import eu.beezig.core.BeezigMain;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.Connector;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.beezig.core.utils.ws.api.handler.C04NewReport;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

public class ClaimReportCommand implements Command {

    @Override
    public String getName() {
        return "claimreport";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/claim", "/claimreport", "/reportclaim"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if(!BeezigMain.isStaffChat()) return false;
        if(args.length != 1) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Usage: /claimreport <name>");
            return true;
        }
        else {
            String player = args[0];
            if(!C04NewReport.newReports.containsKey(player)) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Can't claim report: report not found.");
                return true;
            }

            JSONObject packet = new JSONObject();
            packet.put("opcode", PacketOpcodes.S_CLAIM_REPORT);
            packet.put("claimed", player);
            packet.put("sender", C04NewReport.newReports.get(player));

            Connector.client.sendJson(packet);
            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully claimed report.");
            The5zigAPI.getAPI().sendPlayerMessage("/modtp " + player);
        }
        return true;
    }
}
