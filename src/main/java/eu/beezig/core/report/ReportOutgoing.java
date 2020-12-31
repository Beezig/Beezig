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

package eu.beezig.core.report;

import eu.beezig.core.net.util.PacketBuffer;

import java.util.List;

public class ReportOutgoing {
    private final ReportType type;
    private final List<String> targets;
    private final List<String> reasons;

    public ReportOutgoing(ReportType type, List<String> targets, List<String> reasons) {
        this.type = type;
        this.targets = targets;
        this.reasons = reasons;
    }

    public enum ReportType {
        BLOCK, PLAYER
    }

    public void writeTo(PacketBuffer buffer) {
        buffer.writeByte((byte) type.ordinal());
        buffer.writeByte((byte) targets.size());
        for(String player : targets) buffer.writeString(player);
        buffer.writeByte((byte) reasons.size());
        for(String reason : reasons) buffer.writeString(reason);
    }
}
