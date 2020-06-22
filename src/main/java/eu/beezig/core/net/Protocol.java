/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.net;

import eu.beezig.core.net.packets.PacketAuthentication;
import eu.beezig.core.net.packets.PacketDailyGame;
import eu.beezig.core.net.packets.PacketDisconnect;
import eu.beezig.core.net.packets.PacketIdentification;

import java.util.HashMap;
import java.util.Map;

public class Protocol {
    private Map<Byte, Class<? extends Packet>> packets = new HashMap<>();

    Protocol() {
        register(0x0, PacketDisconnect.class);
        register(0x1, PacketIdentification.class);
        register(0x2, PacketDailyGame.class);
        register(0x3, PacketAuthentication.class);
    }

    private void register(int id, Class<? extends Packet> packet) {
        byte b = (byte) id;
        if (packets.containsKey(b)) {
            throw new RuntimeException("Packet with id " + id + " is already registered!");
        }
        packets.put(b, packet);
    }

    public byte getPacketId(Packet packet) {
        for (Map.Entry<Byte, Class<? extends Packet>> entry : packets.entrySet()) {
            Class c = entry.getValue();
            if (c.isInstance(packet))
                return entry.getKey();
        }
        throw new RuntimeException("Packet " + packet + " is not registered!");
    }

    public Packet getPacket(byte id) throws Exception {
        if (!packets.containsKey(id)) {
            throw new RuntimeException("Could not get unregistered packet (" + id + ")!");
        }
        return packets.get(id).newInstance();
    }
}
