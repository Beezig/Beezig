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

package eu.beezig.core.net.packets;

import eu.beezig.core.Beezig;
import eu.beezig.core.net.Packet;
import eu.beezig.core.net.handler.Connection;
import eu.beezig.core.net.profile.UserProfile;
import eu.beezig.core.net.util.PacketBuffer;

import java.util.*;

public class PacketOnlineUsers implements Packet {

    private Collection<UUID> players;
    private Set<UserProfile> profiles = new HashSet<>();
    private int requestId;

    public PacketOnlineUsers(int requestId, Collection<UUID> players) {
        this.players = players;
        this.requestId = requestId;
    }

    public PacketOnlineUsers(int requestId, UUID player) {
        this(requestId, Collections.singletonList(player));
    }

    public PacketOnlineUsers() {}

    @Override
    public void read(PacketBuffer buffer) {
        requestId = buffer.readInt();
        int size = buffer.readInt();
        for(int i = 0; i < size; i++) {
            UUID uuid = buffer.readUUID();
            int role = buffer.readInt();
            profiles.add(new UserProfile(uuid, role));
        }
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(requestId);
        buffer.writeInt(players.size());
        for(UUID player : players) {
            buffer.writeUUID(player);
        }
    }

    @Override
    public void handle(Connection handler) {
        Beezig.get().getNetworkManager().getProfilesCache().putAll(requestId, profiles);
    }
}
