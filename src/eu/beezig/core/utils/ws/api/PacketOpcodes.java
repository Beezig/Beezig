package eu.beezig.core.utils.ws.api;

public class PacketOpcodes {
    public static final int C_ERROR = 0xC00;
    public static final int C_ONLINE_USERS = 0xC01;
    public static final int C_REFETCH = 0xC02;
    public static final int C_NEW_ANNOUNCEMENT = 0xC03;
    public static final int C_NEW_REPORT = 0xC04;

    public static final int S_IDENTIFICATION = 0x001;
    public static final int S_REQUEST_ONLINE_USERS = 0x002;
    public static final int S_BEEZIGFORGE_LOADED = 0x003;
}
