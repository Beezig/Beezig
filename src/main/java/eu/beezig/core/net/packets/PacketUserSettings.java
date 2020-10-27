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
