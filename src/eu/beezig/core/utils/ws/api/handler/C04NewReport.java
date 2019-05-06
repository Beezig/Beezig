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
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.NotificationManager;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class C04NewReport extends PacketHandler {
    public static HashMap<String, String> newReports = new HashMap<>();

    @Override
    public int getOpcode() {
        return PacketOpcodes.C_NEW_REPORT;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {
        if (Setting.MOD_REPORT_NOTIFICATION.getValue() && BeezigMain.isStaffChat()) {
            JSONObject data = (JSONObject) packetIn.get("data");

            String users = data.get("target").toString();
            String[] parsedUsers = users.split(",");

            String reason = data.get("reason").toString();
            The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + users + "§3 " + (parsedUsers.length == 1 ? "is" : "are")
                    + " being reported for §b" + reason + "§3. Run §b/claim§3 to claim and teleport.");
            if (Setting.PM_PING.getValue()) {
                The5zigAPI.getAPI().playSound("note.pling", 1f);
            }
            if (Setting.PM_NOTIFICATION.getValue() && NotificationManager.isInGameFocus()) {
                NotificationManager.sendNotification("New Report Received", users + " reported for " + reason);
            }
            SendTutorial.send("new_report");

            String sender = data.get("sender").toString();
            for(String user : parsedUsers) {
                user = user.trim();

                putReport(user, sender);
            }
        }
    }

    private void putReport(String user, String sender) {
        if(newReports.size() > 200) newReports.clear();
        newReports.put(user, sender);
    }
}
