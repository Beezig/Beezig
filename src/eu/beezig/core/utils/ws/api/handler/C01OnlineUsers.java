package eu.beezig.core.utils.ws.api.handler;

import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

public class C01OnlineUsers extends PacketHandler {
    @Override
    public int getOpcode() {
        return PacketOpcodes.C_ONLINE_USERS;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {
        The5zigAPI.getAPI().messagePlayer(Log.info + "There are §b" + packetIn.get("data") + "§3 connected clients.");
    }
}
