package eu.beezig.core.command;

import eu.beezig.core.utils.ws.Connector;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

public class DebugCommand implements Command {
    public static boolean go = false;

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "bdev";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/bdev"};
    }


    @Override
    public boolean execute(String[] args) {
        //some debug code here v

        JSONObject packet = new JSONObject();
        packet.put("opcode", PacketOpcodes.S_REQUEST_ONLINE_USERS);

        new Thread(() -> Connector.client.sendJson(packet)).start();

        The5zigAPI.getAPI().getRenderHelper().drawLargeText("Test");

        return true;

    }
}

