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
