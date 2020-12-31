/*
 * Copyright (C) 2017-2021 Beezig Team
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
import eu.beezig.core.Constants;
import eu.beezig.core.net.Packet;
import eu.beezig.core.net.Protocol;
import eu.beezig.core.net.handler.Connection;
import eu.beezig.core.net.profile.OwnProfile;
import eu.beezig.core.net.util.PacketBuffer;

public class PacketProfile implements Packet {

    // In
    private int id;
    private byte role;
    private long firstLogin;
    private String regionId;
    private String regionName;
    private boolean dailyScores;

    // Out
    private String timezone;

    public PacketProfile(String timezone) {
        this.timezone = timezone;
    }

    public PacketProfile() {}

    @Override
    public void read(PacketBuffer buffer) {
        id = buffer.readInt();
        role = buffer.readByte();
        firstLogin = buffer.readLong();
        boolean regionOk = buffer.readBoolean();
        if(regionOk) {
            regionId = buffer.readString();
            regionName = buffer.readString();
        }
        dailyScores = buffer.readBoolean();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(Protocol.VERSION);
        buffer.writeString(timezone == null ? "" : timezone);
        buffer.writeString(Constants.VERSION);
        buffer.writeBoolean(Beezig.api().isForgeEnvironment());
    }

    @Override
    public void handle(Connection handler) {
        OwnProfile profile = new OwnProfile(id, role, firstLogin, regionId, regionName);
        profile.setHasDailyScores(dailyScores);
        Beezig.net().setProfile(profile);
    }
}
