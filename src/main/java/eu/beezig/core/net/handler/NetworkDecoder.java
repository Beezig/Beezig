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

package eu.beezig.core.net.handler;

import eu.beezig.core.Beezig;
import eu.beezig.core.net.Packet;
import eu.beezig.core.net.util.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NetworkDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Make sure we get enough data for id + length
        if(in.readableBytes() < 5) return;
        in.markReaderIndex();
        byte id = in.readByte();
        int size = in.readInt();
        if(in.readableBytes() < size) {
            in.resetReaderIndex();
            return;
        }
        PacketBuffer buf = new PacketBuffer(in.readSlice(size));
        Packet packet = Beezig.net().getProtocol().getPacket(id);
        Beezig.logger.debug("Packet <- " + packet.getClass().getSimpleName());
        packet.read(buf);
        out.add(packet);
    }
}
