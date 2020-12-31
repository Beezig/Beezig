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
import eu.beezig.core.net.Packet;
import eu.beezig.core.net.handler.Connection;
import eu.beezig.core.net.profile.role.UserRole;
import eu.beezig.core.net.util.PacketBuffer;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;

public class PacketUserSettings implements Packet {
    private Action action;
    private UserRole newRole;
    private boolean optDaily;

    public static PacketUserSettings changeDisplayRole(UserRole role) {
        PacketUserSettings pkt = new PacketUserSettings(Action.UPDATE_DISPLAY_ROLE);
        pkt.newRole = role;
        return pkt;
    }

    public static PacketUserSettings optDaily(boolean optIn) {
        PacketUserSettings pkt = new PacketUserSettings(Action.OPT_DAILY);
        pkt.optDaily = optIn;
        return pkt;
    }

    private PacketUserSettings(Action action) {
        this.action = action;
    }

    public PacketUserSettings() {}

    @Override
    public void read(PacketBuffer buffer) {
        action = Action.values()[buffer.readByte()];
        if(action == Action.OPT_DAILY) optDaily = buffer.readBoolean();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeByte((byte) action.ordinal());
        if(action == Action.UPDATE_DISPLAY_ROLE) buffer.writeByte((byte) newRole.getIndex());
        else if(action == Action.OPT_DAILY) buffer.writeBoolean(optDaily);
    }

    @Override
    public void handle(Connection handler) {
        if(action == Action.UPDATE_DISPLAY_ROLE) WorldTask.submit(() -> Message.info(Message.translate("msg.config.save")));
        else if(action == Action.OPT_DAILY) {
            if(Beezig.net().getProfile() != null) Beezig.net().getProfile().setHasDailyScores(optDaily);
            WorldTask.submit(() -> Message.info(Message.translate("msg.daily.opt." + (optDaily ? "in" : "out"))));
        }
    }

    private enum Action {
        UPDATE_DISPLAY_ROLE, OPT_DAILY
    }
}
