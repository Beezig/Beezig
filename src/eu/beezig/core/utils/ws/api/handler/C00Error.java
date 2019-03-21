package eu.beezig.core.utils.ws.api.handler;

import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import org.json.simple.JSONObject;

public class C00Error extends PacketHandler {

    @Override
    public int getOpcode() {
        return PacketOpcodes.C_ERROR;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {

    }
}
