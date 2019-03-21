package eu.beezig.core.utils.ws.api.handler;

import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

public class C02Refetch extends PacketHandler {
    @Override
    public int getOpcode() {
        return PacketOpcodes.C_REFETCH;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {
        new Thread(() -> {
            BeezigMain.refetchMaps();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Map data has been re-fetched due to a remote request.");
        }, "Maps Fetcher").start();
    }
}
