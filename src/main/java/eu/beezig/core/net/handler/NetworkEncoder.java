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

package eu.beezig.core.net.handler;

import eu.beezig.core.Beezig;
import eu.beezig.core.net.Packet;
import eu.beezig.core.net.util.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NetworkEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        Beezig.logger.debug("Packet -> " + msg.getClass().getSimpleName());
        out.writeByte(Beezig.net().getProtocol().getPacketId(msg));
        try(PacketBuffer buffer = new PacketBuffer(Unpooled.buffer())) {
            msg.write(buffer);
            out.writeInt(buffer.getSize());
            out.writeBytes(buffer.getInternal());
        }
    }
}
