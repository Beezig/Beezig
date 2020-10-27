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
