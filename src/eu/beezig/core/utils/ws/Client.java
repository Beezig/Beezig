package eu.beezig.core.utils.ws;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.the5zig.mod.The5zigAPI;
import org.java_websocket_beezig.client.WebSocketClient;
import org.java_websocket_beezig.handshake.ServerHandshake;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class Client extends WebSocketClient {

    public Client(URI serverUri) {
        super(serverUri);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // TODO Auto-generated method stub
        System.out.println("Connected!");

        JSONObject message = new JSONObject();
        message.put("opcode", 0x01);
        message.put("ua", Log.getUserAgent());

        GameProfile profile = The5zigAPI.getAPI().getGameProfile();
        message.put("uuid", profile.getId().toString().replace("-", ""));
        message.put("name", profile.getName());
        message.put("platform", BeezigMain.laby ? "LabyMod" : "5zig");

        sendJson(message);

    }

    @Override
    public void onMessage(String message) {
        PacketHandler.parseIncomingPacket(message);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket (" + code + "): " + reason + " [" + remote + "]");
        System.out.println("Attempting to reconnect...");
        new Thread(() -> {
            Connector.client.close();
            try {
                Connector.client = new Client(new URI(Connector.URL));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Connector.client.connect();
        }).start();

    }

    @Override
    public void onError(Exception ex) {
        // TODO Auto-generated method stub
        ex.printStackTrace();
    }

    public void sendJson(JSONObject object) {
        send(object.toJSONString());
    }

}
