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

package eu.beezig.core.utils.ws.api;

import eu.beezig.core.utils.ws.api.handler.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class PacketHandler {

    public static Set<PacketHandler> handlers = new HashSet<>();

    public abstract int getOpcode();

    public abstract void handlePacket(JSONObject packetIn);

    public static void registerHandlers() {
        handlers.clear();

        handlers.add(new C00Error());
        handlers.add(new C01OnlineUsers());
        handlers.add(new C02Refetch());
        handlers.add(new C03NewAnnouncement());
        handlers.add(new C04NewReport());
        handlers.add(new C05ReportClaimed());
    }

    public static void parseIncomingPacket(String stringIn) {
        try {

            JSONParser parser = new JSONParser();
            JSONObject jsonIn = (JSONObject) parser.parse(stringIn);

            int opcode = Math.toIntExact((long) jsonIn.get("opcode"));

            Optional<PacketHandler> handler = handlers.stream().filter(h -> h.getOpcode() == opcode).findAny();
            handler.ifPresent(packetHandler -> packetHandler.handlePacket(jsonIn));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
