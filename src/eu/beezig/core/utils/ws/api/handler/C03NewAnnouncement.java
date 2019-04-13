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

import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONObject;

public class C03NewAnnouncement extends PacketHandler {
    @Override
    public int getOpcode() {
        return PacketOpcodes.C_NEW_ANNOUNCEMENT;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {
        JSONObject data = (JSONObject)packetIn.get("data");

        String title = data.get("title").toString();
        String content = data.get("content").toString();

        The5zigAPI.getAPI().messagePlayer(Log.info + "§bNEW ANNOUNCEMENT!");
        The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.translateAlternateColorCodes('&', title.trim()));
        The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.translateAlternateColorCodes('&', content.trim()));
        The5zigAPI.getAPI().playSound("note.pling", 1f);
    }
}
